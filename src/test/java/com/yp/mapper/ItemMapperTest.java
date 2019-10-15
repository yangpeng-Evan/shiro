package com.yp.mapper;

import com.github.pagehelper.PageHelper;
import com.yp.SpringTests;
import com.yp.entity.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ItemMapperTest extends SpringTests {

    @Autowired
    private ItemMapper itemMapper;
    @Test
    public void findItemByName() {
        PageHelper.startPage(1,2);
        List<Item> itemList = itemMapper.findItemByName("联想");
        for (Item item : itemList) {
            System.out.println(item);
        }
    }
}