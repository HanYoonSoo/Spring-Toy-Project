package com.hanyoonsoo.springtoy.global.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanyoonsoo.springtoy.module.dto.ImageDto;
import com.hanyoonsoo.springtoy.module.dto.item.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.auth.LoginResponse;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.dto.user.UserDto;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ObjectMapperUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SingleResponseDto<UserDto.Response> actionsSingleResponseToUserDto(ResultActions actions) throws Exception {
        String response = new String(actions.andReturn().getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

        System.out.println(response);

        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, new TypeReference<SingleResponseDto<UserDto.Response>>() {
        });
    }

    public static LoginResponse actionsSingleResponseToLoginDto(ResultActions actions) throws Exception {
        String response = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, LoginResponse.class);
    }

    public static SingleResponseDto<ItemDto> actionsSingleResponseToItemDto(ResultActions actions) throws UnsupportedEncodingException, JsonProcessingException {
        String response = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, new TypeReference<SingleResponseDto<ItemDto>>() {});
    }

    public static SingleResponseDto<List<ImageDto>> actionsSingleResponseToImageDto(ResultActions actions) throws JsonProcessingException, UnsupportedEncodingException {
        String response = actions.andReturn().getResponse().getContentAsString();
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, new TypeReference<SingleResponseDto<List<ImageDto>>>() {});
    }
}
