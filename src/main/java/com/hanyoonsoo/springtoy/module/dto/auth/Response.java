package com.hanyoonsoo.springtoy.module.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Response {

    @JsonProperty
    private int status;

    @JsonProperty
    private String message;
}
