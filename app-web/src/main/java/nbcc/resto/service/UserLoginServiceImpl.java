package nbcc.resto.service;

import nbcc.auth.domain.BearerToken;
//import nbcc.auth.service.LoginService;
import nbcc.common.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements LoginService {

    public UserLoginServiceImpl() {
    }

    @Override
    public boolean isLoggedIn() {
       return getBearerToken() != null;
    }

    @Override
    public boolean isLoggedOut() {
        return !isLoggedIn();
    }

    @Override
    public String getCurrentUsername() {
        var token = getBearerToken();
        return token != null ? token.getUserName() : null;
    }

    private BearerToken getBearerToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof BearerToken bearerToken) {
            return bearerToken;
        } else {
            return null;
        }
    }
}
