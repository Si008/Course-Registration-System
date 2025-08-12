package com.example.Course.Registration.System.DTO;

public class UpdateDefaultRequest {

        private String oldUserName;
        private String newUserName;
        private String newPassword;

    public String getOldUserName()
    {
        return oldUserName;
    }

    public void setOldUserName(String oldUserName) {
        this.oldUserName = oldUserName;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UpdateDefaultRequest(String oldUserName, String newUserName, String newPassword) {
        this.oldUserName = oldUserName;
        this.newUserName = newUserName;
        this.newPassword = newPassword;
    }

    public UpdateDefaultRequest() {
    }
}


