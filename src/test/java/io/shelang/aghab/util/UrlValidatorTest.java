package io.shelang.aghab.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UrlValidatorTest {

    @Test
    void testSafeRedirectUrls() {
        assertTrue(UrlValidator.isSafeRedirectUrl("http://google.com"));
        assertTrue(UrlValidator.isSafeRedirectUrl("https://google.com"));
        assertTrue(UrlValidator.isSafeRedirectUrl("ftp://ftp.is.co.za"));
        assertTrue(UrlValidator.isSafeRedirectUrl("myapp://some-deep-link/token/123"));
        assertTrue(UrlValidator.isSafeRedirectUrl("twitter://user?screen_name=bar"));
        assertTrue(UrlValidator.isSafeRedirectUrl("zoommtg://zoom.us/join?confno=123"));
    }

    @Test
    void testDangerousRedirectUrls() {
        assertFalse(UrlValidator.isSafeRedirectUrl("javascript:alert(1)"));
        assertFalse(UrlValidator.isSafeRedirectUrl("javascript:void(0)"));
        assertFalse(UrlValidator.isSafeRedirectUrl("vbscript:msgbox('hello')"));
        assertFalse(UrlValidator.isSafeRedirectUrl("data:text/html,<html></html>"));
        assertFalse(UrlValidator.isSafeRedirectUrl("file:///etc/passwd"));
        assertFalse(UrlValidator.isSafeRedirectUrl("jar:file:!/"));
    }

    @Test
    void testObfuscatedSchemes() {
        assertFalse(UrlValidator.isSafeRedirectUrl("java\nscript:alert(1)"));
        assertFalse(UrlValidator.isSafeRedirectUrl("javascrip%74:alert(1)"));
        assertFalse(UrlValidator.isSafeRedirectUrl(" javascript:alert(1)"));
    }

    @Test
    void testInvalidFormats() {
        assertFalse(UrlValidator.isSafeRedirectUrl(null));
        assertFalse(UrlValidator.isSafeRedirectUrl(""));
        assertFalse(UrlValidator.isSafeRedirectUrl("noscheme.com"));
        assertFalse(UrlValidator.isSafeRedirectUrl("://missing-scheme"));
    }
}
