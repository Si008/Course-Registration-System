package com.example.Consoleapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    // Absolute or relative path to config.properties
    private static final String CONFIG_PATH = "D:/config/config.properties";
    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            props.load(fis);
            System.out.println("Loaded properties from: " + CONFIG_PATH);
        } catch (IOException e) {
            System.out.println("Could not load config.properties, using defaults.");
            props.setProperty("max.login.attempts", "3");
            props.setProperty("max.register.attempts", "3");
            props.setProperty("max.student.loop", "3");
            props.setProperty("max.enroll.loop", "3");
            props.setProperty("max.student.auth.attempts", "3");
        }
    }

    public static int getMaxLoginAttempts() {
        return Integer.parseInt(props.getProperty("max.login.attempts"));
    }

    public static int getMaxRegisterAttempts() {
        return Integer.parseInt(props.getProperty("max.register.attempts"));
    }

    public static int getMaxStudentAuthAttempts() {
        return Integer.parseInt(props.getProperty("max.student.auth.attempts"));
    }

    public static int getMaxStudentMenuLoop() {
        return Integer.parseInt(props.getProperty("max.student.loop"));
    }

    public static int getMaxEnrollCourse() {
        return Integer.parseInt(props.getProperty("max.enroll.loop"));
    }

    public static String getStudentRegisterUrl() {
        return props.getProperty("student.register");
    }

    public static String getStudentLoginUrl() {
        return props.getProperty("student.login");
    }

    public static String getStudentMyCoursesUrl() {
        return props.getProperty("student.myCourses");
    }

    public static String getCourseAllCoursesUrl() {
        return props.getProperty("course.allCourses");
    }

    public static String getCourseRegistryEnroll() {
        return props.getProperty("courseRegistry.enroll");
    }
}
