package utp.bambinochicken.bambinochicken.shared.health;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("mySqlDb")
public class MySqlDbHealthIndicator implements HealthIndicator {

    private final JdbcTemplate mySqlJdbcTemplate;

    public MySqlDbHealthIndicator(@Qualifier("mysqlDataSource") javax.sql.DataSource mySqlDataSource) {
        this.mySqlJdbcTemplate = new JdbcTemplate(mySqlDataSource);
    }

    @Override
    public Health health() {
        try {
            Integer ping = mySqlJdbcTemplate.queryForObject("SELECT 1", Integer.class);
            Integer authSessionRows = mySqlJdbcTemplate.queryForObject("SELECT COUNT(*) FROM AUTH_SESSION", Integer.class);
            return Health.up()
                    .withDetail("database", "mysql")
                    .withDetail("ping", ping)
                    .withDetail("authSessionRows", authSessionRows)
                    .build();
        } catch (Exception ex) {
            return Health.down(ex)
                    .withDetail("database", "mysql")
                    .build();
        }
    }
}
