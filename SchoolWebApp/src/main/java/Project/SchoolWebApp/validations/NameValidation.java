package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NameValidation implements ConstraintValidator<ValidName, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidName constraintValidation){
        this.pattern = Pattern.compile(constraintValidation.regex());

    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {

        if(name == null)
            return true;
        return pattern.matcher(name).matches();
    }
}
