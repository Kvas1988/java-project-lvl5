package hexlet.code.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtTokenServiceImpl implements TokenService {

    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtTokenServiceImpl(UserDetailsService userDetailsService) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String getToken(Map<String, Object> attributes) {

        return Jwts.builder()
                .signWith(key)
                // .setClaims(attributes)
                .setSubject(attributes.get("username").toString())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .setIssuer("issuer")
                // .setIssuer(request.getRequestURL().toString())
                // .setClaims(roles)
                .compact();
    }

    @Override
    // public Map<String, Object> parse(String token) {
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = parse(token).getBody().getExpiration();
        return expiration.after(new Date());
    }

    public Boolean validateToken(String token) {
        String usernameFromToken = parse(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        Boolean isNameValid = usernameFromToken.equals(userDetails.getUsername());
        Boolean isTokenExpired = isTokenExpired(token);
        return isNameValid && isTokenExpired;
    }
}
