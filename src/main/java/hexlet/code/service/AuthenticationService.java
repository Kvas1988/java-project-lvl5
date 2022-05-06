package hexlet.code.service;

import hexlet.code.model.User;

import java.util.Optional;

interface AuthenticationService {
    String login(String email, String password);
    Optional<User> findByToken(String token);
}
