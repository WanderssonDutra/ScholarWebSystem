package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidName;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class NameValidationTest {


    private Validator validator;

    @BeforeEach
    void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should return false when the name is valid")
    void isValidNameTestCase1() {

        StudentDTO studentDTO = new StudentDTO("Ana Paula de Cunha", "anapaula@gmail.com",
                "anapaula123", LocalDate.parse("2002-06-14"),
                LocalDate.parse("2010-02-01"));

        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        boolean hasValidNameViolation = false;

        for (ConstraintViolation<StudentDTO> v : violations) {
            if (v.getConstraintDescriptor().getAnnotation() instanceof ValidName) {
                hasValidNameViolation = true;
                break;
            }
        }

        assertFalse(hasValidNameViolation);
    }

    @Test
    @DisplayName("Should return true when the name is not valid")
    void isValidNameTestCase2() {

        StudentDTO studentDTO = new StudentDTO("4na Paula de Sousa", "anapaula@gmail.com",
                "anapaula123", LocalDate.parse("2002-06-14"),
                LocalDate.parse("2010-02-01"));

        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        boolean hasValidNameViolation = false;

        for (ConstraintViolation<StudentDTO> v : violations)
            if (v.getConstraintDescriptor().getAnnotation() instanceof ValidName) {
                hasValidNameViolation = true;
                break;
            }


        assertTrue(hasValidNameViolation);
    }
}
