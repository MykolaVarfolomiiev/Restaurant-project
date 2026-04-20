package nbcc.resto.config;

import nbcc.auth.config.JWTConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JWTEnvironmentConfig implements JWTConfig {

    private final Environment environment;

    public JWTEnvironmentConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public long getTimeout() {
        Long timeout = environment.getProperty("jwt.timeout", Long.class);
        if (timeout == null) {
            return 60 * 60; // 1- hour default
        }

        return timeout;
    }

    @Override
    public String getIssuer() {
        return environment.getProperty("jwt.issuer");
    }
}
