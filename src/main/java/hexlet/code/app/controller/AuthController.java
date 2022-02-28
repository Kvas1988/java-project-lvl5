package hexlet.code.app.controller;

import hexlet.code.app.dto.AuthDto;
import hexlet.code.app.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + "/login")
public class AuthController {

    private AuthenticationService authenticationService;
    public static final String LOGIN_PATH = "/login";


    @PostMapping
    public String login(@RequestBody AuthDto authDto) {
        return authenticationService.login(authDto.getEmail(), authDto.getPassword());
    }
}
