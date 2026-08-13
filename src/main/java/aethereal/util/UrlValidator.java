package aethereal.util;


import java.net.URI;

public class UrlValidator {
    private UrlValidator() {
    }

    public static void a(String url, String fieldName, int maxLength) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        if (maxLength > 0 && url.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must be at most " + maxLength + " characters");
        }
        try {
            URI uri = URI.create(url);
            if (!uri.isAbsolute() || uri.getScheme() == null || uri.getHost() == null || uri.getHost().isBlank()) {
                throw new IllegalArgumentException(fieldName + " must be a valid absolute HTTPS URL: " + url);
            }
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new IllegalArgumentException(fieldName + " must use https://, got: " + url);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid absolute HTTPS URL: " + url, e);
        }
    }
}
