package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentUpdateDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.mappers.UserMap;
import Project.SchoolWebApp.models.Student;
import Project.SchoolWebApp.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    Student student;

    private StudentDTO studentDTO;

    private StudentUpdateDTO studentUpdateDTO;

    @Test
    @DisplayName("should get a list of students from DB")
    void allStudentsTest(){

        Student student1 = new Student("Aila",
                              "aila@gmail.com",
                              "aila123",
                              LocalDate.parse("2003-08-14"),
                              LocalDate.parse("2013-02-01"));
        student1.setCode("std232323");

        Student student2 = new Student("Belda",
                              "belda@gmail.com",
                              "belda123",
                              LocalDate.parse("2005-05-10"),
                              LocalDate.parse("2015-02-01"));
        student2.setCode("std242424");

        Mockito.when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        List<StudentBasicInfoDTO> response = studentService.allStudents();

        assertThat(response).hasSize(2);
        assertThat(response.get(1).name()).isEqualTo("Belda");

        Mockito.verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("should get a student from DB by searching the code")
    void SearchByCodeTestCase1(){

        Student student = new Student("Labubu",
                              "labubu@gmail.com",
                              "labubu123",
                              LocalDate.parse("2001-05-26"),
                              LocalDate.parse("2009-02-01"));
        student.setCode("std898989");

        Mockito.when(studentRepository.findById("std898989")).thenReturn(Optional.of(student));

        StudentBasicInfoDTO response = studentService.searchByCode("std898989");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Labubu");

        Mockito.verify(studentRepository).findById("std898989");
    }

    @Test
    @DisplayName("should throw DataNotFoundException when the code does not exist")
    void SearchByCodeTestCase2(){

        Mockito.when(studentRepository.findById("std898985")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, ()-> studentService.searchByCode("std898985"));

        Mockito.verify(studentRepository).findById("std898985");
    }

    @Test
    @DisplayName("Should get students from DB by searching the name")
    void searchByNameTestCase1(){

        Student student = new Student("Larissa",
                              "larissa@gmail.com",
                              "larissa123",
                              LocalDate.parse("2002-03-12"),
                              LocalDate.parse("2012-02-01"));
        student.setCode("std242424");

        Mockito.when(studentRepository.findByName("la")).thenReturn(List.of(student));

        List<StudentBasicInfoDTO> response = studentService.searchByName("la");

        assertThat(response).hasSize(1);
        assertThat(response.get(0).name()).isEqualTo("Larissa");

        Mockito.verify(studentRepository).findByName("la");
    }

    @Test
    @DisplayName("Should throw a DataNotFoundException if there is no student with this name")
    void searchByNameTestCase2(){

        Mockito.when(studentRepository.findByName("nan")).thenReturn(List.of());

        assertThrows(DataNotFoundException.class, ()-> studentService.searchByName("nan"));

        Mockito.verify(studentRepository).findByName("nan");
    }

    @Test
    @DisplayName("Should create a student into DB")
    void createTestCase1(){

        studentDTO = new StudentDTO("Laraina",
                                    "laraina@gmail.com",
                                    "laraina123",
                                    LocalDate.parse("2001-09-12"),
                                    LocalDate.parse("2011-02-01"));

        Student student = UserMap.toEntity(studentDTO, "std141414");
        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

        StudentBasicInfoDTO response = studentService.create(studentDTO);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("laraina@gmail.com");

        Mockito.verify(studentRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataConflictException by trying to register an existing email.")
    void createTestCase2(){

        studentDTO = new StudentDTO("Morenfa",
                                    "georla@gmail.com",
                                    "morenfa123",
                                    LocalDate.parse("2004-06-01"),
                                    LocalDate.parse("2014-02-01"));

        Mockito.when(studentRepository.existsByEmail(studentDTO.email())).thenReturn(true);

        assertThrows(DataConflictException.class,()-> studentService.create(studentDTO));

        Mockito.verify(studentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should update a student from DB")
    void updateTestCase1(){

        Student student = new Student("Laguana",
                              "laguana@gmail.com",
                              "laguana123",
                              LocalDate.parse("2005-11-23"),
                              LocalDate.parse("2012-02-01"));
        student.setCode("std171717");

        studentUpdateDTO = new StudentUpdateDTO(null,
                                                "laguana#@gmail.com",
                                                null,
                                                null,
                                                null);

        Mockito.when(studentRepository.findById("std171717")).thenReturn(Optional.of(student));
        Mockito.when(studentRepository.saveAndFlush(Mockito.any(Student.class))).thenReturn(student);

        StudentBasicInfoDTO response = studentService.update("std171717", studentUpdateDTO);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("laguana#@gmail.com");

        Mockito.verify(studentRepository).saveAndFlush(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when there is no student with " +
                 "the typed code to be updated")
    void updateTestCase2(){

        studentUpdateDTO = new StudentUpdateDTO(null,
                                                "giganoa223@gmail.com",
                                                "giganoa654",
                                                null,
                                                null);

        Mockito.when(studentRepository.findById("std858774")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,()-> studentService.update("std858774",
                                                                            studentUpdateDTO));

        Mockito.verify(studentRepository, Mockito.never()).saveAndFlush(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataConflictException when the student email to be updated " +
                 "is already the current email")
    void updateTestCase3(){

        Student student = new Student("Giganot",
                              "giganot@gmail.com",
                              "giganot123",
                              LocalDate.parse("2002-02-23"),
                              LocalDate.parse("2010-02-01"));
        student.setCode("std787878");

        studentUpdateDTO = new StudentUpdateDTO(null,
                                                "giganot@gmail.com",
                                                "giganot654",
                                                null,
                                                null);

        Mockito.when(studentRepository.findById("std787878")).thenReturn(Optional.of(student));

        RuntimeException ex = assertThrows(DataConflictException.class,
                                           ()-> studentService.update("std787878",
                                                                      studentUpdateDTO));
        assertThat(ex.getMessage()).isEqualTo("the account already uses this email.");

        Mockito.verify(studentRepository, Mockito.never()).saveAndFlush(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataConflictException when the email to be updated is already in use")
    void updateTestCase4(){

        Student student = new Student("Bigorne",
                              "bigorne@gmail.com",
                              "bigorne123",
                              LocalDate.parse("2001-08-16"),
                              LocalDate.parse("2010-02-01"));
        student.setCode("std020202");

        studentUpdateDTO = new StudentUpdateDTO(null,
                                                "belaud@gmail.com",
                                                "gigagigo654",
                                                null,
                                                null);

        Mockito.when(studentRepository.findById("std020202")).thenReturn(Optional.of(student));
        Mockito.when(studentRepository.existsByEmail(studentUpdateDTO.email())).thenReturn(true);

        RuntimeException ex = assertThrows(DataConflictException.class,
                                           ()-> studentService.update("std020202",
                                                                      studentUpdateDTO));
        assertThat(ex.getMessage()).isEqualTo("this email is already in use.");

        Mockito.verify(studentRepository, Mockito.never()).saveAndFlush(Mockito.any());
    }

    @Test
    @DisplayName("Should delete a student from DB")
    void deleteTestCase1(){

        Student student = new Student("Belaugin",
                              "belaugin@gmail.com",
                              "belaugin123",
                              LocalDate.parse("2003-05-23"),
                              LocalDate.parse("2013-02-01"));
        student.setCode("std454545");

        Mockito.when(studentRepository.findById("std454545")).thenReturn(Optional.of(student));
        Mockito.doNothing().when(studentRepository).delete(student);

        StudentBasicInfoDTO response = studentService.delete("std454545");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Belaugin");

        Mockito.verify(studentRepository).delete(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when there are no students with the code")
    void deleteTestCase2(){

        Mockito.when(studentRepository.findById("std282526")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, ()-> studentService.delete("std282526"));

        Mockito.verify(studentRepository, Mockito.never()).delete(Mockito.any());
    }
}
