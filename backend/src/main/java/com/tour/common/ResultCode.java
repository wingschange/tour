package com.tour.common;

public enum ResultCode {

    SUCCESS(0, "success"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    PARAM_ERROR(400, "Parameter Error"),
    SERVER_ERROR(500, "Internal Server Error");

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
