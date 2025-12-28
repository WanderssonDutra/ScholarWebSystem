package Project.SchoolWebApp.dtos.student_dtos;

import Project.SchoolWebApp.models.Student;
import java.time.LocalDate;

/**
 *  Record used to show basic information from student records
 * @param name
 * @param email
 * @param birthDate
 * @param yearOfEnrollment
 */
public record StudentBasicInfoDTO(String name, String email, LocalDate birthDate,
                                  LocalDate yearOfEnrollment) {

    public StudentBasicInfoDTO(Student student){

        this(student.getName(), student.getEmail(), student.getBirthDate(),student.getYearOfEnrollment());
    }
}
