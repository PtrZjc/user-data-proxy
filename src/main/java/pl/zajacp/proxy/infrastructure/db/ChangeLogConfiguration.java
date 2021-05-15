package pl.zajacp.proxy.infrastructure.db;

import java.sql.Connection;
import java.util.Collections;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class ChangeLogConfiguration {

    @Bean
    @SneakyThrows
    public SpringLiquibase getSpringLiquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseProperties.changeLog);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(liquibaseProperties.defaultSchema);
        liquibase.setLiquibaseSchema(liquibaseProperties.defaultSchema);
        try (Connection connection = dataSource.getConnection()) {
            liquibase.setChangeLogParameters(Collections.singletonMap("user_name", connection.getMetaData().getUserName()));
        }
        return liquibase;
    }

    @Component
    @EnableConfigurationProperties
    @ConfigurationProperties(prefix = "spring.liquibase")
    @Setter
    private class LiquibaseProperties {
        private String changeLog;
        private String defaultSchema;
    }
}