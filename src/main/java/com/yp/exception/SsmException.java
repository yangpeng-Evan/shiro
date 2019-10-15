package com.yp.exception;

import com.yp.enums.ExceptionInfoEnum;
import lombok.Getter;

@Getter
public class SsmException extends  RuntimeException{

    private Integer code;

    public SsmException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public SsmException(ExceptionInfoEnum infoEnum) {
        super(infoEnum.getMsg());
        this.code = infoEnum.getCode();
    }
}
