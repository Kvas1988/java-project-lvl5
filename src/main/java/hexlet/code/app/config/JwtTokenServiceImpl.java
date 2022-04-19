package hexlet.code.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtTokenServiceImpl implements TokenService {

    private final Key key;

    public JwtTokenServiceImpl() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
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
                // .setClaims(roles) // TODO: do I need to set claims with authority role
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
}
