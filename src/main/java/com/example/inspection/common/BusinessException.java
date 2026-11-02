package com.example.inspection.common;

/**
 * 业务异常：用于状态机校验失败、权限不足等可预期场景
 */
public class BusinessException extends RuntimeException {

    /** 业务错误码，默认 400 */
    private final int code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
