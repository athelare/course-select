package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.StudentAccountEntity;
import com.ljy.courseselect.repository.UserDao;
import com.ljy.courseselect.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public StudentAccountEntity findById(String username) {
        return userDao.findByStudentId(username);
    }

    @Override
    public boolean UserRegister(String studentId, String realName, String password) {
        if(userDao.findByStudentId(studentId)!=null)
            return false;
        StudentAccountEntity stu = new StudentAccountEntity();
        stu.setName(realName);
        stu.setStudentId(studentId);
        stu.setPassword(password);
        userDao.saveAndFlush(stu);
        return true;
    }

    @Override
    public StudentAccountEntity UserVerify(String username, String password) {
        StudentAccountEntity stu = userDao.findByStudentId(username);
        if(stu == null || !password.equals(stu.getPassword()))
            return null;
        else return stu;
    }

    @Override
    public boolean ChangePassword(String username, String oriPassword, String newPassword) {
        StudentAccountEntity stu = userDao.findByStudentId(username);
        if(stu == null || !stu.getPassword().equals(oriPassword)){
            return false;
        }else{
            stu.setPassword(newPassword);
            userDao.saveAndFlush(stu);
            return true;
        }
    }
}
