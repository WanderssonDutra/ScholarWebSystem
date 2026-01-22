package Project.SchoolWebApp.dtos.student_dtos;

import Project.SchoolWebApp.annotations.ValidEmail;
import Project.SchoolWebApp.annotations.ValidName;
import java.time.LocalDate;

public record StudentUpdateDTO(@ValidName String name,
                               @ValidEmail String email,
                               String password,
                               LocalDate birthDate,
                               LocalDate yearOfEnrollment) {
}
