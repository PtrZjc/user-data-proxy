package pl.zajacp.proxy.test.config;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class DatabaseTestConfiguration {

    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        var jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE SCHEMA user_proxy");
        return jdbcTemplate;
    }

    @Primary
    @Bean
    public DataSource testContainerDataSource() {
        return DataSourceBuilder.create()
                .url(TestContainer.getInstance().getJdbcUrl())
                .password(TestContainer.getInstance().getPassword())
                .username(TestContainer.getInstance().getUsername())
                .build();
    }
}