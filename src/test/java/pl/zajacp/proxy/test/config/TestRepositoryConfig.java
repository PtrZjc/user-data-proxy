package pl.zajacp.proxy.test.config;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class TestRepositoryConfig {

    @Bean
    public TestRepository testRepository(JdbcTemplate jdbcTemplate) {
        return new TestRepository(jdbcTemplate);
    }

    @AllArgsConstructor
    public static class TestRepository {

        private final JdbcTemplate jdbcTemplate;
        private final String countQuery = "SELECT request_count FROM user_proxy.fetch_stats WHERE login = ?";
        private final String loginQuery = "SELECT login FROM user_proxy.fetch_stats WHERE login = ?";
        private final String truncate = "TRUNCATE user_proxy.fetch_stats";

        public Integer getLoginFetchCount(String login) {
            return Try.of(() -> jdbcTemplate.queryForObject(countQuery, Integer.class, login))
                    .getOrElseGet(x -> 0);
        }

        public boolean doesLoginExistInDb(String login) {
            return Try.of(() -> jdbcTemplate.queryForObject(loginQuery, String.class, login))
                    .map(s -> true)
                    .getOrElseGet(x -> false);
        }

        public void clearDatabase(){
            jdbcTemplate.execute(truncate);
        }
    }
}
