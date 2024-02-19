package com.hanyoonsoo.springtoy;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.ObjectMapperUtils;
import com.hanyoonsoo.springtoy.global.ResultActionsUtils;
import com.hanyoonsoo.springtoy.global.UserResponseSnippet;
import com.hanyoonsoo.springtoy.module.dto.Response;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetailService;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import com.hanyoonsoo.springtoy.module.service.UserService;
import com.hanyoonsoo.springtoy.stubdata.StubData;
import com.hanyoonsoo.springtoy.stubdata.WithMockCustomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getRequestPreProcessor;
import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/users";
    private final String EMAIL = "email@gmail.com";
    private final String SENDER_EMAIL = "hdbstn3055@gmail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private UserRepository userRepository;

    private UserDto.Patch patchDto;

    @BeforeEach
    void beforeEach(){
        UserDto.SignUp signUpDto = StubData.MockUser.getSignUpDto();
        userService.signUp(signUpDto);

        patchDto = StubData.MockUser.getPatchDto();
    }

    @AfterEach
    void afterEach(){
        userService.deleteUser(EMAIL);
    }

    @Test
    @DisplayName("회원 조회")
    public void getUserTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();


        ResultActions actions = ResultActionsUtils.getRequestWithToken(mockMvc, uri, accessToken, encryptedRefreshToken);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-user",
                        getResponsePreProcessor(),
                        UserResponseSnippet.getUserResponseSnippet()));
    }

    @Test
    @DisplayName("회원 업데이트")
    public void patchUserTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        //when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndToken(mockMvc, uri, json, accessToken, encryptedRefreshToken);

        //then
        SingleResponseDto<UserDto.Response> responseDto = ObjectMapperUtils.actionsSingleResponseToUserDto(actions);
        UserDto.Patch patchDto = StubData.MockUser.getPatchDto();

        UserDto.Response response = responseDto.getData();

        assertThat(response.getNickname()).isEqualTo(patchDto.getNickname());
        assertThat(response.getAddress()).isEqualTo(patchDto.getAddress());

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        UserResponseSnippet.getPatchSnippet(),
                        UserResponseSnippet.getUserResponseSnippet()));
    }

    @Test
    @DisplayName("이메일 코드 성공")
    public void emailCodeSuccessTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        String authCode = createCode();

        userService.sendCodeToEmailForTest(SENDER_EMAIL, authCode);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/emails/verifications")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithAuthCodeAndToken(mockMvc, uri, accessToken, encryptedRefreshToken, SENDER_EMAIL, authCode);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("email-verification-success",
                        getResponsePreProcessor(),
                        UserResponseSnippet.emailVerificationsResponseSnippet()));
    }

    @Test
    @DisplayName("이메일 코드 실패")
    public void emailCodeFailTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        String authCode = createCode();

        userService.sendCodeToEmailForTest(SENDER_EMAIL, authCode + "1");

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/emails/verifications")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithAuthCodeAndToken(mockMvc, uri, accessToken, encryptedRefreshToken, SENDER_EMAIL, authCode);

        //then
        actions
                .andExpect(status().isBadRequest())
                .andDo(document("email-verification-fail",
                        getResponsePreProcessor(),
                        UserResponseSnippet.emailVerificationsFailSnippet()));
    }

    private String createCode(){
        int length = 6;
        try{
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++){
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch(NoSuchAlgorithmException e){
            throw new BusinessLogicException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

}
