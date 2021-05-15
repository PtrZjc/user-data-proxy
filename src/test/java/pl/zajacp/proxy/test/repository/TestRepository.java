package pl.zajacp.proxy.test.repository;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
public class TestRepository {

    private final JdbcTemplate jdbcTemplate;

    final String COUNT_QUERY = "SELECT request_count FROM fetch_stats WHERE login = ?";
    final String LOGIN_QUERY = "SELECT login FROM fetch_stats WHERE login = ?";


    Integer getLoginFetchCount(String login) {
        return jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);
    }

    boolean doesLoginExist(String login) {
        return Try.of(() -> jdbcTemplate.queryForObject(LOGIN_QUERY, String.class))
                .map(s -> true)
                .getOrElseGet(x -> false);
    }
}
