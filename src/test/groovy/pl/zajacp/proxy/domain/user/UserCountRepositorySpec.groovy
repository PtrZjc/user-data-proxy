package pl.zajacp.proxy.domain.user

import io.vavr.control.Try
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.spock.Testcontainers
import pl.zajacp.proxy.infrastructure.db.ChangeLogConfiguration
import pl.zajacp.proxy.test.config.DatabaseTestConfiguration
import pl.zajacp.proxy.test.config.TestContainer
import pl.zajacp.proxy.test.config.TestRepositoryConfig
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

import static pl.zajacp.proxy.test.config.TestRepositoryConfig.*
import static pl.zajacp.proxy.test.config.TestRepositoryConfig.*

@Subject(UserCountRepository)
@Narrative("""
This test validates postgres function: increment_fetch_counter(login)
""")
@Testcontainers
@SpringBootTest(classes = [
        DatabaseTestConfiguration,
        TestRepositoryConfig,
        ChangeLogConfiguration,
        UserCountRepositoryImpl])
@ContextConfiguration(initializers = TestContainer.TestContainersInitializer)
class UserCountRepositorySpec extends Specification {

    @Autowired
    UserCountRepository repository

    @Autowired
    TestRepository testRepository

    def "Validate incrementing user count"() {
        when: "Function is invoked defined times per login"
        (1..fetchesNumber).each { repository.incrementAndGetLoginCount(loginToFetch) }

        then: "Proper count value is stored in db"
        testRepository.doesLoginExistInDb(loginToFetch)
        testRepository.getLoginFetchCount(loginToFetch) == fetchesNumber

        where:
        loginToFetch | fetchesNumber
        "bob_m"      | 5
        "mary98"     | 2
        "danelS"     | 1
    }
}
