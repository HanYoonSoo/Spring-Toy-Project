package com.hanyoonsoo.springtoy.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanyoonsoo.springtoy.module.dto.LoginResponse;
import org.apache.catalina.connector.Response;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

public class ObjectMapperUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response actionsSingleResponseToUserDto(ResultActions actions) throws Exception {
        String response = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, Response.class);
    }

    public static LoginResponse actionsSingleResponseToLoginDto(ResultActions actions) throws Exception {
        String response = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, LoginResponse.class);
    }
}
