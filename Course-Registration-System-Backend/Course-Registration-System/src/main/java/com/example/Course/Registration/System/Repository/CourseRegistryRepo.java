    package com.example.Course.Registration.System.Repository;

    import com.example.Course.Registration.System.Model.CourseRegistry;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.query.Procedure;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface CourseRegistryRepo extends JpaRepository<CourseRegistry,Integer> {

        @Procedure(name = "CourseRegistry.getBoolByEmailExists")
        boolean existsByEmail(@Param("email") String email);

        @Procedure(name ="CourseRegistry.getBoolByEmailAndCourseName")
        boolean existsByEmailAndCourseName(@Param("email") String email, @Param("course_name") String course_Name);

        @Procedure(name = "CourseRegistry.getCourseRegistryByEmail")
        List<CourseRegistry> findByEmail(@Param("email") String email);
    }


