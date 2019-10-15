package com.yp.service;

import com.yp.entity.User;

/**
 * @author yangpeng
 */
public interface UserService {

    //校验用户名是否可用
    void checkUsername(String username);
    //注册接口
    void register(User user);

    //执行登录
    User login(String username,String password);

}
