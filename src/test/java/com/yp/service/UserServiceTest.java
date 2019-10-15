package com.yp.service;

import com.yp.SpringTests;
import com.yp.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceTest extends SpringTests {

    @Autowired
    private UserService userService;
    @Test
    public void checkUsername() {
        userService.checkUsername("admin");
    }

    @Test
    public void register(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setPhone("13333333333");
        userService.register(user);
    }

    @Test
    public void login(){
        User user = userService.login("zhangsan", "123456");
        System.out.println(user);
    }
}