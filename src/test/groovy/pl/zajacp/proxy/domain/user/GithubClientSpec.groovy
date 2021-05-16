package pl.zajacp.proxy.domain.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import pl.zajacp.proxy.infrastructure.AppConfig
import pl.zajacp.proxy.infrastructure.api.ApiConfig
import pl.zajacp.proxy.infrastructure.api.ExternalPathProperties
import pl.zajacp.proxy.test.config.WiremockConfig
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(GithubClient)
@Narrative("""
This test check proper response mapping of Github client.
It is based on defined stubs located under 'wiremock.base-folder' property.
Logins "ptrzjc" and "octocat" return respose, all other return 404.

Error messages are handled separately - via RestControllerAdvice
""")
@AutoConfigureWireMock(port = 9998)
@SpringBootTest(classes = [GithubClientImpl, WiremockConfig, ApiConfig, ExternalPathProperties, AppConfig])
class GithubClientSpec extends Specification {

    @Autowired
    GithubClient client

    @Unroll
    def "Login #login - check user data fetch"() {
        when: "User data is fetched"
        def response = client.fetchUserData(login)

        then: "All fields are mapped correctly"
        with(response) {
            it.login.equalsIgnoreCase(login)
            it.id == id
            it.name == name
            it.type == type
            it.createdAt.toString() == createdAt
            it.followers == followers
            it.publicRepos == publicRepos
            it.avatarUrl.contains("https://avatars.githubusercontent.com/")
        }

        where:
        login     || id       | name          | type   | createdAt              | followers | publicRepos
        "ptrzjc"  || 41077907 | "Piotr"       | "User" | "2018-07-10T17:01:14Z" | 3         | 11
        "octocat" || 583231   | "The Octocat" | "User" | "2011-01-25T18:44:36Z" | 3735      | 8
    }

    def "Check data fetch of non-existing user"() {
        when: "Non existing user data is fetched"
        client.fetchUserData("Not existing login")

        then: "All fields are mapped correctly"
        thrown(HttpClientErrorException.NotFound)
    }

    def "Check internal server error response"() {
        when: "Non existing user data is fetched"
        client.fetchUserData("500")

        then: "All fields are mapped correctly"
        thrown(HttpServerErrorException.InternalServerError)
    }
}