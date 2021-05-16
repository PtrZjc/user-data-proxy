package pl.zajacp.proxy.domain.fetch

import io.vavr.control.Try
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.spock.Testcontainers
import pl.zajacp.proxy.infrastructure.db.ChangeLogConfiguration
import pl.zajacp.proxy.test.config.DatabaseTestConfiguration
import pl.zajacp.proxy.test.config.TestContainer
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

@Subject(UserCountRepository)
@Narrative("""
This test validates postgres function: increment_fetch_counter(login)
""")
@Testcontainers
@SpringBootTest(classes = [DatabaseTestConfiguration, ChangeLogConfiguration, UserCountRepositoryImpl])
@ContextConfiguration(initializers = TestContainer.TestContainersInitializer)
class UserCountRepositorySpec extends Specification {

    @Autowired
    UserCountRepository repository

    @Autowired
    JdbcTemplate jdbcTemplate

    def "Validate incrementing user count"() {
        when: "Function is invoked defined times per login"
        (1..fetchesNumber).each { repository.incrementAndGetLoginCount(loginToFetch) }

        then: "Proper count value is stored in db"
        doesLoginExistInDb(loginToFetch)
        getLoginFetchCount(loginToFetch) == fetchesNumber

        where:
        loginToFetch | fetchesNumber
        "bob_m"      | 5
        "mary98"     | 2
        "danelS"     | 1
    }

    private Integer getLoginFetchCount(String login) {
        def countQuery = "SELECT request_count FROM user_proxy.fetch_stats WHERE login = ?"
        return Try.of(() -> jdbcTemplate.queryForObject(countQuery, Integer.class, login))
                .getOrElseGet(x -> 0)
    }

    private boolean doesLoginExistInDb(String login) {
        def loginQuery = "SELECT login FROM user_proxy.fetch_stats WHERE login = ?"
        return Try.of(() -> jdbcTemplate.queryForObject(loginQuery, String.class, login))
                .map(s -> true)
                .getOrElseGet(x -> false);
    }
}
