package io.shelang.aghab.util;

import java.util.regex.Pattern;

/**
 * Utility class for sanitizing script content to prevent XSS attacks.
 * Removes or escapes potentially dangerous HTML/JavaScript elements.
 */
public final class ScriptSanitizer {

        private ScriptSanitizer() {
                throw new IllegalAccessError("Utility class");
        }

        // Pattern to match script tags (opening and closing)
        private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile(
                        "<script[^>]*>[\\s\\S]*?</script>", Pattern.CASE_INSENSITIVE);

        // Pattern to match event handlers like onclick, onerror, onload, etc.
        private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile(
                        "\\s*on\\w+\\s*=\\s*[\"'][^\"']*[\"']", Pattern.CASE_INSENSITIVE);

        // Pattern to match javascript: protocol
        private static final Pattern JAVASCRIPT_PROTOCOL_PATTERN = Pattern.compile(
                        "javascript\\s*:", Pattern.CASE_INSENSITIVE);

        // Pattern to match data: protocol with potential script content
        private static final Pattern DATA_PROTOCOL_PATTERN = Pattern.compile(
                        "data\\s*:\\s*text/html", Pattern.CASE_INSENSITIVE);

        // Pattern to match iframe, object, embed tags
        private static final Pattern DANGEROUS_TAGS_PATTERN = Pattern.compile(
                        "<\\s*/?\\s*(iframe|object|embed|form|input|meta|link|base|svg|math)[^>]*>",
                        Pattern.CASE_INSENSITIVE);

        // Pattern to match style tags and expressions
        private static final Pattern STYLE_EXPRESSION_PATTERN = Pattern.compile(
                        "(expression\\s*\\(|behavior\\s*:|binding\\s*:|-moz-binding\\s*:)",
                        Pattern.CASE_INSENSITIVE);

        // Pattern to match HTML comments that could hide malicious content
        private static final Pattern HTML_COMMENT_PATTERN = Pattern.compile(
                        "<!--[\\s\\S]*?-->", Pattern.CASE_INSENSITIVE);

        /**
         * Sanitizes the given script content by removing potentially dangerous
         * elements.
         * 
         * @param content The raw script content to sanitize
         * @return Sanitized content safe for rendering, or empty string if input is
         *         null
         */
        public static String sanitizeScript(String content) {
                if (content == null) {
                        return "";
                }

                String sanitized = content;

                // Remove HTML comments first (could hide malicious payloads)
                sanitized = HTML_COMMENT_PATTERN.matcher(sanitized).replaceAll("");

                // Remove script tags
                sanitized = SCRIPT_TAG_PATTERN.matcher(sanitized).replaceAll("");

                // Remove dangerous tags (iframe, object, embed, etc.)
                sanitized = DANGEROUS_TAGS_PATTERN.matcher(sanitized).replaceAll("");

                // Remove event handlers
                sanitized = EVENT_HANDLER_PATTERN.matcher(sanitized).replaceAll("");

                // Remove javascript: protocol
                sanitized = JAVASCRIPT_PROTOCOL_PATTERN.matcher(sanitized).replaceAll("");

                // Remove data: protocol with HTML content
                sanitized = DATA_PROTOCOL_PATTERN.matcher(sanitized).replaceAll("");

                // Remove style expressions (IE-specific XSS vectors)
                sanitized = STYLE_EXPRESSION_PATTERN.matcher(sanitized).replaceAll("");

                return sanitized.trim();
        }

        /**
         * Checks if the content contains potentially dangerous elements.
         * 
         * @param content The content to check
         * @return true if the content contains dangerous elements, false otherwise
         */
        public static boolean containsDangerousContent(String content) {
                if (content == null || content.isEmpty()) {
                        return false;
                }

                return SCRIPT_TAG_PATTERN.matcher(content).find()
                                || DANGEROUS_TAGS_PATTERN.matcher(content).find()
                                || EVENT_HANDLER_PATTERN.matcher(content).find()
                                || JAVASCRIPT_PROTOCOL_PATTERN.matcher(content).find()
                                || DATA_PROTOCOL_PATTERN.matcher(content).find()
                                || STYLE_EXPRESSION_PATTERN.matcher(content).find();
        }
}
