package Project.SchoolWebApp.factory;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.models.Professor;
import Project.SchoolWebApp.models.Student;

/**
 * Creates an instance of a class
 */
public class UserFactory {

    /**
     * Creates an instance of the Student class
     * @param data
     * @return new Student()
     */
    public static Student create(StudentDTO data){
        return new Student();
    }

    public static Professor create(ProfessorDTO data){return new Professor();}
}
