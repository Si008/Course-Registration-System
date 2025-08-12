package com.example.Consoleapp;

import com.example.Consoleapp.AppConfig;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleAppApplication {
    static Scanner inp = new Scanner(System.in);
    static RestTemplate restTemplate = new RestTemplate();
    static String studentToken = null;
    static String loggedInName = null;
    static String loggedInEmail = null;

    public static void main(String[] args) {
        while (true) {
            if (studentToken == null) {
                studentAuthMenu();
            } else {
                studentMenu();
            }
        }
    }

    private static void studentAuthMenu() {
        int attempts = 0;
        while (attempts < AppConfig.getMaxStudentAuthAttempts()) {
            System.out.println(" ***************** Welcome User please click any of the below options **********************");
            System.out.println("\t   \t      \t     \t       1.REGISTER  \t");
            System.out.println("\t   \t      \t     \t       2.LOGIN     \t");
            System.out.println("\t   \t       \t    \t       3.Exit     \t");

            System.out.print("Enter the choice: ");
            String c = inp.nextLine().trim();
            switch (c) {
                case "1" -> {
                    registerStudent();
                    return; }
                case "2" -> { loginStudent(); return; }
                case "3" -> System.exit(0);
                default -> {
                    attempts++;
                    System.out.println("Invalid choice. attempts :" + attempts  + " out of :" + AppConfig.getMaxStudentAuthAttempts());
                }
            }
        }
        System.out.println("Max attempts reached. Logging out guest user.");
        System.exit(0);
    }

    public static void registerStudent() {
        int attempts = 0;
        while (attempts < AppConfig.getMaxRegisterAttempts()) {
            System.out.println("Enter your name :");
            String name = inp.nextLine().trim();

            System.out.println("Enter your email :");
            String email = inp.nextLine().trim();

            System.out.println("Enter your password :");
            String password = inp.nextLine().trim();

            Map<String, String> payLoad = new HashMap<>();
            payLoad.put("name", name);
            payLoad.put("email", email);
            payLoad.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payLoad, headers);
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        AppConfig.getStudentRegisterUrl(),
                        HttpMethod.POST,
                        entity,
                        Map.class
                );
                if (response.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println(response.getBody());
                    System.out.println("Student has registered successfully. Please log in HERO");
                    return;
                } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                    return;
                } else {
                    attempts++;
                    System.out.println("Registration failed: " + response.getBody());
                    System.out.println("Attempts : " + attempts  + " out of " + AppConfig.getMaxRegisterAttempts());
                }
            } catch (Exception e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                System.out.println("Attempts : " + attempts + " out of " + AppConfig.getMaxRegisterAttempts());
            }
        }
        System.out.println("Max registration attempts reached.");
        System.exit(0);
    }

    private static void loginStudent() {
        int attempts = 0;
        while (attempts < AppConfig.getMaxLoginAttempts()) {

            System.out.println("******* LOG IN  ********");
            System.out.println("Enter your name :");
            String name = inp.nextLine().trim();

            System.out.println("Enter your email :");
            String email = inp.nextLine().trim();

            System.out.println("Enter your password :");
            String password = inp.nextLine().trim();

            Map<String, String> payLoad = new HashMap<>();
            payLoad.put("name", name);
            payLoad.put("email", email);
            payLoad.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payLoad, headers);
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        AppConfig.getStudentLoginUrl(),
                        HttpMethod.POST,
                        entity,
                        Map.class
                );
                if (response.getStatusCode() == HttpStatus.OK) {
                    System.out.println(response.getBody());
                    loggedInEmail = email;
                    loggedInName = name;
                    studentToken = (String) response.getBody().get("data");
                    return;
                } else {
                    attempts++;
                    System.out.println("Login failed: " + response.getBody());
                    System.out.println("Attempts : " + attempts  + " out of " + AppConfig.getMaxLoginAttempts());

                }
            } catch (Exception e) {
                attempts++;
                System.out.println("Error: " + extractErrorMessage(e));
                System.out.println("Attempts : " + attempts   + " out of " + AppConfig.getMaxRegisterAttempts());
            }
        }
        System.out.println("Max login attempts reached.");
        System.exit(0);
    }

    private static void studentMenu() {
        int attempts = 0;
        while (attempts < AppConfig.getMaxStudentMenuLoop()) {
            System.out.println("\n ============== STUDENT MENU ===============");
            System.out.println("1. View All Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. View My Courses");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");

            String choice = inp.nextLine().trim();
            switch (choice) {
                case "1" -> { viewAllCourses(); return; }
                case "2" -> { enrollInCourse(); return; }
                case "3" -> { viewMyCourses(); return; }
                case "4" -> {
                    studentToken = null;
                    loggedInName = null;
                    loggedInEmail = null;
                    System.out.println("Logged out successfully.GOOD BYE STUDENT");
                    System.exit(0);
                    return;

                }
                default -> {
                    attempts++;
                    System.out.println("Invalid choice. Try again.");
                    System.out.println("Attempts : " + attempts  + " out of " + AppConfig.getMaxStudentMenuLoop());
                }
            }
        }
        System.out.println("Max menu attempts reached. Logging out student.");
        studentToken = null;
        System.exit(0);
    }

    private static void viewAllCourses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + studentToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    AppConfig.getCourseAllCoursesUrl(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            System.out.println("\nAvailable Courses:");
            System.out.println(response.getBody());
        } catch (Exception e) {
            System.out.println("Error fetching courses: " + extractErrorMessage(e));
        }
    }

    private static void enrollInCourse() {
        int attempts = 0;
        while (attempts < AppConfig.getMaxEnrollCourse()) {
            System.out.print("Enter course name to enroll: ");
            String courseName = inp.nextLine().trim();

            Map<String, String> payload = new HashMap<>();
            payload.put("name", loggedInName);
            payload.put("email", loggedInEmail);
            payload.put("courseName", courseName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + studentToken);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        AppConfig.getCourseRegistryEnroll(),
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                System.out.println(response.getBody());
                return;
            } catch (Exception e) {
                attempts++;
                System.out.println("Error enrolling in course: " + extractErrorMessage(e));
                System.out.println("Attempts : " + attempts  + " out of " + AppConfig.getMaxEnrollCourse());
            }
        }
        System.out.println("Max enrollment attempts reached.");
        System.exit(0);
    }

    private static void viewMyCourses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + studentToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            String url = AppConfig.getStudentMyCoursesUrl() + "?email=" + loggedInEmail;

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            System.out.println("Your Courses:");
            System.out.println(response.getBody());
        } catch (Exception e) {
            System.out.println("Error fetching your courses: " + extractErrorMessage(e));
        }
    }


    private static String extractErrorMessage(Exception e) {
        String msg = e.getMessage();
        if (msg != null && msg.contains("500")) return "Internal Server Error on server.";
        if (msg != null && msg.contains("403")) return "Access Denied - Invalid or expired token.";
        if (msg != null && msg.contains("400")) return "Bad Request - Please check your input.";
        if (msg != null && msg.contains("401")) return "Unauthorized - Invalid credentials.";
        return msg;
    }
}
