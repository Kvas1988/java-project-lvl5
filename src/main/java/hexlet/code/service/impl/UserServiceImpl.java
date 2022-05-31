package hexlet.code.service.impl;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.TokenService;
import hexlet.code.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenService tokenService;

    @Override
    public User createUser(UserDto givenUser) {
        if (userRepository.findByEmail(givenUser.getEmail()).isPresent()) {
            throw new DuplicateKeyException("User with such email already exists");
        }

        User user = new User();
        user.setEmail(givenUser.getEmail());
        user.setFirstName(givenUser.getFirstName());
        user.setLastName(givenUser.getLastName());
        user.setPassword(encoder.encode(givenUser.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserDto newData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
        user.setEmail(newData.getEmail());
        user.setFirstName(newData.getFirstName());
        user.setLastName(newData.getLastName());
        user.setPassword(encoder.encode(newData.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<String> deleteUser(String authHeader, Long id) {
        User currentUser = getCurrentUser(authHeader);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        if (currentUser.getId() != existingUser.getId()) {
            log.error("User not allowed to delete others accounts");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
        // TODO: write tests for it
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no such user"));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getCurrentUser(String authHeader) {
        if (authHeader == null || authHeader.equals("")) {
            throw new JwtException("invalid token");
        }
        String token = authHeader;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring("Bearer ".length());
        }
        String email = tokenService.parse(token).getBody().getSubject();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No such user"));

    }
}
