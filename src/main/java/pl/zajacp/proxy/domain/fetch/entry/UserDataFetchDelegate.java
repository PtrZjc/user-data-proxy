package pl.zajacp.proxy.domain.fetch.entry;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.zajacp.proxy.api.UsersApiDelegate;
import pl.zajacp.proxy.domain.fetch.UserDataFacade;
import pl.zajacp.proxy.model.UserDataResponse;

@AllArgsConstructor
@Service
public class UserDataFetchDelegate implements UsersApiDelegate {

    private final UserDataFacade facade;

    @Override
    public ResponseEntity<UserDataResponse> fetchUserData(String login) {
        return ResponseEntity.ok(facade.fetchAndCountUser(login));
    }
}
