package Project.SchoolWebApp.annotations;

import Project.SchoolWebApp.validations.CodeValidation;
import Project.SchoolWebApp.validations.ValidationType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeValidation.class)
public @interface ValidCode {

    ValidationType type();

    String message() default "invalid code";

   Class<?>[] groups() default{};
   Class<? extends Payload>[] payload() default{};
}
