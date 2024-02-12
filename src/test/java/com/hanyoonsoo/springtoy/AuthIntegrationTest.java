package com.hanyoonsoo.springtoy;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.ResultActionsUtils;
import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.service.UserService;
import com.hanyoonsoo.springtoy.stubdata.StubData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.time.Duration;

import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/auth";
    private final String EMAIL = "email@gmail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    private String accessToken;

    @BeforeEach
    void beforeEach(){
        UserDto.SignUp signUp = StubData.MockUser.getSignUpDto();
        userService.signUp(signUp);
    }

    @AfterEach
    void afterEach(){
        userService.deleteUser(EMAIL);
        redisService.deleteValues(accessToken);
    }

    @Test
    @DisplayName("로그아웃")
    public void logoutTest() throws Exception{
        //given
        User testUser = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(testUser);
        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        redisService.setValues(EMAIL, refreshToken, Duration.ofMillis(10000));

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/logout")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequestWithToken(mockMvc, uri, accessToken, encryptedRefreshToken);

        //then
        String redisRefreshToken = redisService.getValues(EMAIL);
        String logout = redisService.getValues(accessToken);

        assertThat(redisRefreshToken).isEqualTo("false");
        assertThat(logout).isEqualTo("logout");
        actions
                .andExpect(status().isNoContent())
                .andDo(document("logout"));
    }

    @Test
    @DisplayName("Access token 재발급 성공")
    public void accessTokenReissureSuccessTest() throws Exception{
        //given
        User findUser = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(findUser);
        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = tokenDto.getRefreshToken();
        redisService.setValues(EMAIL, refreshToken, Duration.ofMillis(10000));
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mockMvc, uri, encryptedRefreshToken);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("access-token-reissue-success"));
    }

    @Test
    @DisplayName("Refresh token 불일치로 Access token 재발급 실패")
    void accessTokenReissrueFailTest() throws Exception {
        // given
        User findUser = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(findUser);
        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = tokenDto.getRefreshToken();
        String failRefreshToken = refreshToken + "fail";
        redisService.setValues(EMAIL, failRefreshToken, Duration.ofMillis(10000));
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mockMvc, uri, encryptedRefreshToken);

        // then
        actions
                .andExpect(status().is(404))
                .andDo(document("reissue-fail-by-token-not-same",
                        getResponsePreProcessor(),
                        getFieldErrorSnippetsLong()));
    }

    private Snippet getFieldErrorSnippetsLong() {
        return new Snippet(
                fieldWithPath("error").description("에러 코드"),
                fieldWithPath("error_description").description("에러 상세 설명")
        ) {
            @Override
            public void document(Operation operation) throws IOException {

            }
        };
    }
}

