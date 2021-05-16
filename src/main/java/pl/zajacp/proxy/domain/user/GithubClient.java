package pl.zajacp.proxy.domain.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.zajacp.proxy.domain.user.model.GithubUserData;
import pl.zajacp.proxy.infrastructure.api.ExternalPathProperties;


interface GithubClient {
    GithubUserData fetchUserData(String login);
}

@Slf4j
@Service
@AllArgsConstructor
class GithubClientImpl implements GithubClient {

    private final RestTemplate restTemplate;
    private final ExternalPathProperties externalPath;

    @Override
    public GithubUserData fetchUserData(String login) {
        log.info("Fetching user '{}' data", login);
        return restTemplate.getForObject(getUserResourceUrl(login), GithubUserData.class);
    }

    private String getUserResourceUrl(String login){
        return String.format("/%s/%s", externalPath.getUsersResource(), login);
    }
}
