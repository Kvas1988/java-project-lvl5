package hexlet.code.app.service;

import hexlet.code.app.model.User;

import java.util.Optional;

interface AuthenticationService {
    String login(String email, String password);
    Optional<User> findByToken(String token);
}
