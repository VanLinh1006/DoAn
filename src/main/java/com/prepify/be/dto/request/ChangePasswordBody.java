package com.prepify.be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordBody {
    @JsonProperty("old-password")
    @NotNull(message = "Old password is required")
    @NotEmpty(message = "Old password is required")
    private String oldPassword;

    @JsonProperty("new-password")
    @NotNull(message = "New password is required")
    @NotEmpty(message = "New password is required")
    private String newPassword;
}
