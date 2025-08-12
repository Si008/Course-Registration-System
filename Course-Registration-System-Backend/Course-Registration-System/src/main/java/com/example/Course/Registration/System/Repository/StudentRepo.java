package com.example.Course.Registration.System.Repository;

import com.example.Course.Registration.System.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student,Integer> {

    @Procedure(name="Student.existStudentByEmail")
    boolean existsByEmail(@Param("email") String email);


    @Procedure(name = "Student.fetchStudentByEmailAndName")
    Student findByEmailAndName(@Param("email") String email , @Param("name") String name);

    @Procedure(name = "Student.getNameByEmail")
    String findNameByEmail(@Param("email") String email);
}
