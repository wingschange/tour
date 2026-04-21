package com.tour.common;

/**
 * 业务状态码枚举
 *
 * <p>约定：code=0 为成功，其余均为失败</p>
 */
public enum ResultCode {

    /** 成功 */
    SUCCESS(0, "success"),

    /** 未登录或 Token 无效 */
    UNAUTHORIZED(401, "未登录或 Token 已过期"),

    /** 权限不足 */
    FORBIDDEN(403, "权限不足"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 请求参数错误 */
    PARAM_ERROR(400, "请求参数错误"),

    /** 服务器内部错误 */
    SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
