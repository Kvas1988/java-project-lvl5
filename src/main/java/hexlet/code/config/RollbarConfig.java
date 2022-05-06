package hexlet.code.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration()
@EnableWebMvc
@ComponentScan({
        "hexlet.code.config",
        "com.rollbar.spring"
})
public class RollbarConfig {

    @Value("${ROLLBAR_TOKEN}")
    String token;

    @Value("${SPRING_PROFILES_ACTIVE:dev}")
    String env;

    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs(token));
    }

    private Config getRollbarConfigs(String accessToken) {

        // Reference ConfigBuilder.java for all the properties you can set for Rollbar
        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment(env)
                .build();
    }
}
