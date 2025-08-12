package com.example.Course.Registration.System.Model;


import jakarta.persistence.*;


@Entity
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "CourseRegistry.getBoolByEmailExists",
                procedureName = "getBoolByEmailExists",
                resultClasses = boolean.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "email",type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "CourseRegistry.getBoolByEmailAndCourseName",
                procedureName = "getBoolByEmailAndCourseName",
                resultClasses = boolean.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "course_name",type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "CourseRegistry.getCourseRegistryByEmail",
                procedureName = "getCourseRegistryByEmail",
                resultClasses = CourseRegistry.class,
                parameters = {
                        @StoredProcedureParameter(mode=ParameterMode.IN,name = "email",type = String.class)
                }
        )
}
)

public class CourseRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String courseName;
    private String email;

    public CourseRegistry() {

    }

    public CourseRegistry(String name, String courseName, String email) {
        this.name = name;
        this.courseName  = courseName;
        this.email = email;
    }

    public int getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
