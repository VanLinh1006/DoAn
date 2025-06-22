package com.prepify.be.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prepify.be.entities.User;
import lombok.Data;

@Data
public class UserResponse extends User {
    @JsonProperty("access_token")
    private String accessToken;
}
