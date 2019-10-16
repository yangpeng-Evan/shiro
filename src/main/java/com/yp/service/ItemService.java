package com.yp.service;

import com.github.pagehelper.PageInfo;
import com.yp.entity.Item;
import com.yp.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yangpeng
 */
public interface ItemService {

    /**
     * 分页查询商品列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Item> findItemByNameLimit(String name,Integer pageNum,Integer pageSize);

    /**
     * 添加商品方法
     */
    void addItem(Item item);

    /**
     * 删除方法
     * @param id
     */
    void deleteItem(Integer id);

    /**
     * 根据Id查询商品信息
     */
    Item FindItemById(Integer id);
}
