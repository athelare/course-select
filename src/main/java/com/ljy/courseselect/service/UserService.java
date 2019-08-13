package com.ljy.courseselect.service;

import com.ljy.courseselect.domain.StudentAccountEntity;

public interface UserService {
    StudentAccountEntity findById(String username);
    boolean UserRegister(String studentId,String realName, String password);
    StudentAccountEntity UserVerify(String username, String password);
    boolean ChangePassword(String username,String oriPassword,String newPassword);
}
