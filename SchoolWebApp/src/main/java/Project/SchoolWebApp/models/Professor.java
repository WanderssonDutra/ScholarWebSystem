package Project.SchoolWebApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "professor")
@Table(name = "Professor")
@Getter
@Setter
@NoArgsConstructor
public class Professor extends User{

    public Professor(String name, String email, String password, LocalDate birthDate){
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }
}
