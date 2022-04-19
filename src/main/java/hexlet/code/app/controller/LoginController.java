package hexlet.code.app.controller;

import hexlet.code.app.dto.AuthDto;
import hexlet.code.app.service.TokenAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.app.controller.LoginController.LOGIN;

@RestController
@RequestMapping("${base-url}" + LOGIN)
@AllArgsConstructor
public class LoginController {

    public static final String LOGIN = "/login";

    private final TokenAuthenticationService authenticationService;

    @PostMapping
    public String login(@RequestBody AuthDto authDto) {
        return authenticationService.login(authDto.getEmail(), authDto.getPassword());
    }
}
