package utp.bambinochicken.bambinochicken.shared.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.health.contributor.Status;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.health.db-monitor.enabled", havingValue = "true", matchIfMissing = true)
public class DualDbHealthMonitor {

    private static final Logger log = LoggerFactory.getLogger(DualDbHealthMonitor.class);

    private final HealthIndicator oracleDb;
    private final HealthIndicator mySqlDb;

    private Boolean oracleUp;
    private Boolean mySqlUp;

    public DualDbHealthMonitor(
            @Qualifier("oracleDb") HealthIndicator oracleDb,
            @Qualifier("mySqlDb") HealthIndicator mySqlDb
    ) {
        this.oracleDb = oracleDb;
        this.mySqlDb = mySqlDb;
    }

    @Scheduled(fixedDelayString = "${app.health.db-monitor.delay-ms:30000}")
    public void monitor() {
        boolean oracleNowUp = Status.UP.equals(oracleDb.health().getStatus());
        boolean mysqlNowUp = Status.UP.equals(mySqlDb.health().getStatus());

        emitTransition("Oracle", oracleUp, oracleNowUp);
        emitTransition("MySQL", mySqlUp, mysqlNowUp);

        oracleUp = oracleNowUp;
        mySqlUp = mysqlNowUp;
    }

    private void emitTransition(String dbName, Boolean previousState, boolean currentState) {
        if (previousState == null) {
            if (currentState) {
                log.info("DB health initialized: {} is UP", dbName);
            } else {
                log.error("DB health initialized: {} is DOWN", dbName);
            }
            return;
        }

        if (!previousState && currentState) {
            log.warn("DB recovered: {} is UP again", dbName);
            return;
        }

        if (previousState && !currentState) {
            log.error("DB outage detected: {} is DOWN", dbName);
        }
    }
}
