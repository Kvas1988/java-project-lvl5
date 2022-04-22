package hexlet.code.app.config;

import hexlet.code.app.security.CustomAuthenticationFilter;
import hexlet.code.app.security.CustomAuthorizationFilter;
import hexlet.code.app.security.JwtTokenServiceImpl;
import hexlet.code.app.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private RequestMatcher publicUrls;
    private RequestMatcher protectedUrls;
    private final JwtTokenServiceImpl tokenService;

    public SecurityConfig(@Value("${base-url}") String baseUrl,
                          JwtTokenServiceImpl tokenService) {

        this.publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, POST.toString()),
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, GET.toString()),
                new AntPathRequestMatcher(baseUrl + USER_CONTROLLER_PATH, PUT.toString()),
                new AntPathRequestMatcher(baseUrl + "/login", POST.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );

        this.protectedUrls = new NegatedRequestMatcher(publicUrls);
        this.tokenService = tokenService;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable(); // h2 console
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests()
                        .requestMatchers(publicUrls).permitAll()
                        .anyRequest().authenticated();

        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), tokenService)); // TODO: bean???
        http.addFilterAfter(new CustomAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);



        // http
        //         .authorizeRequests()
        //         .requestMatchers(publicUrls).permitAll()
        //         .anyRequest().authenticated()
        //         .and()
        //         .authenticationProvider(authenticationProvider)
        //         .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
        //         // .addFilterBefore(jwtAuthenticationFilter, JWTAuthenticationFilter.class)
        //         .sessionManagement().disable()
        //         .formLogin().disable()
        //         .logout().disable()
        // ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: cleanup

    // @Bean
    // public Key getKey() {
    //     return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // }


    // @Bean
    // public JWTAuthenticationFilter restAuthenticationFilter() throws Exception {
    //     JWTAuthenticationFilter filter = new JWTAuthenticationFilter(protectedUrls);
    //     filter.setAuthenticationManager(authenticationManager());
    //     filter.setAuthenticationSuccessHandler(successHandler());
    //     return filter;
    // }

    // private SimpleUrlAuthenticationSuccessHandler successHandler() {
    //     SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
    //     successHandler.setRedirectStrategy(((request, response, url) -> { }));
    //     return successHandler;
    // }
}
