package Project.SchoolWebApp.dtos.student_dtos;

import Project.SchoolWebApp.annotations.ValidEmail;
import Project.SchoolWebApp.annotations.ValidName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Record used to transfer data to the student entity
 * @param name
 * @param email
 * @param password
 * @param birthDate
 * @param yearOfEnrollment
 */
public record StudentDTO(@NotBlank @ValidName  String name,
                         @NotBlank @ValidEmail String email,
                         @NotBlank String password,
                         @NotNull LocalDate birthDate,
                         @NotNull LocalDate yearOfEnrollment) {


}
