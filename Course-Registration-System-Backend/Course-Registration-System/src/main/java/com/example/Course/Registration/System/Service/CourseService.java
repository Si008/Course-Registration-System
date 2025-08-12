package com.example.Course.Registration.System.Service;

import com.example.Course.Registration.System.Model.Course;
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
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    CourseRepo CourseRepo;

    @Transactional(readOnly = true)
    public List<Course> availableCourses() {
        logger.info("Fetching available courses");
        return CourseRepo.findAll();
    }

    @Transactional
    public ApiResponseUtil<?> addCourse(Course course) {
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty() ||
                course.getCourseName() == null || course.getCourseName().trim().isEmpty() ||
                course.getTrainer() == null || course.getTrainer().trim().isEmpty()) {
            throw new BlankFieldException("Course ID, Course Name, and Trainer cannot be empty");
        }
        logger.info("None of the input field was missing");

        if (CourseRepo.existsByUserName(course.getCourseId())) {
            throw new ResourceNotFoundException("Course ID already exists in DB");
        }
        logger.info("Course id not exists in DB already");
        CourseRepo.saveAndFlush(course);
        logger.info("Course added to the DB");
        return new ApiResponseUtil<>("SUCCESS", true, "Course added successfully", HttpStatus.CREATED.value(), null);
    }

    @Transactional
    public ApiResponseUtil<?> updateCourse(Course course) {
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty() ||
                course.getCourseName() == null || course.getCourseName().trim().isEmpty() ||
                course.getTrainer() == null || course.getTrainer().trim().isEmpty()) {
            throw new BlankFieldException("Course ID, Course Name, and Trainer cannot be empty");
        }
        logger.info("None of the give input field were missing");

        if (!CourseRepo.existsByUserName(course.getCourseId())) {
            throw new ResourceNotFoundException("Course ID not found in DB");
        }
        logger.info("Course Id found in DB");
        CourseRepo.save(course);
        logger.info("Course updated successfully");
        return new ApiResponseUtil<>("SUCCESS", true, "Course updated successfully", HttpStatus.OK.value(), null);
    }

    @Transactional
    public ApiResponseUtil<?> deleteCourse(String courseId) {
        logger.info("delete Course method is called");
        if (!CourseRepo.existsByUserName(courseId)) {
            throw new ResourceNotFoundException("No such course exists.");
        }
        logger.info("CourseID found in DB");
        CourseRepo.deleteById(courseId);
        logger.info("Course delete from DB successfully");
        return new ApiResponseUtil<>("SUCCESS", true, "Course deleted successfully", HttpStatus.OK.value(), null);
    }
}
