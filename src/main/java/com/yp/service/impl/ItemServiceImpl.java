package com.yp.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yp.entity.Item;
import com.yp.enums.ExceptionInfoEnum;
import com.yp.exception.SsmException;
import com.yp.mapper.ItemMapper;
import com.yp.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yangpeng
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Override
    public PageInfo<Item> findItemByNameLimit(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Item> list = itemMapper.findItemByName(name);
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    @Transactional
    public void addItem(Item item) {
        int count = itemMapper.insertSelective(item);
        if(count==0){
            log.info("【添加商品信息功能】 添加商品信息失败！！！ item={}",item);
            throw new SsmException(ExceptionInfoEnum.ITEM_ADD_ERROR.getCode(),"添加商品信息失败！！！");
        }
    }

    @Override
    public void deleteItem(Integer id) {
//        Item item = new Item();
//        item.setId(id);
        int count = itemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Item FindItemById(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if(item == null){
            log.error("【修改商品】 参数不正确！id={}",id);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"参数不正确!");
        }
        return item;
    }

    @Override
    public void updateItemById(Item item) {
        int count = itemMapper.updateByPrimaryKeySelective(item);
        if(count != 1){
            log.error("【修改商品信息】 修改商品信息失败！！！item",item);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"商品信息参数错误");
        }
    }
}
