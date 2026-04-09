package com.magicvs.backend.util;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {}

    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_-]{3,30}$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD = Pattern.compile("^(?=.{8,12}$)(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$");

    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        return USERNAME.matcher(username).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return PASSWORD.matcher(password).matches();
    }

    public static boolean isUsernameOrEmail(String s) {
        return isValidUsername(s) || isValidEmail(s);
    }

    public static String sanitizeDisplayName(String input) {
        if (input == null) return null;
        // Remove HTML tags
        String stripped = input.replaceAll("<.*?>", "");
        // Allow letters, numbers, spaces, underscore, hyphen
        String cleaned = stripped.replaceAll("[^\\p{L}0-9 _\\-]", "");
        // Trim and limit length
        if (cleaned.length() > 50) {
            cleaned = cleaned.substring(0, 50);
        }
        return cleaned.trim();
    }

    public static boolean containsMaliciousPayload(String s) {
        if (s == null) return false;
        String lower = s.toLowerCase();
        // quick checks for SQL/XSS-ish tokens
        if (lower.contains("<script") || lower.contains("javascript:") || lower.contains("--") || lower.contains(";drop ") || lower.contains("/*") || lower.contains("*/")) {
            return true;
        }
        return false;
    }
}
