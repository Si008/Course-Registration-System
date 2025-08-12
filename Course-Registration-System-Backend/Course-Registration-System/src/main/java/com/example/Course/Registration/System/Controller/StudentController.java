package com.example.Course.Registration.System.Controller;

import com.example.Course.Registration.System.Model.Student;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import com.example.Course.Registration.System.Service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService StudentService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseUtil<?>> addStudents(@RequestBody Student student) {
        logger.info("Register API called");
        return ResponseEntity.status(HttpStatus.CREATED).body(StudentService.addStudents(student));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseUtil<?>> login(@RequestBody Student student) {
        logger.info("Login API called");
        return ResponseEntity.ok(StudentService.login(student));
    }

    @GetMapping("/myCourses")
    public ResponseEntity<ApiResponseUtil<List<?>>> getMyCourses(@RequestParam("email") String email) {
        logger.info("Get My Courses API called");
        return ResponseEntity.ok(StudentService.getMyCourses(email));
    }

    @GetMapping("/studentName")
    public ResponseEntity<ApiResponseUtil<String>> getStudentName(@RequestParam("email") String email) {
        logger.info("Get Student Name API called");
        String name = StudentService.getStudentByEmail(email);
        ApiResponseUtil<String> response = new ApiResponseUtil<>("SUCCESS", true, "Student name found", HttpStatus.OK.value(), name);
        return ResponseEntity.ok(response);
    }
}
