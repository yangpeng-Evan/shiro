package com.yp.mapper;

import com.yp.entity.Item;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author yangpeng
 */
public interface ItemMapper extends Mapper<Item> {
    //根据商品名称查询商品数据
    List<Item> findItemByName(@Param("name") String name);
}
