package com.prepify.be.config.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Base64;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class JWTPayload {
    private String aud;
    private Long exp;
    private String jti;
    private Long iat;
    private String iss;
    private String sub;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("role")
    private String role;

    public static JWTPayload parserFromJWT(String token) {
        try {
            String[] arr = token.split("\\.");
            byte[] payload = Base64.getDecoder().decode(arr[1]);
            return new ObjectMapper().readValue(payload, JWTPayload.class);
        } catch (Exception ex) {
            return null;
        }
    }

}
