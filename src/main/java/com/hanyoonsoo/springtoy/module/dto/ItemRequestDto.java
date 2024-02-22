package com.hanyoonsoo.springtoy.module.dto;

import com.hanyoonsoo.springtoy.module.entity.item.Album;
import com.hanyoonsoo.springtoy.module.entity.item.Book;
import com.hanyoonsoo.springtoy.module.entity.item.Item;
import com.hanyoonsoo.springtoy.module.entity.item.Movie;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    private String name;

    @Min(value = 1, message = "최소 1개")
    private int price;

    @Min(value = 1, message = "최소 1개")
    private int stockQuantity;

    public ItemRequestDto(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookRequestDto extends ItemRequestDto{
        @NotBlank(message = "Author이 입력되지 않았습니다.")
        private String author;

        @NotBlank(message = "Isbn이 입력되지 않았습니다.")
        private String isbn;

        public BookRequestDto(String name, int price, int stockQuantity, String author, String isbn) {
            super(name, price, stockQuantity);
            this.author = author;
            this.isbn = isbn;
        }

    }

    @Getter @Setter
    @NoArgsConstructor
    public static class AlbumRequestDto extends ItemRequestDto{
        @NotBlank(message = "아티스트 입력되지 않았습니다.")
        private String artist;

        @NotBlank(message = "ETC가 입력되지 않았습니다.")
        private String etc;

        public AlbumRequestDto(String name, int price, int stockQuantity, String artist, String etc) {
            super(name, price, stockQuantity);
            this.artist = artist;
            this.etc = etc;
        }

    }

    @Getter @Setter
    @NoArgsConstructor
    public static class MovieRequestDto extends ItemRequestDto{
        @NotBlank(message = "감독이 입력되지 않았습니다.")
        private String director;

        @NotBlank(message = "배우가 입력되지 않았습니다.")
        private String actor;

        public MovieRequestDto(String name, int price, int stockQuantity, String director, String actor) {
            super(name, price, stockQuantity);
            this.director = director;
            this.actor = actor;
        }

        public MovieRequestDto(Movie movie){
            this.setName(movie.getName());
            this.setPrice(movie.getPrice());
            this.setStockQuantity(movie.getStockQuantity());
            this.setDirector(movie.getDirector());
            this.setActor(movie.getActor());
        }
    }
}
