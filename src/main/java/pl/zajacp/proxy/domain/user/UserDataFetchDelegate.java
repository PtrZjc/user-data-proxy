package pl.zajacp.proxy.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.zajacp.proxy.api.UsersApiDelegate;
import pl.zajacp.proxy.model.UserDataResponse;

@AllArgsConstructor
@Service
class UserDataFetchDelegate implements UsersApiDelegate {

    private final UserDataFacade facade;

    @Override
    public ResponseEntity<UserDataResponse> fetchUserData(String login) {
        return ResponseEntity.ok(facade.fetchAndCountUser(login));
    }
}
