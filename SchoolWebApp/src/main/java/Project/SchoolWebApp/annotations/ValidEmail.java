package Project.SchoolWebApp.annotations;


import Project.SchoolWebApp.validations.EmailValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidation.class)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String regex() default "^[^\\s\\p{So}@]+@[Gg][Mm][Aa][Ii][Ll]\\.[Cc][Oo][Mm]$";
    String message() default "The email must contain at the end: @gmail.com";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
