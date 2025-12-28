package Project.SchoolWebApp.repositories;

import Project.SchoolWebApp.models.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Mock
    Student student;

    @Test
    @DisplayName("Should get a student from DB by searching it's name")
    void findByNameCase1() {

        student = new Student();
        student.setName("Alexandra");
        student.setEmail("alexandra@gmail.com");
        student.setPassword("alexandraAGrande");
        student.setBirthDate(LocalDate.of(2004, 06, 11));
        student.setYearOfEnrollment(LocalDate.of(2014, 02, 01));
        student.setCode("std999999");

        studentRepository.save(student);

        student.setName("Cleopatra");
        student.setEmail("cleopatra@gmail.com");
        student.setPassword("cleopatra123");
        student.setBirthDate(LocalDate.of(2002, 05, 15));
        student.setYearOfEnrollment(LocalDate.of(2012, 02, 01));
        student.setCode("std999992");

        studentRepository.save(student);

        List<Student> result = studentRepository.findByName("ra");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should not get a student from DB by searching a non existing name")
    void findByNameTestCase2(){

        student = new Student();

        student.setName("Leonardico");
        student.setEmail("leonardico@gmail.com");
        student.setPassword("leonardico123");
        student.setBirthDate(LocalDate.of(2005, 07, 25));
        student.setYearOfEnrollment(LocalDate.of(2015, 02, 01));
        student.setCode("std525252");

        studentRepository.save(student);

        var result = studentRepository.findByName("Lucas");
        assertThat(result).isEmpty();
    }
}