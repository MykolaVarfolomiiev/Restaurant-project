package nbcc.resto.service;

import nbcc.auth.domain.BearerToken;
import nbcc.auth.domain.LoginRequest;
import nbcc.auth.domain.UserResponse;
import nbcc.auth.result.AuthResult;
import nbcc.auth.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public UserRepositoryAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";

        AuthResult<UserResponse> result = userService.isAuthorized(new LoginRequest(username, password));

        if (result.isError() || !result.isSuccessful()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserResponse user = result.getValue();
        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (user.isLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User account is not enabled");
        }

        BearerToken bearerToken = new BearerToken("session", user.getUsername(), 86400, null);
        return new UsernamePasswordAuthenticationToken(bearerToken, null, List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
