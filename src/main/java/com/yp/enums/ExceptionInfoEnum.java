package com.yp.enums;


import lombok.Getter;

@Getter
public enum ExceptionInfoEnum {
    PARAM_ERROR(11,"参数不合法!!"),
    CAPTCHA_ERROR(5,"验证码错误!!"),
    USER_REGISTER_ERROR(31,"注册账号失败!!"),
    USER_USERNAMEANDPASSWORD_ERROR(32,"用户名或密码不正确!!"),
    ITEM_ADD_ERROR(51,"添加商品信息失败！！！");

    private Integer code;

    private String msg;

    ExceptionInfoEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
