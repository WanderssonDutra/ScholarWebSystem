package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidEmail;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should return false when the email is valid")
    void isValidEmailTestCase1() {

        StudentDTO studentDTO = new StudentDTO("Laraina", "laraina@gmail.com", "laraina123",
                LocalDate.parse("2001-05-19"), LocalDate.parse("2010-02-01"));

        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        boolean hasEmailViolation = false;

        for (ConstraintViolation<StudentDTO> v : violations)
            if (v.getConstraintDescriptor().getAnnotation() instanceof ValidEmail) {
                hasEmailViolation = true;
                break;
            }

        assertFalse(hasEmailViolation);
    }

    @Test
    @DisplayName("Should return true when the email is not valid")
    void isValidEmailTestCase2() {

        StudentDTO studentDTO = new StudentDTO("Laraina", "laraina@@gmail.com", "laraina123",
                LocalDate.parse("2001-05-19"), LocalDate.parse("2010-02-01"));

        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);

        boolean hasEmailViolation = false;

        for (ConstraintViolation<StudentDTO> v : violations)
            if (v.getConstraintDescriptor().getAnnotation() instanceof ValidEmail) {
                hasEmailViolation = true;
                break;
            }

        assertTrue(hasEmailViolation);
    }
}
