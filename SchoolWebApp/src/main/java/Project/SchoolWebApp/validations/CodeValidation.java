package Project.SchoolWebApp.validations;

import Project.SchoolWebApp.annotations.ValidCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CodeValidation implements ConstraintValidator<ValidCode, String> {

    private ValidationType type;
    @Override
    public void initialize(ValidCode annotation){

        this.type = annotation.type();

    }
    @Override
    public boolean isValid(String code, ConstraintValidatorContext context){

        if(code.length() != 9) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The must have exactly 9 characters")
                   .addConstraintViolation();
            return false;

        }
        if(type == ValidationType.STUDENT_CODE) {

            if (!(code.startsWith("std") && code.substring(3).matches("^[0-9]+$"))) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("the code must begin with the letters:" +
                       " \"std\" following a sequence of six numbers.")
                       .addConstraintViolation();
                return false;
            }
        }
        if(type == ValidationType.PROFESSOR_CODE) {

            if (!(code.startsWith("pfr") && code.substring(3).matches("^[0-9]+$"))){

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("the code must begin with" +
                       " the letters:\"pfr\" following a sequence of six numbers.")
                       .addConstraintViolation();
                return false;

            }
        }

        return true;
    }
}
