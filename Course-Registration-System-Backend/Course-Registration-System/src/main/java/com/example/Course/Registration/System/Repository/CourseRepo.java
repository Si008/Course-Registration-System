package com.example.Course.Registration.System.Repository;

import com.example.Course.Registration.System.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends JpaRepository<Course,String> {

    @Procedure(name = "Course.existsByUserName")
    boolean existsByUserName(String courseId);

    @Procedure(name ="Student.getBoolByCourseName")
    boolean existsByCourseName(@Param("courseName") String courseName);


}
