package com.example.Course.Registration.System.Controller;

import com.example.Course.Registration.System.Model.CourseRegistry;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import com.example.Course.Registration.System.Service.CourseRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courseRegistry")
public class CourseRegistryController {

    private static final Logger logger = LoggerFactory.getLogger(CourseRegistryController.class);

    @Autowired
    CourseRegistryService CourseRegistryService;

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponseUtil<?>> registerStudents(@RequestBody CourseRegistry courseRegistry) {
        logger.info("Enroll Student API called");
        return ResponseEntity.ok(CourseRegistryService.enrollStudents(courseRegistry));
    }

    @GetMapping("/enrolledStudents")
    public ResponseEntity<List<CourseRegistry>> enrolledStudents() {
        logger.info("Get Enrolled Students API called");
        return ResponseEntity.ok(CourseRegistryService.getEnrolledStudents());
    }
}
