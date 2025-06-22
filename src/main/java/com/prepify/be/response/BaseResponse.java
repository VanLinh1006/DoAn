package com.prepify.be.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int code;
    @JsonProperty("rid")
    private String requestId;
}

