package io.shelang.aghab.util;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class for validating URLs to prevent SSRF attacks.
 * Blocks internal IP addresses, localhost, and cloud metadata endpoints.
 */
public final class UrlValidator {

    private UrlValidator() {
        throw new IllegalAccessError("Utility class");
    }

    // Blocked hostnames
    private static final Set<String> BLOCKED_HOSTNAMES = Set.of(
            "localhost",
            "127.0.0.1",
            "0.0.0.0",
            "[::1]",
            "metadata.google.internal",
            "metadata.goog",
            "169.254.169.254" // AWS/GCP/Azure metadata endpoint
    );

    // Pattern for internal IP ranges
    private static final Pattern PRIVATE_IP_PATTERN = Pattern.compile(
            "^(10\\." + // 10.0.0.0/8
                    "|172\\.(1[6-9]|2[0-9]|3[0-1])\\." + // 172.16.0.0/12
                    "|192\\.168\\." + // 192.168.0.0/16
                    "|127\\." + // 127.0.0.0/8 (loopback)
                    "|169\\.254\\." + // 169.254.0.0/16 (link-local)
                    "|0\\.)" + // 0.0.0.0/8
                    ".*");

    // Allowed protocols
    private static final Set<String> ALLOWED_PROTOCOLS = Set.of("http", "https");

    /**
     * Validates if the URL is safe to call (not internal/localhost/metadata).
     *
     * @param urlString The URL to validate
     * @return true if the URL is safe to call externally, false otherwise
     */
    public static boolean isValidExternalUrl(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            return false;
        }

        try {
            URI uri = new URI(urlString);

            // Check protocol
            String scheme = uri.getScheme();
            if (scheme == null || !ALLOWED_PROTOCOLS.contains(scheme.toLowerCase())) {
                return false;
            }

            // Check hostname
            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                return false;
            }

            String hostLower = host.toLowerCase();

            // Check blocked hostnames
            if (BLOCKED_HOSTNAMES.contains(hostLower)) {
                return false;
            }

            // Check if it's a private IP pattern
            if (PRIVATE_IP_PATTERN.matcher(hostLower).matches()) {
                return false;
            }

            // Try to resolve and check the actual IP address
            try {
                InetAddress address = InetAddress.getByName(host);
                String ip = address.getHostAddress();

                // Check resolved IP against private ranges
                if (PRIVATE_IP_PATTERN.matcher(ip).matches()) {
                    return false;
                }

                // Check if it's a loopback or link local address
                if (address.isLoopbackAddress() || address.isLinkLocalAddress()
                        || address.isSiteLocalAddress() || address.isAnyLocalAddress()) {
                    return false;
                }

            } catch (UnknownHostException e) {
                // Cannot resolve hostname - might be dangerous, reject it
                return false;
            }

            return true;

        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Validates URL and throws exception if invalid.
     *
     * @param urlString The URL to validate
     * @throws IllegalArgumentException if the URL is not safe
     */
    public static void validateOrThrow(String urlString) {
        if (!isValidExternalUrl(urlString)) {
            throw new IllegalArgumentException(
                    "URL is not allowed: internal addresses, localhost, and metadata endpoints are blocked");
        }
    }
}
