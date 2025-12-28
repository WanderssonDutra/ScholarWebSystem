package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.models.Student;
import Project.SchoolWebApp.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Mock
    Student student;

    @Mock
    StudentDTO studentDTO;

    @Test
    @DisplayName("should return a list of students from DB")
    void allStudentsTest(){

        student = new Student("Aila", "aila@gmail.com", "aila123",
                              LocalDate.parse("2003-08-14"), LocalDate.parse("2013-02-01"));
        student.setCode("std232323");

        studentRepository.save(student);

        student = new Student("Belda", "belda@gmail.com", "belda123",
                              LocalDate.parse("2005-05-10"), LocalDate.parse("2015-02-01"));
        student.setCode("std242424");

        studentRepository.save(student);

        var result = studentService.allStudents();

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("should return a student with the specified code.")
    void SearchByCodeTestCase1(){

        student = new Student("Labubu", "labubu@gmail.com", "labubu123",
                              LocalDate.parse("2001-05-26"), LocalDate.parse("2009-02-01"));
        student.setCode("std898989");

        studentRepository.save(student);

        StudentBasicInfoDTO response = studentService.searchByCode("std898989");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Labubu");
    }

    @Test
    @DisplayName("should throw DataNotFoundException when the code does not exist")
    void SearchByCodeTestCase2()throws DataNotFoundException {

        student = new Student("Lorico", "lorico@gmail.com", "lorico123",
                              LocalDate.parse("2001-05-26"), LocalDate.parse("2009-02-01"));
        student.setCode("std898989");

        studentRepository.save(student);

        assertThrows(DataNotFoundException.class, ()-> studentService.searchByCode("std898985"),
                "There is no user with this code.");
    }

    @Test
    @DisplayName("should return a error message when the code doesn't follow the logic")
    void searchByCodeTestCase3()throws BadRequestException{

        assertThrows(BadRequestException.class, ()-> studentService.searchByCode("stdbdjsksk"),
                "the code must begin with the letters: \"std\" " +
                        "following a sequence of six numbers.");
    }

    @Test
    @DisplayName("Should get students from DB by searching their names")
    void searchByNameTestCase1(){

        student = new Student("Larissa", "larissa@gmail.com", "larissa123",
                              LocalDate.parse("2002-03-12"), LocalDate.parse("2012-02-01"));
        student.setCode("std242424");

        studentRepository.save(student);

        var result = studentService.searchByName("la");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should throw a DataNotFoundException if there is no student with the specified name")
    void searchByNameTestCase2()throws DataNotFoundException{

        student = new Student("Aila", "aila@gmail.com", "aila123",
                              LocalDate.parse("2005-05-05"), LocalDate.parse("2015-02-01"));
        student.setCode("std212121");

        studentRepository.save(student);

        assertThrows(DataNotFoundException.class, ()-> studentService.searchByName("nan"),
                    "There is no user with this name.");

    }

    @Test
    @DisplayName("Should persist a student in DB and return it´s information through DTO")
    void createTestCase1(){

        studentDTO = new StudentDTO("Laraina", "laraina@gmail.com", "laraina123",
                              LocalDate.parse("2001-09-12"), LocalDate.parse("2011-02-01"));

        assertThat(studentService.create(studentDTO)).isNotNull();
    }

    @Test
    @DisplayName("Should throw DataConflictException by trying registering an existing email.")
    void createTestCase2()throws DataConflictException{

        student = new Student("Georla", "georla@gmail.com", "georla123",
                                    LocalDate.parse("2003-02-23"),
                                    LocalDate.parse("2013-02-01"));
        student.setCode("std232323");

        studentRepository.save(student);

        studentDTO = new StudentDTO("Morenfa", "georla@gmail.com", "morenfa123",
                                    LocalDate.parse("2004-06-01"),
                                    LocalDate.parse("2014-02-01"));

        assertThrows(DataConflictException.class,()-> studentService.create(studentDTO),
                     "the email is already in use.");
    }

}
