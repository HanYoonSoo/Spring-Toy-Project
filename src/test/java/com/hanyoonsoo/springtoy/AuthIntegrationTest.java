package com.hanyoonsoo.springtoy;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.ObjectMapperUtils;
import com.hanyoonsoo.springtoy.global.ResultActionsUtils;
import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.LoginResponse;
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

import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getRequestPreProcessor;
import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

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


    @BeforeEach
    void beforeEach(){
        UserDto.SignUp signUp = StubData.MockUser.getSignUpDto();
        userService.signUp(signUp);
    }

    @AfterEach
    void afterEach(){
        userService.deleteUser(EMAIL);
    }

    @Test
    @DisplayName("로그인 성공")
    public void loginSuccessTest() throws Exception{
        //given
        LoginDto loginSuccessDto = StubData.MockUser.getLoginSuccessDto();
        LoginResponse expectedResponseDto = StubData.MockUser.getLoginResponseDto();

        //when
        String uri  = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginSuccessDto);
        ResultActions actions = ResultActionsUtils.getRequest(mockMvc, uri, json);

        //then
        LoginResponse responseDto = ObjectMapperUtils.actionsSingleResponseToLoginDto(actions);
        assertThat(expectedResponseDto.getEmail()).isEqualTo(responseDto.getEmail());
        assertThat(expectedResponseDto.getNickName()).isEqualTo(responseDto.getNickName());

        actions
                .andExpect(status().isOk())
                .andDo(document("login-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()));
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailTest() throws Exception {
        // given
        LoginDto loginFailDto = StubData.MockUser.getLoginFailDto();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginFailDto);
        ResultActions actions = ResultActionsUtils.getRequest(mockMvc, uri, json);

        // then
        actions
                .andExpect(status().isUnauthorized())
                .andDo(document("login-fail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()));
    }
    @Test
    @DisplayName("로그아웃")
    public void logoutTest() throws Exception{
        //given
        User testUser = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(testUser);
        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
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
                .andDo(document("reissue-fail-by-token-not-same"));
    }

    @Test
    @DisplayName("Header에 Refresh token이 존재하지 않으면 Access token 재발급 실패")
    void accessTokenReissrueFailTest2() throws Exception {
        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mockMvc, uri);

        // then
        actions
                .andExpect(status().is(404))
                .andDo(document("reissue-fail-by-no-refresh-token-in-header",
                        getResponsePreProcessor()));
    }

    @Test
    @DisplayName("중복 로그인 방지")
    public void duplicateLoginTest() throws Exception{
        //given
        LoginDto loginSuccessDto1 = StubData.MockUser.getLoginSuccessDto();
        LoginDto loginSuccessDto2 = StubData.MockUser.getLoginSuccessDto();
        LoginResponse expectedResponseDto = StubData.MockUser.getLoginResponseDto();

        //when
        String uri  = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginSuccessDto1);
        ResultActionsUtils.getRequest(mockMvc, uri, json);
        ResultActions actions;

        uri  = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        json = ObjectMapperUtils.asJsonString(loginSuccessDto2);
        actions = ResultActionsUtils.getRequest(mockMvc, uri, json);

        //then
        LoginResponse responseDto = ObjectMapperUtils.actionsSingleResponseToLoginDto(actions);
        assertThat(expectedResponseDto.getEmail()).isEqualTo(responseDto.getEmail());
        assertThat(expectedResponseDto.getNickName()).isEqualTo(responseDto.getNickName());

        actions
                .andExpect(status().isOk())
                .andDo(document("login-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()));
    }

    private Snippet getFieldErrorSnippetsLong() {
        return responseFields(
                fieldWithPath("error").description("에러 코드"),
                fieldWithPath("error_description").description("에러 상세 설명")
        );
    }
}

