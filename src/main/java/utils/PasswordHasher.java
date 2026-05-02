package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordHasher {

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 600000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String passwordAttempt, String dbPassword) throws Exception {
        String[] parts = dbPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);

        KeySpec spec = new PBEKeySpec(passwordAttempt.toCharArray(), salt, 600000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] testHash = factory.generateSecret(spec).getEncoded();

        return java.util.Arrays.equals(testHash, storedHash);
    }
}