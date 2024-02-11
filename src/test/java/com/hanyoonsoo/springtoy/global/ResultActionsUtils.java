package com.hanyoonsoo.springtoy.global;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
}
