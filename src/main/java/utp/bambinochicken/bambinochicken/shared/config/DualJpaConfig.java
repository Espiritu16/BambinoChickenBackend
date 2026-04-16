package utp.bambinochicken.bambinochicken.shared.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DualJpaConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "oracleDataSource")
    @Primary
    public DataSource oracleDataSource(@Qualifier("oracleDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("spring.mysql-datasource")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource(@Qualifier("mysqlDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "oracleEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
            @Qualifier("oracleDataSource") DataSource dataSource,
            @Value("${app.oracle.jpa.ddl-auto:update}") String ddlAuto
    ) {
        return entityManagerFactory(dataSource, "oracle-pu", "org.hibernate.dialect.OracleDialect", ddlAuto);
    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
            @Qualifier("mysqlDataSource") DataSource dataSource,
            @Value("${app.mysql.jpa.ddl-auto:update}") String ddlAuto
    ) {
        return entityManagerFactory(dataSource, "mysql-pu", "org.hibernate.dialect.MySQLDialect", ddlAuto);
    }

    @Bean(name = "oracleTransactionManager")
    @Primary
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    private LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            String persistenceUnit,
            String dialect,
            String ddlAuto
    ) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("utp.bambinochicken.bambinochicken.auth.persistence.entity");
        bean.setPersistenceUnitName(persistenceUnit);
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setJpaPropertyMap(jpaProperties(dialect, ddlAuto));
        return bean;
    }

    private Map<String, Object> jpaProperties(String dialect, String ddlAuto) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.show_sql", false);
        return properties;
    }

    @Configuration
    @EnableJpaRepositories(
            basePackages = "utp.bambinochicken.bambinochicken.auth.persistence.oracle",
            entityManagerFactoryRef = "oracleEntityManagerFactory",
            transactionManagerRef = "oracleTransactionManager"
    )
    static class OracleRepositories {
    }

    @Configuration
    @EnableJpaRepositories(
            basePackages = "utp.bambinochicken.bambinochicken.auth.persistence.mysql",
            entityManagerFactoryRef = "mysqlEntityManagerFactory",
            transactionManagerRef = "mysqlTransactionManager"
    )
    static class MySqlRepositories {
    }
}
