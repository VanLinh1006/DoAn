package com.prepify.be.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateInfoBody {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("phone")
    private String phone = null;
}
