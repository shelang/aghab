package io.shelang.aghab.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScriptSanitizerTest {

    @Test
    void testSanitizeScript_NullOrEmpty() {
        assertEquals("", ScriptSanitizer.sanitizeScript(null));
        assertEquals("", ScriptSanitizer.sanitizeScript(""));
    }

    @Test
    void testSanitizeScript_CleanContent() {
        String content = "This is safe content";
        assertEquals(content, ScriptSanitizer.sanitizeScript(content));

        String html = "<div><b>Bold</b> text</div>";
        assertEquals(html, ScriptSanitizer.sanitizeScript(html));
    }

    @Test
    void testSanitizeScript_ScriptTags() {
        String content = "Hello <script>alert('xss')</script> World";
        assertEquals("Hello  World", ScriptSanitizer.sanitizeScript(content));

        String complex = "<SCRIPT type=\"text/javascript\">alert('xss')</SCRIPT>";
        assertEquals("", ScriptSanitizer.sanitizeScript(complex));
    }

    @Test
    void testSanitizeScript_EventHandlers() {
        String content = "<img src='x' onerror='alert(1)'>";
        String sanitized = ScriptSanitizer.sanitizeScript(content);
        assertFalse(sanitized.contains("onerror"));

        String click = "<a href='#' onclick=\"doBad()\">Click me</a>";
        assertFalse(ScriptSanitizer.sanitizeScript(click).contains("onclick"));
    }

    @Test
    void testSanitizeScript_Protocols() {
        String js = "<a href='javascript:alert(1)'>Link</a>";
        assertFalse(ScriptSanitizer.sanitizeScript(js).contains("javascript:"));

        String data = "<a href='data:text/html;base64,...'>Link</a>";
        assertFalse(ScriptSanitizer.sanitizeScript(data).contains("data:text/html"));
    }

    @Test
    void testContainsDangerousContent() {
        assertFalse(ScriptSanitizer.containsDangerousContent("Safe text"));
        assertTrue(ScriptSanitizer.containsDangerousContent("<script></script>"));
        assertTrue(ScriptSanitizer.containsDangerousContent("javascript:"));
        assertTrue(ScriptSanitizer.containsDangerousContent("onload=\"\""));
    }
}
