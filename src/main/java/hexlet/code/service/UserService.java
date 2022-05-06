package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

public interface UserService {

    User createUser(UserDto givenUser);
    User updateUser(Long id, UserDto newData);
    void deleteUser(Long id);
    User getUser(Long id);
    Iterable<User> getAllUsers();
    User getCurrentUser(String authHeader);
}
