package com.hanyoonsoo.springtoy;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.ResultActionsUtils;
import com.hanyoonsoo.springtoy.global.UserResponseSnippet;
import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetailService;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import com.hanyoonsoo.springtoy.module.service.UserService;
import com.hanyoonsoo.springtoy.stubdata.StubData;
import com.hanyoonsoo.springtoy.stubdata.WithMockCustomUser;
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
import org.springframework.web.util.UriComponentsBuilder;

import static com.hanyoonsoo.springtoy.global.ApiDocumentUtils.getResponsePreProcessor;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/users";
    private final String EMAIL = "email@gmail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach(){
        UserDto.SignUp signUpDto = StubData.MockUser.getSignUpDto();
        userService.signUp(signUpDto);
    }

    @AfterEach
    void afterEach(){
        userService.deleteUser(EMAIL);
    }

    @Test
    @DisplayName("회원 조회")
    public void getMemberTest() throws Exception{
        //given
        User user = userService.findUserByEmail("email@gmail.com");
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

}
