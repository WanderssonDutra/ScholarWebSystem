package Project.SchoolWebApp.dtos.professor_dtos;

import Project.SchoolWebApp.models.Professor;
import java.time.LocalDate;

/**
 *  Record used to show basic information from professor records
 * @param name
 * @param email
 * @param birthDate
 */
public record ProfessorBasicInfoDTO(String name, String email, LocalDate birthDate) {

     public ProfessorBasicInfoDTO(Professor professor){

         this(professor.getName(), professor.getEmail(), professor.getBirthDate());
    }
}

