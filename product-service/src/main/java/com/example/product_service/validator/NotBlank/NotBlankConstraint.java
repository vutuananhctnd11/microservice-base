package com.example.product_service.validator.NotBlank;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankValidator.class)
public @interface NotBlankConstraint {

    String message() default "FIELD_NOT_BLANK";

    String name();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
