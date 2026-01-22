package Project.SchoolWebApp.dtos.professor_dtos;

import Project.SchoolWebApp.annotations.ValidEmail;
import Project.SchoolWebApp.annotations.ValidName;
import java.time.LocalDate;

public record ProfessorUpdateDTO (@ValidName String name,
                                  @ValidEmail String email,
                                  String password,
                                  LocalDate birthDate){
}
