package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User createUser(UserDto givenUser);
    User updateUser(Long id, UserDto newData);
    ResponseEntity<String> deleteUser(String authHeader, Long id);
    User getUser(Long id);
    Iterable<User> getAllUsers();
    User getCurrentUser(String authHeader);
}
