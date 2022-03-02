package hexlet.code.app.config;

import hexlet.code.app.service.JWTAuthenticationFilter;
import hexlet.code.app.service.TokenAuthenticationProvider;
import hexlet.code.app.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static hexlet.code.app.controller.AuthController.LOGIN_PATH;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    // private JWTAuthenticationFilter jwtAuthenticationFilter;
    TokenAuthenticationProvider authenticationProvider;

    private RequestMatcher publicUrls;
    private RequestMatcher protectedUrls;

    public SecurityConfig(@Value("${base-url}") String baseUrl,
                          @Lazy final TokenAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;

        this.publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, POST.toString()),
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, GET.toString()),
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, PUT.toString()),
                new AntPathRequestMatcher(baseUrl + LOGIN_PATH, POST.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );

        this.protectedUrls = new NegatedRequestMatcher(publicUrls);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable(); // h2 console

        http
                .authorizeRequests()
                .requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                // .addFilterBefore(jwtAuthenticationFilter, JWTAuthenticationFilter.class)
                .sessionManagement().disable()
                .formLogin().disable()
                .logout().disable()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Override
    // public void configure(AuthenticationManagerBuilder builder) throws Exception {
    //     builder.userDetailsService(userDetailsService);
    // }

    @Bean
    public JWTAuthenticationFilter restAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(protectedUrls);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    private SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(((request, response, url) -> { }));
        return successHandler;
    }
}
