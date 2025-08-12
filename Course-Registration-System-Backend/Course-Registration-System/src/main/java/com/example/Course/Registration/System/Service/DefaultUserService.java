package com.example.Course.Registration.System.Service;

import com.example.Course.Registration.System.Model.DefaultUser;
import com.example.Course.Registration.System.Repository.DefaultUserRepo;
import com.example.Course.Registration.System.config.AppConfig;
import com.example.Course.Registration.System.exception.BlankFieldException;
import com.example.Course.Registration.System.exception.InvalidCredentialsException;
import com.example.Course.Registration.System.exception.ResourceNotFoundException;
import com.example.Course.Registration.System.util.AESPasswordUtil;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService {

    @Autowired
    DefaultUserRepo defaultUserRepo;

    @Autowired
    AppConfig appConfig;

    Logger logger = LoggerFactory.getLogger(DefaultUserService.class);

    public ApiResponseUtil<?> getCredentials() {
        logger.info("Fetching all default user credentials");
        return new ApiResponseUtil<>("SUCCESS", true, "All the credentials were fetched", HttpStatus.FOUND.value(), defaultUserRepo.findAll());
    }

    public ApiResponseUtil<?> addCredentials(DefaultUser defaultUser) {
        logger.info("Attempting to add new default user credentials: {}", defaultUser.getUserName());

        if (defaultUser.getUserName() == null || defaultUser.getUserName().isEmpty() ||
                defaultUser.getPassword() == null || defaultUser.getPassword().isEmpty()) {
            logger.warn("One or more fields are empty for adding credentials");
            throw new BlankFieldException("None of the field cannot be empty");
        }

        if (!defaultUser.getPassword().matches(appConfig.getPassDefaultFormat())) {
            logger.warn("Password format not matched for username: {}", defaultUser.getUserName());
            throw new InvalidCredentialsException("Password format not matched");
        }
        if (!defaultUser.getUserName().matches(appConfig.getUserNameDefaultFormat())) {
            logger.warn("Username format not matched: {}", defaultUser.getUserName());
            throw new InvalidCredentialsException("admin format not matched");
        }
        if (defaultUserRepo.existsByUserName(defaultUser.getUserName())) {
            logger.error("Username already exists in DB: {}", defaultUser.getUserName());
            throw new RuntimeException("admin name already exists");
        }

        String encryptedPassword = AESPasswordUtil.encrypt(defaultUser.getPassword());
        defaultUser.setPassword(encryptedPassword);
        defaultUserRepo.save(defaultUser);

        logger.info("Default credentials added successfully for username: {}", defaultUser.getUserName());
        return new ApiResponseUtil<>("SUCCESS", true, "Default credentials to get JWT1 token is added", HttpStatus.CREATED.value(), null);
    }

    public ApiResponseUtil<?> deleteCredentials(String userName) {
        logger.info("Attempting to delete credentials for username: {}", userName);

        if (userName.isEmpty()) {
            logger.warn("Username is empty while attempting delete");
            throw new BlankFieldException("Admin name cannot be empty");
        }
        if (!defaultUserRepo.existsByUserName(userName)) {
            logger.error("Username not found in DB: {}", userName);
            throw new RuntimeException("Admin name not found in DB");
        }

        defaultUserRepo.deleteByUserName(userName);
        logger.info("Credentials deleted successfully for username: {}", userName);

        return new ApiResponseUtil<>("SUCCESS", true, "Credentials deleted successfully", HttpStatus.FOUND.value(), null);
    }

    public boolean validateDefaultUser(String username, String rawPassword) {
        logger.info("Validating default user: {}", username);

        DefaultUser user = defaultUserRepo.findNativeByUserName(username);
        if (user == null) {
            logger.warn("Default user not found: {}", username);
            return false;
        }

        logger.info("User is found in DB for username: {}", username);
        String decryptedPassword = AESPasswordUtil.decrypt(user.getPassword());
        logger.info("Password decrypted successfully, comparing with raw password");

        boolean isValid = decryptedPassword.equals(rawPassword);
        if (isValid) {
            logger.info("Default user authentication successful for username: {}", username);
        } else {
            logger.warn("Password mismatch for username: {}", username);
        }
        return isValid;
    }

    public ApiResponseUtil<?> updateCredentials(String oldUserName, String newUserName, String newPassword) {
        logger.info("Updating default credentials from '{}' to '{}'", oldUserName, newUserName);

        if (oldUserName == null || oldUserName.isEmpty() ||
                newUserName == null || newUserName.isEmpty() ||
                newPassword == null || newPassword.isEmpty()) {
            logger.warn("One or more fields are empty for update operation");
            throw new BlankFieldException("All fields must be filled");
        }

        if (!newPassword.matches(appConfig.getPassDefaultFormat())) {
            logger.warn("New password format not matched for username: {}", newUserName);
            throw new InvalidCredentialsException("Password format not matched");
        }
        if (!newUserName.matches(appConfig.getUserNameDefaultFormat())) {
            logger.warn("New username format not matched: {}", newUserName);
            throw new InvalidCredentialsException("Username format not matched");
        }

        DefaultUser existingUser = defaultUserRepo.findNativeByUserName(oldUserName);
        if (existingUser == null) {
            logger.error("Default user not found for update: {}", oldUserName);
            throw new ResourceNotFoundException("Default user not found: " + oldUserName);
        }

        existingUser.setUserName(newUserName);
        existingUser.setPassword(AESPasswordUtil.encrypt(newPassword));

        defaultUserRepo.save(existingUser);
        logger.info("Default credentials updated successfully for username: {}", newUserName);

        return new ApiResponseUtil<>("SUCCESS", true, "Default credentials updated", HttpStatus.OK.value(), null);
    }
}
