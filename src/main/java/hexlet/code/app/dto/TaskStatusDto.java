package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TaskStatusDto {

    @NotBlank
    private String name;
}
