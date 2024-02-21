package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.ItemDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item{

    private String author;
    private String isbn;

    public static Book toEntity(ItemDto.BookDto bookDto){
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setPrice(bookDto.getPrice());
        book.setStockQuantity(bookDto.getStockQuantity());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());

        return book;
    }
}
