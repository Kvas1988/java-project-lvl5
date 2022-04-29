package hexlet.code.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Map;

public interface TokenService {

    String getToken(Map<String, Object> attributes);
    // Map<String, Object> parse(String token);
    Jws<Claims> parse(String token);
    Boolean validateToken(String token);
}
