package io.shelang.aghab.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {

    @Test
    void testIsValidExternalUrl_ValidUrls() {
        assertTrue(UrlValidator.isValidExternalUrl("https://8.8.8.8")); // Good public IP
        assertTrue(UrlValidator.isValidExternalUrl("http://1.1.1.1/path?q=1"));
    }

    @Test
    void testIsValidExternalUrl_BlockedHosts() {
        assertFalse(UrlValidator.isValidExternalUrl("http://localhost"));
        assertFalse(UrlValidator.isValidExternalUrl("https://127.0.0.1"));
        assertFalse(UrlValidator.isValidExternalUrl("http://[::1]"));
        assertFalse(UrlValidator.isValidExternalUrl("http://metadata.google.internal"));
    }

    @Test
    void testIsValidExternalUrl_InternalIPs() {
        assertFalse(UrlValidator.isValidExternalUrl("http://192.168.1.1"));
        assertFalse(UrlValidator.isValidExternalUrl("http://10.0.0.1"));
        assertFalse(UrlValidator.isValidExternalUrl("http://172.16.0.1"));
        assertFalse(UrlValidator.isValidExternalUrl("http://169.254.169.254"));
    }

    @Test
    void testIsValidExternalUrl_InvalidSchemes() {
        assertFalse(UrlValidator.isValidExternalUrl("ftp://example.com"));
        assertFalse(UrlValidator.isValidExternalUrl("file:///etc/passwd"));
        assertFalse(UrlValidator.isValidExternalUrl("javascript:alert(1)"));
    }

    @Test
    void testValidateOrThrow() {
        assertDoesNotThrow(() -> UrlValidator.validateOrThrow("https://8.8.8.8"));

        assertThrows(IllegalArgumentException.class,
                () -> UrlValidator.validateOrThrow("http://localhost:8080"));
    }
}
