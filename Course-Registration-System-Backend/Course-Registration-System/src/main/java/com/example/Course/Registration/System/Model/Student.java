package com.example.Course.Registration.System.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
        name = "Student.fetchStudentByEmailAndName",
        procedureName = "getStudentByEmailAndName1",
        resultClasses = Student.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN,name="email",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN,name ="name",type = String.class )
        }
        ),
        @NamedStoredProcedureQuery(
                name = "Student.existStudentByEmail",
                procedureName = "getBooleanByEmail",
                resultClasses = boolean.class,
                parameters = {
                        @StoredProcedureParameter(mode=ParameterMode.IN,name="email",type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "Student.getNameByEmail",
                procedureName = "getNameByEmail",
                resultClasses = String.class,
                parameters = {
                        @StoredProcedureParameter(mode=ParameterMode.IN,name="email",type=String.class)
                }
        )
}
)

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;
    private String email;
    private String password;
    @Column(name = "failed_attempts")
    private int failedAttempts;

    @Column(name = "lock_out_time")
    private LocalDateTime lockOutTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockOutTime() {
        return lockOutTime;
    }

    public void setLockOutTime(LocalDateTime lockOutTime) {
        this.lockOutTime = lockOutTime;
    }

    public Student() {
    }

    public Student(String name, String email, String password, int failedAttempts, LocalDateTime lockOutTime) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.failedAttempts = failedAttempts;
        this.lockOutTime = lockOutTime;
    }
}
