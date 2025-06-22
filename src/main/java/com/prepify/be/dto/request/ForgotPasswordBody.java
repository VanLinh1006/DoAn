package com.prepify.be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordBody {
    @JsonProperty("username")
    @NotNull(message = "username is required")
    @NotEmpty(message = "username is required")
    private String username;
}
