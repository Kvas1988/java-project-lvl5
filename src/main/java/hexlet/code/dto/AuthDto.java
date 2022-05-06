package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;
}
