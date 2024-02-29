package com.hanyoonsoo.springtoy.integration;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.global.snippets.OrderResponseSnippet;
import com.hanyoonsoo.springtoy.global.adapter.ResultActionsUtils;
import com.hanyoonsoo.springtoy.module.dto.auth.TokenDto;
import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import com.hanyoonsoo.springtoy.module.dto.order.OrderDto;
import com.hanyoonsoo.springtoy.module.dto.order.OrderSearchDto;
import com.hanyoonsoo.springtoy.module.dto.user.UserDto;
import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.encryption.AES128Config;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.repository.OrderSearch;
import com.hanyoonsoo.springtoy.module.service.ItemService;
import com.hanyoonsoo.springtoy.module.service.OrderService;
import com.hanyoonsoo.springtoy.module.service.UserService;
import com.hanyoonsoo.springtoy.stubdata.StubData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.hanyoonsoo.springtoy.global.adapter.ApiDocumentUtils.getRequestPreProcessor;
import static com.hanyoonsoo.springtoy.global.adapter.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/orders";
    private final String EMAIL = "email@gmail.com";

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @BeforeEach
    void beforeEach() {
        UserDto.SignUp signUpDto = StubData.MockUser.getSignUpDto();
        userService.signUp(signUpDto);
    }

    @Test
    @DisplayName("Order 생성 테스트")
    public void createOrder() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        userService.updateUserEmailVerified(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        Long itemId = itemService.saveBook(bookDto).getItemId();
        int count = 3;


        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndParam(mockMvc, uri, accessToken, encryptedRefreshToken, itemId, count);

        //then
        actions
                .andExpect(status().isCreated())
                .andDo(document("new-order",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        OrderResponseSnippet.newOrderSnippet()));
    }

    @Test
    @DisplayName("OrderStatus 별 조회 테스트")
    public void orderSearchStatusTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        userService.updateUserEmailVerified(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        Long bookId = itemService.saveBook(bookDto).getItemId();
        int count = 3;

        orderService.order(EMAIL, bookId, count);

        ItemRequestDto.AlbumRequestDto albumDto = StubData.MockItem.getAlbumDto();
        Long albumId = itemService.saveAlbum(albumDto).getItemId();
        count = 2;

        orderService.order(EMAIL, albumId, count);

        OrderSearchDto orderSearchDto = new OrderSearchDto(null, OrderStatus.ORDER);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequestWithTokenAndParam(mockMvc, uri, accessToken, encryptedRefreshToken, orderSearchDto);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-orders-orderStatus",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        OrderResponseSnippet.getOrdersSnippet()));
    }

    @Test
    @DisplayName("Order ItemName별 조회 테스트")
    public void orderSearchItemNameTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        userService.updateUserEmailVerified(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        Long bookId = itemService.saveBook(bookDto).getItemId();
        int count = 3;

        orderService.order(EMAIL, bookId, count);

        ItemRequestDto.AlbumRequestDto albumDto = StubData.MockItem.getAlbumDto();
        Long albumId = itemService.saveAlbum(albumDto).getItemId();
        count = 2;

        orderService.order(EMAIL, albumId, count);

        OrderSearchDto orderSearchDto = new OrderSearchDto("book", null);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequestWithTokenAndParam(mockMvc, uri, accessToken, encryptedRefreshToken, orderSearchDto);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-orders-itemName",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        OrderResponseSnippet.getOrdersSnippet()));
    }

    @Test
    @DisplayName("Order Paging 조회 테스트")
    public void orderPagingTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        userService.updateUserEmailVerified(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        Long bookId = itemService.saveBook(bookDto).getItemId();
        int count = 3;

        orderService.order(EMAIL, bookId, count);

        ItemRequestDto.AlbumRequestDto albumDto = StubData.MockItem.getAlbumDto();
        Long albumId = itemService.saveAlbum(albumDto).getItemId();
        count = 2;

        orderService.order(EMAIL, albumId, count);

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/pages")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequestWithTokenAndParamAndPaging(mockMvc, uri, accessToken, encryptedRefreshToken, 100);

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("paging-orders",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()));
    }

    @Test
    @DisplayName("Order 취소 테스트")
    public void orderCancelTest() throws Exception{
        //given
        User user = userService.findUserByEmail(EMAIL);
        userService.updateUserEmailVerified(EMAIL);
        CustomUserDetails userDetails = CustomUserDetails.of(user);

        TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        ItemRequestDto.BookRequestDto bookDto = StubData.MockItem.getBookDto();
        Long bookId = itemService.saveBook(bookDto).getItemId();
        int count = 3;

        orderService.order(EMAIL, bookId, count);

        List<OrderDto> book = orderService.findSearchOrder(new OrderSearch(EMAIL, "book", null));

        //when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + book.get(0).getOrderId() + "/cancel")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(mockMvc, uri, accessToken, encryptedRefreshToken);

        //then
        book = orderService.findSearchOrder(new OrderSearch(EMAIL, "book", null));
        assertThat(book.get(0).getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        actions
                .andExpect(status().isAccepted())
                .andDo(document("delete-order",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        OrderResponseSnippet.deleteOrdersSnippet()));
    }
}
