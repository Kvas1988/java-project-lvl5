package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;

public interface UserService {

    User createUser(UserDto givenUser);
    User updateUser(Long id, UserDto newData);
    void deleteUser(Long id);
    User getUser(Long id);
    Iterable<User> getAllUsers();
    User getCurrentUser(String authHeader);
}
