package com.example.Course.Registration.System.util;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class AESPasswordUtil {

    private static String secretKey;
    private static final String ALGORITHM = "AES";

    @PostConstruct
    public void init() {
        secretKey = loadAesKey();
        if (secretKey == null || secretKey.length() != 16) {
            throw new RuntimeException("Invalid AES key: must be exactly 16 characters");
        }
        System.out.println(" AES key loaded at startup = " + secretKey);
    }

    public static String encrypt(String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public static String decrypt(String encryptedPassword) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }

    private String loadAesKey() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("aesconfig")) {
            if (input == null) {
                throw new RuntimeException("aesconfig not found in resources");
            }
            props.load(input);
            return props.getProperty("aes.key.value");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load AES key", e);
        }
    }
}
