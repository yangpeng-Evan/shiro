package com.yp.controller;

import static com.yp.constant.SsmConstant.*;
import com.yp.entity.User;
import com.yp.enums.ExceptionInfoEnum;
import com.yp.exception.SsmException;
import com.yp.service.UserService;
import com.yp.util.SendSMSUtil;
import com.yp.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author yangpeng
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SendSMSUtil sendSMSUtil;
    //转发到注册页面
    @GetMapping("/register-ui")
    public String registerUI(){
        return "user/register";
    }
    //校验用户名是否可用
    @PostMapping("/check-username")
    @ResponseBody
    public ResultVO checkUsername(@RequestBody User user){
        System.out.println(user.getUsername());
        userService.checkUsername(user.getUsername());
        //用户名可用
        return new ResultVO(0,"成功",null);
    }

    @PostMapping("/send-sms")
    @ResponseBody
    public ResultVO sendSMS(String phone, HttpSession session){
//        3. 校验参数.
        System.out.println(phone);
        if(phone==null && phone.length()!=11){
            log.error("【校验用户发送短信的手机号】用户发送短信的手机号不合法！！！phone={}",phone);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"用户输入的手机号不合法！！！");
        }
        //4. 调用工具类中的sendSMS方法. -> 返回ResultVO.
        ResultVO vo = sendSMSUtil.sendSMS(phone,session);
//        5. 返回ResultVO.
        return vo;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultVO register(@Valid User user, BindingResult bandingResult, String registerCode,HttpSession session){
        System.out.println(session.getAttribute(USER_CODE));
//        2. 校验验证码是否正确.
        if(!registerCode.equals(session.getAttribute(USER_CODE))){
            log.error("【执行注册】验证码不正确！！！registerCode={}",registerCode);
            throw new SsmException(ExceptionInfoEnum.CAPTCHA_ERROR.getCode(),ExceptionInfoEnum.CAPTCHA_ERROR.getMsg());
        }
        //3.校验手机号是否合法
        if(bandingResult.hasErrors()){
            String msg = bandingResult.getFieldError().getDefaultMessage();
            log.info("【执行注册】 参数不正确！！！msg={},user={}",msg,user);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),ExceptionInfoEnum.PARAM_ERROR.getMsg());
        }
//        4. 调用service执行注册.
        userService.register(user);
//        5. 响应数据.
        return new ResultVO(0,"成功",null);
    }

    @GetMapping("/login-ui")
    public String loginUI(){
        return "user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(String username,String password,HttpSession session){
        //校验参数
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            log.info("【登录功能】 用户名或密码为空！！！username={},password={}",username,password);
            throw new SsmException(ExceptionInfoEnum.USER_USERNAMEANDPASSWORD_ERROR.getCode(),"用户名和密码不能为空！！！");
        }
        //调用service方法查询用户信息
        User user = userService.login(username, password);
        //将查询到的用户信息设置到session域中
        session.setAttribute(USER_INFO,user);
        log.info("【登录功能】 用户名和密码校验正确！！！");
        return new ResultVO(0,"成功",null);
    }
}
