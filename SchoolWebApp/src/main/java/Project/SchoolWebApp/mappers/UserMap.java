package Project.SchoolWebApp.mappers;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorUpdateDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentUpdateDTO;
import Project.SchoolWebApp.factory.UserFactory;
import Project.SchoolWebApp.models.Professor;
import Project.SchoolWebApp.models.Student;

/**
 * Class that passes data to dto's and entitys
 */
public class UserMap{

    /**
     * Transfer data from DTO to student
     * @param data A data transfer object
     * @param code Student id
     * @return Student object
     */
    public static Student toEntity(StudentDTO data, String code){

        Student student = UserFactory.create(data);

        if(data.name() != null)
            student.setName(data.name());
        if(data.email() != null)
            student.setEmail(data.email());
        if(data.password() != null)
            student.setPassword(data.password());
        if (data.birthDate() != null)
            student.setBirthDate(data.birthDate());
        if (data.yearOfEnrollment() != null)
            student.setYearOfEnrollment(data.yearOfEnrollment());
        student.setCode(code);

        return student;
    }

    /**
     * Transfer data from DTO to professor
     * @param data A data transfer object
     * @param code Professor id
     * @return Professor object
     */
    public static Professor toEntity(ProfessorDTO data, String code){

        Professor professor = UserFactory.create(data);

        if(data.name() != null)
            professor.setName(data.name());
        if(data.email() != null)
            professor.setEmail(data.email());
        if(data.password() != null)
            professor.setPassword(data.password());
        if (data.birthDate() != null)
            professor.setBirthDate(data.birthDate());
        professor.setCode(code);

        return professor;
    }

    /**
     * transfer data from DTO to Student
     * @param data A data transfer object
     * @param student A student object
     * @return The updated student object
     */
    public static Student updateEntity(StudentUpdateDTO data, Student student){

        if(data.name() != null)
            if(!data.name().isBlank())
                student.setName(data.name());
        if(data.email() != null)
            if(!data.email().isBlank())
                student.setEmail(data.email());
        if(data.password() != null)
            if(!data.password().isBlank())
                student.setPassword(data.password());
        if (data.birthDate() != null)
            student.setBirthDate(data.birthDate());
        if (data.yearOfEnrollment() != null)
            student.setYearOfEnrollment(data.yearOfEnrollment());

        return student;
    }

    /**
     * transfer the updated data from DTO to Professor
     * @param data DTO with data to update
     * @param professor the professor to be updated
     * @return The updated professor object
     */
    public static Professor updateEntity(ProfessorUpdateDTO data, Professor professor){

        if(data.name() != null)
            if(!data.name().isBlank())
                professor.setName(data.name());
        if(data.email() != null)
            if(!data.email().isBlank())
                professor.setEmail(data.email());
        if(data.password() != null)
            if(!data.password().isBlank())
                professor.setPassword(data.password());
        if (data.birthDate() != null)
            professor.setBirthDate(data.birthDate());

        return professor;
    }

    /**
     * Receives data from Student to a DTO
     * @param student Object from student class
     * @return A new StudentBasicInfoDTO instance
     */
    public static StudentBasicInfoDTO toDTO(Student student){

        return new StudentBasicInfoDTO(student);
    }

    /**
     * Receives data from Professor to a DTO
     * @param professor Object from the Professor class
     * @return a new ProfessorBasicInfoDTO instance
     */
    public static ProfessorBasicInfoDTO toDTO(Professor professor){

        return new ProfessorBasicInfoDTO(professor);
    }
}
