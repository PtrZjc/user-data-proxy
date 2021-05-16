package pl.zajacp.proxy.domain.fetch;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajacp.proxy.domain.fetch.model.GithubUserData;
import pl.zajacp.proxy.model.UserDataResponse;

@Slf4j
@AllArgsConstructor
@Service
public class UserDataFacade {

    private final UserCountRepository repository;
    private final GithubClient client;
    private final UserDataMapper mapper;

    @Transactional
    public UserDataResponse fetchAndCountUser(String login) {
        log.info("Fetching user '{}' data", login);

        GithubUserData githubUser = client.fetchUserData(login);
        Integer userFetchCount = repository.incrementAndGetLoginCount(login);
        UserDataResponse user = mapper.fromGithubResponse(githubUser);
        user.setCalculations(calculateValue(githubUser));

        log.info("Successfully fetched user '{}' data with total fetch count: {}", login, userFetchCount);
        return user;
    }

    private Double calculateValue(GithubUserData githubUser) {
        return UserValueCalculator.calculateValue(githubUser.getFollowers(), githubUser.getPublicRepos());
    }
}
