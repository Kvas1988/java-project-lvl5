package hexlet.code.app.service;

import java.util.Map;

public interface TokenService {
    String getToken(Map<String, Object> attribs);
    Map<String, Object> parse(String token);
}
