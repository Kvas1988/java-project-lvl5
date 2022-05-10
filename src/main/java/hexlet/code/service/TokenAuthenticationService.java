package hexlet.code.service;

import hexlet.code.security.JwtTokenServiceImpl;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TokenAuthenticationService implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenServiceImpl tokenService;

    public TokenAuthenticationService(PasswordEncoder passwordEncoder,
                                      UserRepository userRepository,
                                      JwtTokenServiceImpl tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public String login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> "Bearer " + tokenService.getToken(Map.of("username", email)))
                .orElseThrow(() -> new NoSuchElementException("invalid login and/or password"));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByEmail(tokenService.parse(token).getBody().getSubject().toString());
    }
}
