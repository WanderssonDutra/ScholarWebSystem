package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentUpdateDTO;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.factory.UserCodeFactory;
import Project.SchoolWebApp.mappers.UserMap;
import Project.SchoolWebApp.models.Student;
import Project.SchoolWebApp.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Class that applies business logic
 */
@RequiredArgsConstructor
@Service
public class StudentService {
    
    private final StudentRepository studentRepository;

    /**
     * lists all the students in the database
     * @return list of StudentBasicInfoDTO
     */
    public List<StudentBasicInfoDTO> allStudents (){

        List<StudentBasicInfoDTO> responseDTO = studentRepository.findAll().stream().
                                                   map(StudentBasicInfoDTO::new).toList();
        return responseDTO;
    }

    /**
     * Search a student by the code
     * @param code
     * @return A StudentBasicInfoDTO
     */
    public StudentBasicInfoDTO searchByCode(String code){

        StudentBasicInfoDTO responseDTO = studentRepository.findById(code)
                        .map(StudentBasicInfoDTO::new)
                        .orElseThrow(() -> new DataNotFoundException("There is no user with this code."));

        return responseDTO;
    }

    /**
     * Search a list of Student by name
     * @param name
     * @return A list of StudentBasicInfoDTO
     */
    public List<StudentBasicInfoDTO> searchByName(String name){

        List < StudentBasicInfoDTO > studentsDTO = studentRepository.findByName(name)
                                                                    .stream().map(StudentBasicInfoDTO::new)
                                                                    .toList();

        if(studentsDTO.isEmpty())
            throw new DataNotFoundException("There is no user with this name.");

        return studentsDTO;
    }

    /**
     * record a new student in the database
     * @param data
     * @return StudentBasicInfoDTO
     */
    public StudentBasicInfoDTO create(StudentDTO data){

        if(studentRepository.existsByEmail(data.email()))
            throw new DataConflictException("the email is already in use.");

        String code;
        while (true) {

            code = UserCodeFactory.generate("student");

            if (studentRepository.existsById(code))
                continue;
            break;
        }

        Student student = UserMap.toEntity(data, code);
        studentRepository.save(student);
        StudentBasicInfoDTO basicInfoDTO = UserMap.toDTO(student);

        return basicInfoDTO;
    }

    /**
     * Update a student in the database
     * @param code
     * @param data
     * @return a StudentBasicInfoDTO of the updated student
     */
    public StudentBasicInfoDTO update(String code, StudentUpdateDTO data){

        Student student = studentRepository.findById(code).orElseThrow(()-> new DataNotFoundException
                                                                    ("there is no student with this id"));

        if(student.getEmail().equals(data.email()))
            throw new DataConflictException("the account already uses this email.");

        if(studentRepository.existsByEmail(data.email()))
            throw new DataConflictException("this email is already in use.");

        student = UserMap.updateEntity(data, student);
        studentRepository.saveAndFlush(student);

        return UserMap.toDTO(student);
    }

    /***
     * Delete the student by informing id
     * @param code id used to find the student to be deleted
     * @return the basic information of the deleted student
     */
    public StudentBasicInfoDTO delete(String code){

        Student student = studentRepository.findById(code)
                                               .orElseThrow(()-> new DataNotFoundException
                                                       ("there is no user with this code"));
        StudentBasicInfoDTO response = UserMap.toDTO(student);

        studentRepository.delete(student);

        return response;
    }
}
