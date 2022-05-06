package hexlet.code.controller;

import hexlet.code.dto.AuthDto;
import hexlet.code.service.TokenAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.LoginController.LOGIN;

@RestController
@RequestMapping("${base-url}" + LOGIN)
@AllArgsConstructor
public class LoginController {

    public static final String LOGIN = "/login";

    private final TokenAuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Login request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "404", description = "Bad credentials")
    })
    public String login(
            @Parameter(description = "Email and password data")
            @RequestBody AuthDto authDto) {
        return authenticationService.login(authDto.getEmail(), authDto.getPassword());
    }
}
