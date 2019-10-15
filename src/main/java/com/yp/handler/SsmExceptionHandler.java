package com.yp.handler;

import com.yp.exception.SsmException;
import com.yp.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 *  异常处理器.
 */
@ControllerAdvice
public class SsmExceptionHandler {


    @ExceptionHandler({SsmException.class})
    @ResponseBody
    public ResultVO ssmException(SsmException ex){
        return new ResultVO(ex.getCode(),ex.getMessage(),null);
    }

}
