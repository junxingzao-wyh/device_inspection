package com.example.inspection.common;

import lombok.Data;

/**
 * 统一返回结果
 * code=200 表示成功，其余为业务或系统错误
 */
@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> ok() {
        return build(200, "success", null);
    }

    public static <T> Result<T> ok(T data) {
        return build(200, "success", data);
    }

    public static <T> Result<T> error(int code, String message) {
        return build(code, message, null);
    }

    private static <T> Result<T> build(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
