package hexlet.code.app.service;

import hexlet.code.app.model.User;

import java.util.Optional;

public interface AuthenticationService {
    public String login(String email, String password);
    public Optional<User> findByToken(String token);
}
