package com.example.Consoleapp;


public class CourseRegistry {
    private int id;
    private String name ;
    private String courseName ;
    private String email ;

    public CourseRegistry(String name, String courseName, String email) {
        this.name = name;
        this.courseName = courseName;
        this.email = email;
    }

    public CourseRegistry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseName='" + courseName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
