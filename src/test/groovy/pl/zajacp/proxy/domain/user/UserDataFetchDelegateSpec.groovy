package pl.zajacp.proxy.domain.user

import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import pl.zajacp.proxy.api.UsersApiController
import pl.zajacp.proxy.domain.user.GithubClientImpl
import pl.zajacp.proxy.domain.user.UserCountRepositoryImpl
import pl.zajacp.proxy.infrastructure.AppConfig
import pl.zajacp.proxy.infrastructure.api.ApiConfig
import pl.zajacp.proxy.infrastructure.api.ExternalPathProperties
import pl.zajacp.proxy.infrastructure.db.ChangeLogConfiguration
import pl.zajacp.proxy.test.config.DatabaseTestConfiguration
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

import static org.hamcrest.Matchers.equalToIgnoringCase
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.zajacp.proxy.test.config.TestRepositoryConfig.TestRepository

@Subject(UserDataFetchDelegate)
@Narrative("""
Integration test of UserDataFetchDelegate.
It is based on defined wiremock stubs:
Logins "ptrzjc" and "octocat" return respose, "500" returns 500 response, all other return 404.

Detailed repository test is in UserCountRepositorySpec.
""")
@WebMvcTest(controllers = UsersApiController)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 9998, files = "file:src/test/resources/wiremock")
@Import([UserDataFetchDelegate,
        UserDataFacade,
        GithubClientImpl,
        UserCountRepositoryImpl,
        TestRepository,
        ExternalPathProperties,
        ApiConfig,
        AppConfig,
        UserDataMapperImpl,
        DatabaseTestConfiguration,
        ChangeLogConfiguration])
class UserDataFetchDelegateSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    TestRepository testRepository

    void setup() {
        testRepository.clearDatabase()
    }

    def "Test fetching existing user (login '#existingLogin')"() {
        when: "Request is made with existing login"
        def response = mockMvc.perform(get("/users/" + existingLogin))
                .andDo(print());

        then: "User data with 200 status is returned"
        response.andExpect(jsonPath("@.login", equalToIgnoringCase(existingLogin)))
                .andExpect(status().isOk())

        and: "Fetch was accounted in db"
        testRepository.getLoginFetchCount(existingLogin) == 1

        where:
        existingLogin << ["ptrzjc", "octocat"]
    }

    def "Test unsuccessful user fetch (#returnCode response)"() {
        when: "Unsuccessful request is made"
        def response = mockMvc.perform(get("/users/" + login))
                .andDo(print());

        then: "Response has proper code and contains its number in body"
        response.andExpect(jsonPath("@.code", Matchers.is(returnCode)))
                .andExpect(responseStatus)

        and: "Unsuccessful login is not accounted in db"
        testRepository.getLoginFetchCount(login) == 0

        where:
        login           || returnCode | responseStatus
        "I don't exist" || 404        | status().isNotFound()
        "500"           || 503        | status().isServiceUnavailable() //client throws 500

    }
}
