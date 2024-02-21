package com.hanyoonsoo.springtoy.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDto {

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    private String name;

    @Size(min = 1, max = 100000000, message = "잘못된 가격입니다.")
    private int price;

    @Size(min = 1, max = 1000)
    private int stockQuantity;


    @Getter @Setter
    public static class BookDto extends ItemDto{
        @NotBlank(message = "Author이 입력되지 않았습니다.")
        private String author;

        @NotBlank(message = "Isbn이 입력되지 않았습니다.")
        private String isbn;

    }

    @Getter @Setter
    public static class AlbumDto extends ItemDto{
        @NotBlank(message = "아티스트 입력되지 않았습니다.")
        private String artist;

        @NotBlank(message = "ETC가 입력되지 않았습니다.")
        private String etc;
    }

    @Getter @Setter
    public static class MovieDto extends ItemDto{
        @NotBlank(message = "감독이 입력되지 않았습니다.")
        private String director;

        @NotBlank(message = "배우가 입력되지 않았습니다.")
        private String actor;
    }
}
