package pl.zajacp.proxy.domain.fetch;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

public interface UserCountRepository {
    Integer incrementAndGetLoginCount(String login);
}

@Repository
@AllArgsConstructor
class UserCountRepositoryImpl implements UserCountRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String CALL_INCREMENT_PROCEDURE = "SELECT increment_fetch_counter(?)";

    @Override
    public Integer incrementAndGetLoginCount(String login) {
        return jdbcTemplate.queryForObject(CALL_INCREMENT_PROCEDURE, Integer.class);
    }
}

