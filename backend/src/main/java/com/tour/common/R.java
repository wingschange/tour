package com.tour.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应包装类
 *
 * <p>所有接口均返回此格式：
 * <pre>
 * {
 *   "code": 0,        // 0=成功，非 0=失败
 *   "message": "success",
 *   "data": {}        // 业务数据，失败时为 null
 * }
 * </pre>
 * </p>
 *
 * @param <T> 业务数据类型
 */
@Data
public class R<T> implements Serializable {

    /** 业务状态码，0 表示成功 */
    private int code;

    /** 响应描述信息 */
    private String message;

    /** 响应业务数据 */
    private T data;

    private R() {}

    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功（无数据） */
    public static <T> R<T> success() {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /** 成功（含数据） */
    public static <T> R<T> success(T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /** 失败（自定义消息，状态码默认 500） */
    public static <T> R<T> fail(String message) {
        return new R<>(ResultCode.SERVER_ERROR.getCode(), message, null);
    }

    /** 失败（自定义状态码和消息） */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /** 失败（使用预定义错误码） */
    public static <T> R<T> fail(ResultCode resultCode) {
        return new R<>(resultCode.getCode(), resultCode.getMessage(), null);
    }
}
