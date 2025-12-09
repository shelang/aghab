package io.shelang.aghab.validation.impl;

import io.shelang.aghab.util.UrlValidator;
import io.shelang.aghab.validation.ValidURI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidURIValidator implements ConstraintValidator<ValidURI, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) {
            return true; // Let @NotBlank handle null checks if needed
        }
        return UrlValidator.isSafeRedirectUrl(s);
    }
}
