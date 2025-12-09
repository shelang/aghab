package io.shelang.aghab.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedirectSanitizerTest {

    @Test
    void testSanitizeRedirectUrl_CleanUrls() {
        assertEquals("https://example.com",
                RedirectSanitizer.sanitizeRedirectUrl("https://example.com"));
        assertEquals("/local/path",
                RedirectSanitizer.sanitizeRedirectUrl("/local/path"));
    }

    @Test
    void testSanitizeRedirectUrl_CRLF() {
        String input = "https://example.com\r\nSet-Cookie: evil=true";
        String expected = "https://example.comSet-Cookie: evil=true";
        assertEquals(expected, RedirectSanitizer.sanitizeRedirectUrl(input));

        String percentEncoded = "https://example.com%0d%0aSet-Cookie: evil=true";
        assertEquals(expected, RedirectSanitizer.sanitizeRedirectUrl(percentEncoded));
    }

    @Test
    void testSanitizeRedirectUrl_DangerousProtocols() {
        assertThrows(IllegalArgumentException.class,
                () -> RedirectSanitizer.sanitizeRedirectUrl("javascript:alert(1)"));

        assertThrows(IllegalArgumentException.class,
                () -> RedirectSanitizer.sanitizeRedirectUrl("data:text/html,bad"));

        assertThrows(IllegalArgumentException.class,
                () -> RedirectSanitizer.sanitizeRedirectUrl("vbscript:alert(1)"));
    }

    @Test
    void testIsSafeRedirectUrl() {
        assertTrue(RedirectSanitizer.isSafeRedirectUrl("https://safe.com"));
        assertFalse(RedirectSanitizer.isSafeRedirectUrl("javascript:bad()"));
    }
}
