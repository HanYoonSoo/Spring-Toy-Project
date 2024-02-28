package com.hanyoonsoo.springtoy.stubdata;

import com.hanyoonsoo.springtoy.module.dto.auth.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.auth.LoginResponse;
import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import com.hanyoonsoo.springtoy.module.dto.order.OrderSearchDto;
import com.hanyoonsoo.springtoy.module.dto.user.UserDto;
import com.hanyoonsoo.springtoy.module.dto.user.UserPatchDto;
import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.Gender;
import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class StubData {

    public static class MockUser extends StubData{


        public static UserDto.SignUp getSignUpDto() {
            return new UserDto.SignUp("email@gmail.com", "1234", "test", "test", new Address("test", "1234", "1234"), Gender.from("남자"));
        }

        public static CustomUserDetails getUserDetails() {
            return CustomUserDetails.of(new User());
        }

        public static LoginDto getLoginSuccessDto() {
            return new LoginDto("email@gmail.com", "1234");
        }

        public static LoginResponse getLoginResponseDto() {
            return new LoginResponse("email@gmail.com", "test");
        }

        public static LoginDto getLoginFailDto() {
            return new LoginDto("email@gmail.com", "12345");
        }

        public static UserPatchDto getPatchDto() {
            return new UserPatchDto("test2", new Address("test2", "12345", "12345"));
        }
    }

    public static class MockItem extends StubData{
        public static ItemRequestDto.BookRequestDto getBookDto(){
            return new ItemRequestDto.BookRequestDto("book", 1000, 1000, "book", "book");
        }

        public static ItemRequestDto.AlbumRequestDto getAlbumDto(){
            return new ItemRequestDto.AlbumRequestDto("album", 1000, 1000, "album", "album");
        }

        public static ItemRequestDto.MovieRequestDto getMovieDto(){
            return new ItemRequestDto.MovieRequestDto("movie", 1000, 1000, "movie", "movie");
        }

        public static ItemRequestDto updateItemDto(){
            return new ItemRequestDto("test2", 2000, 2000);
        }
    }

    public static class MockOrder extends StubData{
        public static OrderSearchDto orderSearchDto(){
            return new OrderSearchDto("test1", OrderStatus.ORDER);
        }
    }

    public static class CustomMultipartFile {
        @Getter
        static final String IMAGE_URL =
                "https://s3.ap-northeast-2.amazonaws.com/imagetest.file.bucket/image/example.png";

    }
}
