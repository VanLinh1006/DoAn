package com.prepify.be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordBody {
    @JsonProperty("password")
    @NotNull(message = "password is required")
    @NotEmpty(message = "password is required")
    private String password;
}
