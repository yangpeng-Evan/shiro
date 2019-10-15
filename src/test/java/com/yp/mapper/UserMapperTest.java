package com.yp.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yp.SpringTests;
import com.yp.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class UserMapperTest extends SpringTests {

    @Autowired
    private UserMapper userMapper;
    //测试查询全部用户信息
    @Test
    public void findAllUsers(){
        PageHelper.startPage(1, 3);
        List<User> list = userMapper.selectAll();
        PageInfo<User> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getList());
    }
}