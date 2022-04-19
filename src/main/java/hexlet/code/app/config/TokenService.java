package hexlet.code.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Map;

public interface TokenService {

    String getToken(Map<String, Object> attributes);
    // Map<String, Object> parse(String token);
    Jws<Claims> parse(String token);
}
