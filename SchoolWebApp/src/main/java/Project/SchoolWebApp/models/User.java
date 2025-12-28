package Project.SchoolWebApp.models;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.IdGeneratorType;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class User{

    @Id
    @Column(unique = true)
    protected String code;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected String email;

    @Column(nullable = false)
    protected LocalDate birthDate;


}