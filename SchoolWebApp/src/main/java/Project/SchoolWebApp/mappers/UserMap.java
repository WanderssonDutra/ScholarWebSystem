package Project.SchoolWebApp.mappers;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.factory.UserFactory;
import Project.SchoolWebApp.models.Professor;
import Project.SchoolWebApp.models.Student;

/**
 * Class that passes data to dto's and entitys
 */
public class UserMap{

    /**
     * Passes data from dto to entity
     * @param data A data transfer object
     * @param code Id to be added to a Student id property
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
     * passes data from dto to entity
     * @param data A data transfer object
     * @param code Id to be added to a Professor id property
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
     * transfer the data from the Data Transfer Operation to a student Entity
     * @param data A data transfer object
     * @param student A student object
     * @return a student entity with the updated data
     */
    public static Student updateEntity(StudentDTO data, Student student){

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

        return student;
    }

    /**
     * Passes data from entity to dto
     * @param student Object from the Student class
     * @return A new StudentBasicInfoDTO instance
     */
    public static StudentBasicInfoDTO toDTO(Student student){

        return new StudentBasicInfoDTO(student);
    }

    /**
     * Passes data from entity to dto
     * @param professor Object from the Professor class
     * @return a new ProfessorBasicInfoDTO instance
     */
    public static ProfessorBasicInfoDTO toDTO(Professor professor){

        return new ProfessorBasicInfoDTO(professor);
    }
}
