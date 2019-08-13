package com.ljy.courseselect.controller;

import com.ljy.courseselect.domain.StudentAccountEntity;
import com.ljy.courseselect.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping("/register")
    boolean UserRegister(@RequestParam String username,@RequestParam String password,@RequestParam String realName){
        if(null == userService.findById(username)){
            userService.UserRegister(username,realName,password);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("/userLogin")
    boolean UserLogin(@RequestParam String username,@RequestParam String password, HttpServletRequest request){
        StudentAccountEntity stu = userService.findById(username);
        if(null != stu && stu.getPassword().equals(password)){
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",stu);
            return true;
        }else return false;
    }

    @ResponseBody
    @RequestMapping("/changePwd")
    boolean changePassword(@RequestParam String username, @RequestParam String oriPassword, @RequestParam String newPassword){
        return userService.ChangePassword(username,oriPassword,newPassword);
    }


}
