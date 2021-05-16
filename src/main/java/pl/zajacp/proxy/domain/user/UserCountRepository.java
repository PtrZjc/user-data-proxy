package pl.zajacp.proxy.domain.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

interface UserCountRepository {
    Integer incrementAndGetLoginCount(String login);
}

@Slf4j
@Repository
@AllArgsConstructor
class UserCountRepositoryImpl implements UserCountRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String CALL_INCREMENT_PROCEDURE = "SELECT increment_fetch_counter(?)";

    @Override
    public Integer incrementAndGetLoginCount(String login) {
        log.info("Incrementing fetch count for user {}", login);
        return jdbcTemplate.queryForObject(CALL_INCREMENT_PROCEDURE, Integer.class, login);
    }
}

