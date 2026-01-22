package Project.SchoolWebApp.annotations;

import Project.SchoolWebApp.validations.NameValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidation.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String regex() default "^[\\p{L}]+(?:[ '-][\\p{L}]+)*$";
    String message() default "The name must not have numbers or special characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
