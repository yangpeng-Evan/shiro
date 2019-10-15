package com.yp.controller;

import com.github.pagehelper.PageInfo;
import com.yp.entity.Item;
import com.yp.enums.ExceptionInfoEnum;
import com.yp.exception.SsmException;
import com.yp.service.ItemService;
import com.yp.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author yangpeng
 * 商品模块controller
 */
@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Value("${pic.maxSize}")
    private Long picMaxSize;

    @Value("${pic.types}")
    private String types;
//    //映射商品列表页面
//    @GetMapping("/list-ui")
//    public String itemListUI(){
//        return "item/item_list";
//    }

    //商品信息分页显示
    @GetMapping("/list")
    public String itemList(String name,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "5") Integer pageSize,
                           Model model){

//        3. 调用service查询数据,并获得PageInfo.
        PageInfo<Item> pageInfo = itemService.findItemByNameLimit(name, page, pageSize);
//        4. 将数据放到request域中.
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
//        5. 转发到item_list页面.
        return "item/item_list";
    }

    //映射添加商品页面
    @GetMapping("/add-ui")
    public String addUI(){
        return "item/item_add";
    }

    //添加商品
    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(MultipartFile picFile,
                        @Valid Item item, BindingResult bindingResult, HttpServletRequest request) throws IOException {
//        3. 在controller的方法参数上使用MultipartFile对象接收文件上传项.
//        4. 接收普通表单项Item item,并校验.
//        5. 文件上传项.
        if(picFile==null || picFile.getSize()==0){
            log.info("【添加商品】 商品图片为必传项，不能为空");
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"商品图片为必传项，不能为空");
        }
//        5.1 大小判断.
        if(picFile.getSize()>picMaxSize){
            log.info("【添加商品】 上传的图片过大！！！picFile.getSize={}",picFile.getSize());
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"上传图片过大！！！");
        }
//        5.2 类型判断.
        List<String> list = Arrays.asList(types.split(","));
        String fileName = picFile.getOriginalFilename();
        String typesName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if(!list.contains(typesName)){
            log.info("【添加商品】 上传的文件类型不正确！！！typesName={}",typesName);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"上传您的文件类型不正确！！！");
        }
//        5.3 是否损坏.
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if(image == null){
            log.info("【添加商品】 上传您的图片已损坏！！！");
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"上传的图片已损坏！！！");
        }
//        5.4 新名字.
        String newName = UUID.randomUUID().toString() + "." + typesName;
//        5.5 保存到本地.
        String path = request.getRealPath("/")+"static/images/"+newName;
        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        IOUtils.copy(picFile.getInputStream(),new FileOutputStream(file));
//        5.6 将图片的访问路径设置到item对象中.
        String pic = request.getContextPath() + "/static/images/" + newName;
        item.setPic(pic);
//        6. 校验普通表单项.
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【添加商品】 添加商品参数异常！！！msg={}",msg);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),msg);
        }
//        7. 调用serivce保存.
        itemService.addItem(item);
//        8. 响应数据.
        return new ResultVO(0,"成功",null);
    }

    //删除商品
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResultVO deleteItem(@PathVariable Integer id){
        itemService.deleteItem(id);
        if(id==null){
            log.info("【删除商品】 商品id为空！！！id={}",id);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"商品id不能为空！！！");
        }
        return new ResultVO(0,"成功",null);
    }
}
