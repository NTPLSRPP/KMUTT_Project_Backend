package sit.syone.itbkkapi.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import sit.syone.itbkkapi.primarydatasource.repositories.StatusRepository;
import sit.syone.itbkkapi.validations.annotations.StatusExists;

public class StatusExistsValidator implements ConstraintValidator<StatusExists, Integer> {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if(integer == null){
            return false;
        }
        return statusRepository.existsById(integer);
    }
}
