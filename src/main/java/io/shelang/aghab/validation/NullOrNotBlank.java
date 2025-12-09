package io.shelang.aghab.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.shelang.aghab.validation.impl.NullOrNotBlankValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "{io.shelang.aghab.validation.NullOrNotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
