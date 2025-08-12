package com.example.Course.Registration.System.Controller;

import com.example.Course.Registration.System.DTO.UpdateDefaultRequest;
import com.example.Course.Registration.System.Model.DefaultUser;
import com.example.Course.Registration.System.Service.DefaultUserService;
import com.example.Course.Registration.System.util.ApiResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/defaultUser")
public class DefaultUserController {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserController.class);

    @Autowired
    DefaultUserService defaultUserService;

    @GetMapping("/credentials")
    public ResponseEntity<?> getCredentials() {
        logger.info("Fetching all default user credentials");
        return ResponseEntity.ok(defaultUserService.getCredentials());
    }

    @PostMapping("/addNewCredentials")
    public ResponseEntity<ApiResponseUtil<?>> addCredentials(@RequestBody DefaultUser defaultUser) {
        logger.info("Adding new default user credentials for username: {}", defaultUser.getUserName());
        return ResponseEntity.ok(defaultUserService.addCredentials(defaultUser));
    }

    @DeleteMapping("deleteCredentials")
    public ResponseEntity<ApiResponseUtil<?>> deleteCredentials(@RequestParam("userName") String userName) {
        logger.info("Deleting default user credentials for username: {}", userName);
        return ResponseEntity.ok(defaultUserService.deleteCredentials(userName));
    }

    @PutMapping("/updateCredentials")
    public ResponseEntity<ApiResponseUtil<?>> updateCredentials(@RequestBody UpdateDefaultRequest request) {
        logger.info("Updating default user credentials from '{}' to '{}'", request.getOldUserName(), request.getNewUserName());
        ApiResponseUtil<?> response = defaultUserService.updateCredentials(
                request.getOldUserName(),
                request.getNewUserName(),
                request.getNewPassword()
        );
        return ResponseEntity.ok(response);
    }
}
