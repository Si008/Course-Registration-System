    package com.example.Course.Registration.System.Service;

    import com.example.Course.Registration.System.Model.CourseRegistry;
    import com.example.Course.Registration.System.Model.DefaultUser;
    import com.example.Course.Registration.System.Model.Student;
    import com.example.Course.Registration.System.Repository.CourseRegistryRepo;
    import com.example.Course.Registration.System.Repository.DefaultUserRepo;
    import com.example.Course.Registration.System.Repository.StudentRepo;
    import com.example.Course.Registration.System.config.AppConfig;
    import com.example.Course.Registration.System.exception.*;
    import com.example.Course.Registration.System.security.JwtUtil;
    import com.example.Course.Registration.System.util.AESPasswordUtil;
    import com.example.Course.Registration.System.util.ApiResponseUtil;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    public class StudentService {
        private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

        @Autowired
        StudentRepo studentRepo;

        @Autowired
        CourseRegistryRepo CourseRegistryRepo;

        @Autowired
        AppConfig appConfig;

        @Autowired
        JwtUtil jwtUtil;

        @Autowired
        DefaultUserService defaultUserService;


        @Transactional
        public ApiResponseUtil<?> addStudents(Student student) {
            logger.info("Student service is called");

            if (student.getName() == null || student.getName().trim().isEmpty() ||
                    student.getEmail() == null || student.getEmail().trim().isEmpty() ||
                    student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                throw new BlankFieldException("Name, Email and Password must not be empty");
            }
            logger.info("None of the field is empty");

            if(!student.getName().matches(appConfig.getRegisterUserNameRegex())){
                    throw new BlankFieldException("Invalid name ");
            }
            logger.info("Valid name format");
            if (!student.getEmail().matches(appConfig.getEmailRegexFormat())) {
                throw new BlankFieldException("Invalid email format");
            }
            logger.info("Email format is valid");

            if (!student.getPassword().matches(appConfig.getPassRegexFormat())) {
                throw new BlankFieldException("Password must be minimum 8 characters,one upper case, one lower case ,1 digit and 1 special characters");
            }
            logger.info("Password format matched");

            if (studentRepo.existsByEmail(student.getEmail())) {
                return new ApiResponseUtil<>("FAILURE",false,"Email already exists . Please LOG IN !", HttpStatus.CONFLICT.value(), null);
            }
            logger.info("Email is not already registered");

            boolean isValid = defaultUserService.validateDefaultUser(appConfig.getAdminUserName(), appConfig.getAdminPassword());
            if(isValid) {
                logger.info("Admin credentials validated");
                String token = jwtUtil.generateToken(student.getEmail(),"USER");
                System.out.println("Token : " + token);
                if(!token.isEmpty()) {
                    String encryptedPassword = AESPasswordUtil.encrypt(student.getPassword());
                    student.setPassword(encryptedPassword);
                    studentRepo.save(student);
                    logger.info("Password encrypted and student registered successfully");
                    return new ApiResponseUtil<>("SUCCESS", true, "Student registered successfully", HttpStatus.CREATED.value(), null);
                }
                else{
                    logger.warn("Token is empty");
                }
            }
            logger.warn("Admin credentials not valid");
            return new ApiResponseUtil<>("FAILURE ",false,"Student Default user is not valid ",HttpStatus.NOT_FOUND.value(), null );
        }
        // _____________________________________________________________________________
        @Transactional(noRollbackFor = {InvalidCredentialsException.class, AccountLockedException.class})
        public ApiResponseUtil<?> login(Student student) {
            logger.info("Login method called");

            // 1. Blank field validation
            if (student.getEmail() == null || student.getEmail().trim().isEmpty() ||
                    student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                throw new BlankFieldException("Email and Password must not be empty");
            }

            // 2. Fetch from DB
            Student studentFromDB = studentRepo.findByEmailAndName(student.getEmail(),student.getName());
            if (studentFromDB == null) {
                throw new ResourceNotFoundException("Student name " + student.getName() + " or email " + student.getEmail() + " is incorrect");
            }

            // 3. Lockout check
            if (studentFromDB.getLockOutTime() != null &&
                    LocalDateTime.now().isBefore(studentFromDB.getLockOutTime())) {
                long minutesLeft = Duration.between(LocalDateTime.now(), studentFromDB.getLockOutTime()).toMinutes();
                throw new AccountLockedException("Account is locked. Try again in " + minutesLeft + " minutes.");
            }

            // 4. Decrypt password
            String decryptedPassword = AESPasswordUtil.decrypt(studentFromDB.getPassword());
            System.out.println(decryptedPassword);

            // 5. If password matches
            if (student.getPassword().equals(decryptedPassword)) {
                studentFromDB.setFailedAttempts(0);
                studentFromDB.setLockOutTime(null);
                studentRepo.saveAndFlush(studentFromDB);

                String token = jwtUtil.generateToken(studentFromDB.getEmail(),"USER");
                return new ApiResponseUtil<>("SUCCESS", true, "Logged in successfully", HttpStatus.OK.value(), token);
            }

            // 6. Wrong password case — increment immediately and flush
            int failed = studentFromDB.getFailedAttempts() + 1;
            studentFromDB.setFailedAttempts(failed);
            studentRepo.saveAndFlush(studentFromDB); // flush before checking lock

            // 7. If failed attempts exceed limit
            if (failed >= appConfig.getMaxFailedAttempts()) {
                LocalDateTime unlockTime = LocalDateTime.now().plusMinutes(appConfig.getLockoutDurationMinutes());
                studentFromDB.setLockOutTime(unlockTime);
                studentFromDB.setFailedAttempts(0);
                studentRepo.saveAndFlush(studentFromDB);
                throw new AccountLockedException(
                        "Account locked for " + appConfig.getLockoutDurationMinutes() + " minutes."
                );
            }

            // 8. Throw exception after flushing the increment
            throw new InvalidCredentialsException(
                    "Incorrect password. Attempt " + failed + " of " + appConfig.getMaxFailedAttempts()
            );
        }


    // _________________________________________________________________________________

        @Transactional(readOnly = true)
        public ApiResponseUtil<List<?>> getMyCourses(String email) {
            logger.info("Get my courses method called");
            if (!CourseRegistryRepo.existsByEmail(email)) {
                throw new ResourceNotFoundException("No courses found for email: " + email);
            }
            logger.info("Courses found for the email");
            List<CourseRegistry> list = CourseRegistryRepo.findByEmail(email);
            return new ApiResponseUtil<>("SUCCESS", true, "Courses fetched", HttpStatus.FOUND.value(), list);
        }
    //-----------------------------------------------------------------------------
        @Transactional(readOnly = true)
        public String getStudentByEmail(String email) {
            logger.info("fetching student name using email method is called");
            String name = studentRepo.findNameByEmail(email);
            if (name == null) {
                throw new ResourceNotFoundException("No student name found for email: " + email);
            }
            logger.info("Name is returned");
            return name;
        }
    }
