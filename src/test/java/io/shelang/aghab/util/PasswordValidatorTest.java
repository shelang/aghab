package io.shelang.aghab.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void testValidate_ValidPasswords() {
        assertTrue(PasswordValidator.validate("StrongPass1!").isValid());
        assertTrue(PasswordValidator.validate("AnotherS3cret@").isValid());
    }

    @Test
    void testValidate_LengthRequirements() {
        // Too short
        assertFalse(PasswordValidator.validate("Short1!").isValid());
        // Empty
        assertFalse(PasswordValidator.validate("").isValid());
        // Null
        assertFalse(PasswordValidator.validate(null).isValid());
    }

    @Test
    void testValidate_ComplexityRequirements() {
        // No uppercase
        assertFalse(PasswordValidator.validate("weakpass1!").isValid());
        // No lowercase
        assertFalse(PasswordValidator.validate("WEAKPASS1!").isValid());
        // No digit
        assertFalse(PasswordValidator.validate("WeakPass!").isValid());
        // No special char
        assertFalse(PasswordValidator.validate("WeakPass123").isValid());
    }

    @Test
    void testValidateOrThrow() {
        assertDoesNotThrow(() -> PasswordValidator.validateOrThrow("GoodPass1!"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> PasswordValidator.validateOrThrow("bad"));
        assertTrue(ex.getMessage().contains("at least"));
    }
}
