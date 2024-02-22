package com.hanyoonsoo.springtoy.module.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanyoonsoo.springtoy.module.entity.item.Album;
import com.hanyoonsoo.springtoy.module.entity.item.Book;
import com.hanyoonsoo.springtoy.module.entity.item.Item;
import com.hanyoonsoo.springtoy.module.entity.item.Movie;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.NIP;

@Getter @Setter
@NoArgsConstructor
public class ItemDto {

    private Long itemId;

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    private String name;

    @Min(value = 1, message = "최소 1개")
    private int price;

    @Min(value = 1, message = "최소 1개")
    private int stockQuantity;

    public ItemDto(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public ItemDto(Item item){
        this.setItemId(item.getId());
        this.setName(item.getName());
        this.setPrice(item.getPrice());
        this.setStockQuantity(item.getStockQuantity());
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class BookDto extends ItemDto{
        @NotBlank(message = "Author이 입력되지 않았습니다.")
        private String author;

        @NotBlank(message = "Isbn이 입력되지 않았습니다.")
        private String isbn;

        public BookDto(String name, int price, int stockQuantity, String author, String isbn) {
            super(name, price, stockQuantity);
            this.author = author;
            this.isbn = isbn;
        }

        public BookDto(Book book){
            this.setItemId(book.getId());
            this.setName(book.getName());
            this.setPrice(book.getPrice());
            this.setStockQuantity(book.getStockQuantity());
            this.setAuthor(book.getAuthor());
            this.setIsbn(book.getIsbn());
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class AlbumDto extends ItemDto{
        @NotBlank(message = "아티스트 입력되지 않았습니다.")
        private String artist;

        @NotBlank(message = "ETC가 입력되지 않았습니다.")
        private String etc;

        public AlbumDto(String name, int price, int stockQuantity, String artist, String etc) {
            super(name, price, stockQuantity);
            this.artist = artist;
            this.etc = etc;
        }

        public AlbumDto(Album album){
            this.setItemId(album.getId());
            this.setName(album.getName());
            this.setPrice(album.getPrice());
            this.setStockQuantity(album.getStockQuantity());
            this.setArtist(album.getArtist());
            this.setEtc(album.getEtc());
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class MovieDto extends ItemDto{
        @NotBlank(message = "감독이 입력되지 않았습니다.")
        private String director;

        @NotBlank(message = "배우가 입력되지 않았습니다.")
        private String actor;

        public MovieDto(String name, int price, int stockQuantity, String director, String actor) {
            super(name, price, stockQuantity);
            this.director = director;
            this.actor = actor;
        }

        public MovieDto(Movie movie){
            this.setItemId(movie.getId());
            this.setName(movie.getName());
            this.setPrice(movie.getPrice());
            this.setStockQuantity(movie.getStockQuantity());
            this.setDirector(movie.getDirector());
            this.setActor(movie.getActor());
        }
    }
}
