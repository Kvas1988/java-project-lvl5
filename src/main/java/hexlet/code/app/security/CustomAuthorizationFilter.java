package hexlet.code.app.security;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.config.JwtTokenServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenServiceImpl tokenService;

    public CustomAuthorizationFilter(JwtTokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/login")) { // TODO: check login path
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    // Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    // Jws<Claims> claimsJws = Jwts.parserBuilder()
                    //         .setSigningKey(key)
                    //         .build()
                    //         .parseClaimsJws(token);
                    Jws<Claims> claimsJws = tokenService.parse(token);

                    String username = claimsJws.getBody().getSubject();
                    // List<String> roles = claimsJws.getBody().get("roles",
                    //         new ArrayList<String>().getClass());

                    // TODO: verify with SecurityContextHolder
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                } catch (JwtException e) {
                    log.error("Logging error: " + e.getMessage());
                    e.printStackTrace();
                    // forbiden
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }

    }
}
