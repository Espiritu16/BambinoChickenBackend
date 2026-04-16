package utp.bambinochicken.bambinochicken.shared.health;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("oracleDb")
public class OracleDbHealthIndicator implements HealthIndicator {

    private final JdbcTemplate oracleJdbcTemplate;

    public OracleDbHealthIndicator(@Qualifier("oracleDataSource") javax.sql.DataSource oracleDataSource) {
        this.oracleJdbcTemplate = new JdbcTemplate(oracleDataSource);
    }

    @Override
    public Health health() {
        try {
            Integer ping = oracleJdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            Integer authSessionRows = oracleJdbcTemplate.queryForObject("SELECT COUNT(*) FROM AUTH_SESSION", Integer.class);
            return Health.up()
                    .withDetail("database", "oracle")
                    .withDetail("ping", ping)
                    .withDetail("authSessionRows", authSessionRows)
                    .build();
        } catch (Exception ex) {
            return Health.down(ex)
                    .withDetail("database", "oracle")
                    .build();
        }
    }
}
