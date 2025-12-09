package io.shelang.aghab.util;

/**
 * Utility class for sanitizing redirect URLs to prevent:
 * - CRLF injection (HTTP response splitting)
 * - Open redirect attacks via javascript: protocol
 * - Data URL attacks
 */
public final class RedirectSanitizer {

    private RedirectSanitizer() {
        throw new IllegalAccessError("Utility class");
    }

    // Characters that could cause CRLF injection
    private static final char CR = '\r';
    private static final char LF = '\n';

    /**
     * Sanitizes a URL for safe use in HTTP Location header.
     * Removes CRLF characters and validates URL protocol.
     *
     * @param url The URL to sanitize
     * @return Sanitized URL safe for use in headers
     * @throws IllegalArgumentException if URL uses dangerous protocol
     */
    public static String sanitizeRedirectUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        // Remove CRLF characters that could cause HTTP response splitting
        String sanitized = url.replace(String.valueOf(CR), "")
                .replace(String.valueOf(LF), "")
                .replace("%0d", "")
                .replace("%0D", "")
                .replace("%0a", "")
                .replace("%0A", "");

        // Check for dangerous protocols
        String lowerUrl = sanitized.toLowerCase().trim();

        if (lowerUrl.startsWith("javascript:")) {
            throw new IllegalArgumentException("javascript: URLs are not allowed");
        }

        if (lowerUrl.startsWith("data:")) {
            throw new IllegalArgumentException("data: URLs are not allowed");
        }

        if (lowerUrl.startsWith("vbscript:")) {
            throw new IllegalArgumentException("vbscript: URLs are not allowed");
        }

        return sanitized;
    }

    /**
     * Checks if the URL is safe for redirect.
     *
     * @param url The URL to check
     * @return true if the URL is safe, false otherwise
     */
    public static boolean isSafeRedirectUrl(String url) {
        try {
            sanitizeRedirectUrl(url);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
