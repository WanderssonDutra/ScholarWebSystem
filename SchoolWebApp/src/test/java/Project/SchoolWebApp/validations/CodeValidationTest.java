package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeValidationTest {

    public record TestCodeDTO(@ValidCode(type = ValidationType.STUDENT_CODE) String code){ }

    private TestCodeDTO testCodeDTO;

    private Validator validator;

    @BeforeEach
    void SetUp(){

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should return false when the valid code does not produce a ValidCode type" +
                 " Validation error")
    void isValidCodeTestCase1(){

        testCodeDTO = new TestCodeDTO("std545454");

        Set<ConstraintViolation<TestCodeDTO>> violations = validator.validate(testCodeDTO);

        boolean hasCodeViolation = false;


        for(ConstraintViolation<TestCodeDTO> violation: violations){

            if(violation.getConstraintDescriptor().getAnnotation() instanceof ValidCode) {
                hasCodeViolation = true;
                break;
            }
        }

        assertFalse(hasCodeViolation);
    }

    @Test
    @DisplayName("Should return true when the invalid code does produce a ValidCode type " +
                 "Validation error")
    void isValidCodeTestCase2(){

        testCodeDTO = new TestCodeDTO("stdlklck87");

        Set<ConstraintViolation<TestCodeDTO>> violations = validator.validate(testCodeDTO);

        boolean hasCodeViolation = false;

        for(ConstraintViolation<TestCodeDTO> violation: violations){

            if(violation.getConstraintDescriptor().getAnnotation() instanceof ValidCode) {
                hasCodeViolation = true;
                break;
            }
        }

        assertTrue(hasCodeViolation);
    }
}
