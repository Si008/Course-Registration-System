package com.example.Course.Registration.System.config;

import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class AppConfig {

    private final Properties properties;

    public AppConfig() {
        properties = new Properties();
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("aesconfig")) {
            if (fis == null) {
                throw new RuntimeException("aesconfig file not found in resources folder");
            }
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties file from classpath", e);
        }
    }

    // ====== Getters ======
    public int getLockoutDurationMinutes() {
        return Integer.parseInt(properties.getProperty("login.lockout.duration.minutes"));
    }

    public int getMaxFailedAttempts() {
        return Integer.parseInt(properties.getProperty("login.max.failed.attempts"));
    }

    public String getRegisterUserNameRegex() {
        return properties.getProperty("username.regex.format");
    }

    public String getEmailRegexFormat() {
        return properties.getProperty("email.regex.format");
    }

    public String getPassRegexFormat() {
        return properties.getProperty("password.regex.format");
    }

    public String getJwtSecretKey() {
        return properties.getProperty("secret.key.JWT");
    }

    public String getPassDefaultFormat() {
        return properties.getProperty("default.user.password.format");
    }

    public String getUserNameDefaultFormat() {
        return properties.getProperty("default.user.username.format");
    }

    public String getAdminUserName() {
        return properties.getProperty("admin.username");
    }

    public String getAdminPassword() {
        return properties.getProperty("admin.password");
    }
}
