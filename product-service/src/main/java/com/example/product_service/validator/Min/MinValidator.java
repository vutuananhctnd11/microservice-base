package com.example.product_service.validator.Min;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinValidator implements ConstraintValidator<MinConstraint, Long> {

    long min;

    @Override
    public void initialize(MinConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null && value >= min;
    }
}
