package Project.SchoolWebApp.dtos.professor_dtos;

import Project.SchoolWebApp.annotations.ValidEmail;
import Project.SchoolWebApp.annotations.ValidName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Record used to transfer data to the professor entity
 * @param name
 * @param email
 * @param password
 * @param birthDate
 */
public record ProfessorDTO(@ValidName @NotBlank String name,
                           @ValidEmail @NotBlank String email,
                           @NotBlank String password,
                           @NotNull LocalDate birthDate) {
}
