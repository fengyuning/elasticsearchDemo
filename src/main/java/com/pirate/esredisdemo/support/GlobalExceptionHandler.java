package com.pirate.esredisdemo.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pirate.esredisdemo.domain.Request;
import com.pirate.esredisdemo.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理程序
 *
 * @author fyn
 * @version 1.0 2018/08/27
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(Throwable t) {
        return getExceptionRequest(t);
    }

    private Request getExceptionRequest(Throwable t) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        JSONObject json = new JSONObject();
        json.put("msg", t.getMessage());
        json.put("type", t.getClass().getName());
        json.put("detail", stackTrace);
        log.error(stackTrace[0] + " exception:{}", json);
        return RequestUtils.fail(t.getMessage());
    }
}
