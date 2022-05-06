package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    @Operation(summary = "Get All Users")
    @ApiResponse(responseCode = "200", description = "List of all Users",
        content = @Content( mediaType = "application/json",
            schema = @Schema(implementation = User.class) )
    )
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by specified id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's data",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = User.class) )),
            @ApiResponse(responseCode = "404", description = "No User with such id")
    })
    public User getUser(
            @Parameter(description = "User's id", required = true)
            @PathVariable long id) {
        return userService.getUser(id);
    }

    @PostMapping("")
    @Operation(summary = "Create new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's data",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class) )),
            @ApiResponse(responseCode = "422", description = "Invalid data given")
    })
    public User createUser(
            @Parameter(description = "User's data", required = true)
            @RequestBody @Valid UserDto givenUser) {
        return userService.createUser(givenUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User by given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "No User with such id"),
            @ApiResponse(responseCode = "403", description = "Unauthorized request")
    })
    public void deleteUser(
            @Parameter(description = "User's id", required = true)
            @PathVariable long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = User.class) )),
            @ApiResponse(responseCode = "422", description = "Invalid data given"),
            @ApiResponse(responseCode = "404", description = "No User with such id"),
            @ApiResponse(responseCode = "403", description = "Unauthorized request")
    })
    public User updateUser(
            @Parameter(description = "User's data", required = true)
            @RequestBody @Valid UserDto user,
            @Parameter(description = "User's id", required = true)
            @PathVariable long id) {
        return userService.updateUser(id, user);
    }
}
