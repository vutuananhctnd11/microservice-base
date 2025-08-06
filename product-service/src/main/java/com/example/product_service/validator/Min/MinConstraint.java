package com.example.product_service.validator.Min;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinValidator.class)
public @interface MinConstraint {

    String message() default "INVALID_SIZE_MIN";

    String name();

    long min();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
