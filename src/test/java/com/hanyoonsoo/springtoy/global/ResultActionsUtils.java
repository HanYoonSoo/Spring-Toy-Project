package com.hanyoonsoo.springtoy.global;

import com.hanyoonsoo.springtoy.module.dto.OrderSearchDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ResultActionsUtils {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Refresh";
    private static final String BEARER_PREFIX = "Bearer ";

    public static ResultActions patchRequestWithToken(MockMvc mockMvc,
                                                                String url,
                                                                String accessToken,
                                                                String encryptedRefreshToken) throws Exception {

        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequestWithToken(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequest(MockMvc mockMvc, String url, String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequest(MockMvc mockMvc, String url) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

    public static ResultActions getRequest(MockMvc mockMvc, String url, String json) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print());
    }

    public static ResultActions postRequestWithAuthCodeAndToken(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken, String email, String code) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                .param("email", email)
                        .param("code", code))
                .andDo(print());
    }

    public static ResultActions patchRequestWithContentAndToken(MockMvc mockMvc, String url, String json, String accessToken, String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                        .content(json))
                .andDo(print());
    }


    public static ResultActions postRequestWithTokenAndJson(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken, String json) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                        .content(json))
                .andDo(print());
    }

    public static ResultActions deleteRequestWithToken(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndParam(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken, Long itemId, int count) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                        .param("itemId", String.valueOf(itemId))
                        .param("count", String.valueOf(count)))
                .andDo(print());
    }

    public static ResultActions getRequestWithTokenAndParam(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken, OrderSearchDto orderSearchDto) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                        .param("itemName", orderSearchDto.getItemName())
                        .param("orderStatus", orderSearchDto.getOrderStatus() == null ? null : String.valueOf(orderSearchDto.getOrderStatus())))
                .andDo(print());
    }

    public static ResultActions getRequestWithTokenAndParamAndPaging(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken, int limit) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken)
                        .param("limit", String.valueOf(limit)))
                .andDo(print());
    }

    public static ResultActions postRequestWithToken(MockMvc mockMvc, String url, String accessToken, String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }
}
