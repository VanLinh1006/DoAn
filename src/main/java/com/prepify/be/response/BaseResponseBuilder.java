package com.prepify.be.response;

public class BaseResponseBuilder {
    public static <T> BaseResponse<T> error(String message) {
        return error(500, message);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static <T> BaseResponse<T> error(int code, String message, T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setSuccess(true);
        response.setCode(200);
        response.setMessage("Thành công!");
        response.setData(data);
        return response;
    }
}
