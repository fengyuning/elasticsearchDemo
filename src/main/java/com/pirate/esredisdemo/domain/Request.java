package com.pirate.esredisdemo.domain;

import com.pirate.esredisdemo.utils.RequestUtils;
import lombok.Data;

/**
 * 响应结果
 *
 * @author fyn
 * @version 1.0 2018/08/24
 */
@Data
public class Request {
    private int code;
    private String msg;
    private Object date;

    public Request(RequestUtils.RequestEnum requestEnum) {
        this.code = requestEnum.getCode();
        this.msg = requestEnum.getMsg();
    }
}