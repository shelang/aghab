package io.shelang.aghab.validation;

import io.shelang.aghab.validation.impl.ValidURIValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidURIValidator.class)
@Documented
public @interface ValidURI {

    String message() default "Invalid URL: scheme not allowed or format invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
