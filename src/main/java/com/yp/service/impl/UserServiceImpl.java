package com.yp.service.impl;

import com.yp.constant.SsmConstant;
import com.yp.entity.User;
import com.yp.enums.ExceptionInfoEnum;
import com.yp.exception.SsmException;
import com.yp.mapper.UserMapper;
import com.yp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author yangpeng
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    //校验用户名
    @Override
    public void checkUsername(String username) {
        //封装参数
        User param = new User();
        param.setUsername(username);
        //调用mapper查询
        int count = userMapper.selectCount(param);
        //判断返回结果
        if (count!=0){
            log.info("【校验用户名是否存在】用户名已存在！！！username={}",username);
            throw new SsmException(1,"用户名已被占用！！！");
        }
    }

    /**
     * 执行注册
     * @param user
     */
    @Override
    @Transactional
    public void register(User user) {
//        1. 生成一个盐
        String salt = UUID.randomUUID().toString();
//        2. 对密码进行MD5和加盐.			->		shiro-core-1.4.0
        String newPassword = new Md5Hash(user.getPassword(),salt,SsmConstant.HASH_ITERATIONS).toString();
//        3. 封装数据.
        user.setPassword(newPassword);
        user.setSalt(salt);
//        4. 执行添加.
        int count = userMapper.insertSelective(user);
//        5. 判断注册是否成功.
        if(count!=1){
            log.error("【用户注册】用户注册失败！！！user={}",user);
            throw new SsmException(ExceptionInfoEnum.USER_REGISTER_ERROR.getCode(),ExceptionInfoEnum.USER_REGISTER_ERROR.getMsg());
        }
    }

    @Override
    public User login(String username, String password) {
        //根据用户名查询用户信息
        User param = new User();
        param.setUsername(username);
        //param.setPassword(password);
        User user = userMapper.selectOne(param);
        if(user==null){
            log.info("【登录功能】 用户名或密码错误-用户名！！！username={},password={}",username,password);
            throw new SsmException(ExceptionInfoEnum.USER_USERNAMEANDPASSWORD_ERROR);
        }
        String newPassword = new Md5Hash(password,user.getSalt(),SsmConstant.HASH_ITERATIONS).toString();
        if(!user.getPassword().equals(newPassword)){
            log.info("【登录功能】 用户名或密码错误-密码！！！user={},username={},password={}",user,username,password);
            throw new SsmException(ExceptionInfoEnum.USER_USERNAMEANDPASSWORD_ERROR);
        }
        return user;
    }
}
