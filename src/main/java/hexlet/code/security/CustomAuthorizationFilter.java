package hexlet.code.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public CustomAuthorizationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null) {
                try {
                    String token = authorizationHeader;
                    if (authorizationHeader.startsWith("Bearer ")) {
                        token = authorizationHeader.substring("Bearer ".length());
                    }
                    Jws<Claims> claimsJws = tokenService.parse(token);

                    String username = claimsJws.getBody().getSubject();

                    // List<String> roles = claimsJws.getBody().get("roles",
                    //         new ArrayList<String>().getClass());

                    if (tokenService.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    log.error("Logging error: " + e.getMessage());
                    e.printStackTrace();
                    // forbiden
                    response.sendError(SC_UNAUTHORIZED);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }

    }
}
