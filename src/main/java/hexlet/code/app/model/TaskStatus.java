package hexlet.code.app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
public class TaskStatus {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @NotBlank
    private String name;

    @CreationTimestamp
    private Date createdAt;
}
