package com.example.Course.Registration.System.Controller;

import com.example.Course.Registration.System.Model.Course;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import com.example.Course.Registration.System.Service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    CourseService CourseService;

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> availableCourses() {
        logger.info("Available Courses API called");
        return ResponseEntity.ok(CourseService.availableCourses());
    }

    @PostMapping("/addCourse")
    public ResponseEntity<ApiResponseUtil<?>> addCourses(@RequestBody Course course) {
        logger.info("Add Course API called");
        return ResponseEntity.ok(CourseService.addCourse(course));
    }

    @PostMapping("/updateCourse")
    public ResponseEntity<ApiResponseUtil<?>> updateCourses(@RequestBody Course course) {
        logger.info("Update Course API called");
        return ResponseEntity.ok(CourseService.updateCourse(course));
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity<ApiResponseUtil<?>> deleteCourse(@RequestParam("courseId") String courseId) {
        logger.info("Delete Course API called");
        return ResponseEntity.ok(CourseService.deleteCourse(courseId));
    }

}
