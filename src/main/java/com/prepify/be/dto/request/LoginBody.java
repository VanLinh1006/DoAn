package com.prepify.be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginBody {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
