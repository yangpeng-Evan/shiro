package com.yp.service;

import com.yp.SpringTests;
import com.yp.entity.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

public class ItemServiceTest extends SpringTests {

    @Autowired
    private ItemService itemService;
    @Test
    public void findItemByNameLimit() {
    }

    @Test
    public void addItem() {
        Item item = new Item();
        item.setName("测试添加商品信息");
        item.setPrice(new BigDecimal(100));
        //item.setCreated(new Date());
        item.setDescription("这个是测试添加的商品，可能会没有图片信息，我就随便填写一个得了");
        item.setPic("http://xxxxxxxx.baidu.com");
        item.setProductionDate(new Date());
        itemService.addItem(item);
    }

    @Test
    public void deleteItem(){
        itemService.deleteItem(18);
    }
}