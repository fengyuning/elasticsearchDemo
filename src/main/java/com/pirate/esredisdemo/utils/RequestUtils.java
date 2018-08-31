package com.pirate.esredisdemo.utils;

import com.pirate.esredisdemo.domain.Request;

/**
 * 响应结果工具
 *
 * @author fyn
 * @version 1.0 2018/08/24
 */
public class RequestUtils {
    public enum RequestEnum {
        SUCCESS(200, "success"),
        FAIL(400, "fail");

        int code;
        String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        RequestEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static Request success() {
        return new Request(RequestEnum.SUCCESS);
    }

    public static Request success(Object data) {
        Request request = new Request(RequestEnum.SUCCESS);
        request.setDate(data);
        return request;
    }

    public static Request fail() {
        return new Request(RequestEnum.FAIL);
    }

    public static Request fail(Object data) {
        Request request = new Request(RequestEnum.FAIL);
        request.setDate(data);
        return request;
    }
}
