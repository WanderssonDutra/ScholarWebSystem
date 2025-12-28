package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidation implements ConstraintValidator<ValidEmail, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidEmail constraintValidation){

        this.pattern = Pattern.compile(constraintValidation.regex());
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){

        return pattern.matcher(email).matches();
    }
}
