package com.example.Course.Registration.System.Service;

import com.example.Course.Registration.System.Model.CourseRegistry;
import com.example.Course.Registration.System.Repository.CourseRegistryRepo;
import com.example.Course.Registration.System.Repository.CourseRepo;
import com.example.Course.Registration.System.exception.BlankFieldException;
import com.example.Course.Registration.System.exception.ResourceNotFoundException;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseRegistryService {

    private static final Logger logger = LoggerFactory.getLogger(CourseRegistryService.class);

    @Autowired
    CourseRegistryRepo CourseRegistryRepo;

    @Autowired
    CourseRepo CourseRepo;

    @Transactional
    public ApiResponseUtil<?> enrollStudents(CourseRegistry courseRegistry) {
        if (courseRegistry.getCourseName() == null || courseRegistry.getCourseName().trim().isEmpty() ||
                courseRegistry.getEmail() == null || courseRegistry.getEmail().trim().isEmpty() ||
                courseRegistry.getName() == null || courseRegistry.getName().trim().isEmpty()) {
            throw new BlankFieldException("Name, Email, and Course Name cannot be empty");
        }
        logger.info("None of the given input field is missing");

        if (!CourseRepo.existsByCourseName(courseRegistry.getCourseName())) {
            throw new ResourceNotFoundException("Course does not exist in DB");
        }
        logger.info("Course ID exists in DB");

        if (CourseRegistryRepo.existsByEmailAndCourseName(courseRegistry.getEmail(), courseRegistry.getCourseName())) {
            throw new ResourceNotFoundException("You are already enrolled in this course");
        }
        logger.info("You are not already enrolled for this course");

        CourseRegistryRepo.save(courseRegistry);
        logger.info("Course registered in DB successfully");
        return new ApiResponseUtil<>("SUCCESS", true, "Student enrolled successfully", HttpStatus.OK.value(), null);
    }

    @Transactional(readOnly = true)
    public List<CourseRegistry> getEnrolledStudents() {
        logger.info("Enrolled courses method is called");
        return CourseRegistryRepo.findAll();                // Need to implement inbuilt stored procedures.
    }
}
