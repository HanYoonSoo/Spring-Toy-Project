package com.hanyoonsoo.springtoy.integration;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.snippets.ItemResponseSnippet;
import com.hanyoonsoo.springtoy.global.adapter.ObjectMapperUtils;
import com.hanyoonsoo.springtoy.global.adapter.ResultActionsUtils;
import com.hanyoonsoo.springtoy.module.dto.item.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.dto.user.UserDto;
import com.hanyoonsoo.springtoy.module.dto.auth.TokenDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.encryption.AES128Config;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.service.ItemService;
import com.hanyoonsoo.springtoy.module.service.UserService;
import com.hanyoonsoo.springtoy.stubdata.StubData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import static com.hanyoonsoo.springtoy.global.adapter.ApiDocumentUtils.getRequestPreProcessor;
import static com.hanyoonsoo.springtoy.global.adapter.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/items";
    private final String EMAIL = "email@gmail.com";

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @BeforeEach
    void beforeEach(){
        UserDto.SignUp signUpDto = StubData.MockUser.getSignUpDto();
        userService.signUp(signUpDto);

    }

    @Test
    @DisplayName("Book 생성 테스트")
    public void createBook() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/new/book")
                .build().toUri().toString();

        String json = ObjectMapperUtils.asJsonString(bookDto);

        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndJson(mockMvc, uri, accessToken, encryptedRefreshToken, json);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("new-book",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.newBookSnippet()));
    }

    @Test
    @DisplayName("Album 생성 테스트")
    public void createAlbum() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.AlbumRequestDto albumDto = StubData.MockItem.getAlbumDto();
        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/new/album")
                .build().toUri().toString();

        String json = ObjectMapperUtils.asJsonString(albumDto);

        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndJson(mockMvc, uri, accessToken, encryptedRefreshToken, json);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("new-album",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.newAlbumSnippet()));
    }

    @Test
    @DisplayName("Movie 생성 테스트")
    public void createMovie() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.MovieRequestDto movieDto = StubData.MockItem.getMovieDto();

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/new/movie")
                .build().toUri().toString();

        String json = ObjectMapperUtils.asJsonString(movieDto);

        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndJson(mockMvc, uri, accessToken, encryptedRefreshToken, json);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("new-movie",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.newMovieSnippet()));
    }

    @Test
    @DisplayName("Item 정보 받아오기")
    public void getItems() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        createItems();

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequestWithToken(mockMvc, uri, accessToken,  encryptedRefreshToken);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-items",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.getItemsSnippet()));
    }

    @Test
    @DisplayName("Item 수정 테스트")
    public void updateItem() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.MovieRequestDto movieDto = StubData.MockItem.getMovieDto();
        ItemDto.MovieDto newMovie = itemService.saveMovie(movieDto);

        ItemRequestDto itemDto = StubData.MockItem.updateItemDto();

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + newMovie.getItemId() + "/edit")
                .build().toUri().toString();

        String json = ObjectMapperUtils.asJsonString(itemDto);

        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndJson(mockMvc, uri, accessToken, encryptedRefreshToken, json);

        //then
        SingleResponseDto result = ObjectMapperUtils.actionsSingleResponseToItemDto(actions);
        ItemDto response = (ItemDto) result.getData();

        assertThat(response.getName()).isEqualTo(itemDto.getName());
        assertThat(response.getPrice()).isEqualTo(itemDto.getPrice());
        assertThat(response.getStockQuantity()).isEqualTo(itemDto.getStockQuantity());
        actions
                .andExpect(status().isOk())
                .andDo(document("update-item",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.updateItemSnippet()));
    }

    @Test
    @DisplayName("Item 삭제 테스트")
    public void deleteItem() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.MovieRequestDto movieDto = StubData.MockItem.getMovieDto();
        ItemDto.MovieDto newMovie = itemService.saveMovie(movieDto);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + newMovie.getItemId())
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.deleteRequestWithToken(mockMvc, uri, accessToken, encryptedRefreshToken);

        //then

        assertThrows(BusinessLogicException.class, () -> itemService.deleteItem(newMovie.getItemId()));
        actions
                .andExpect(status().isOk())
                .andDo(document("delete-item",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ItemResponseSnippet.deleteItemSnippet()));
    }

    private void createItems() {
        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();

        ItemRequestDto.AlbumRequestDto albumDto = StubData.MockItem.getAlbumDto();

        ItemRequestDto.MovieRequestDto movieDto = StubData.MockItem.getMovieDto();

        itemService.saveMovie(movieDto);
        itemService.saveBook(bookDto);
        itemService.saveAlbum(albumDto);
    }
}
