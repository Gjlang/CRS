package com.myapp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Simple SHA-256 hashing for assignment.
 * (BCrypt is better, but SHA-256 is still much safer than plaintext.)
 */
public class PasswordHasher {

    public static String hash(String passwordPlain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(passwordPlain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }

    public static boolean verify(String passwordPlain, String storedHash) {
        if (storedHash == null) return false;
        return storedHash.equals(hash(passwordPlain));
    }
}
