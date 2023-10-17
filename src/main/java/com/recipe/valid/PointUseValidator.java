package com.recipe.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// custom valid 포인트 0 은 통과하고 1000 아래면 유효성걸림
public class PointUseValidator implements ConstraintValidator<ValidPointUse, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == 0) {
            return true;
        }
        return value >= 1000;
    }
}
