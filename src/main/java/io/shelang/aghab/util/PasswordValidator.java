package io.shelang.aghab.util;

import java.util.regex.Pattern;

/**
 * Utility class for password validation.
 * Enforces password strength requirements.
 */
public final class PasswordValidator {

    private PasswordValidator() {
        throw new IllegalAccessError("Utility class");
    }

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 128;

    // At least one lowercase letter
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");

    // At least one uppercase letter
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

    // At least one digit
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");

    // At least one special character
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]");

    /**
     * Validates password strength.
     *
     * @param password The password to validate
     * @return ValidationResult containing success status and error message if any
     */
    public static ValidationResult validate(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }

        if (password.length() < MIN_LENGTH) {
            return new ValidationResult(false,
                    "Password must be at least " + MIN_LENGTH + " characters long");
        }

        if (password.length() > MAX_LENGTH) {
            return new ValidationResult(false,
                    "Password cannot exceed " + MAX_LENGTH + " characters");
        }

        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            return new ValidationResult(false,
                    "Password must contain at least one lowercase letter");
        }

        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            return new ValidationResult(false,
                    "Password must contain at least one uppercase letter");
        }

        if (!DIGIT_PATTERN.matcher(password).find()) {
            return new ValidationResult(false,
                    "Password must contain at least one digit");
        }

        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            return new ValidationResult(false,
                    "Password must contain at least one special character");
        }

        return new ValidationResult(true, null);
    }

    /**
     * Validates password and throws exception if invalid.
     *
     * @param password The password to validate
     * @throws IllegalArgumentException if password doesn't meet requirements
     */
    public static void validateOrThrow(String password) {
        ValidationResult result = validate(password);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

    /**
     * Result of password validation.
     */
    public record ValidationResult(boolean valid, String message) {
        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
