package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@NoArgsConstructor
public class Book extends Item{

    private String author;
    private String isbn;

    public static Book toEntity(ItemRequestDto.BookRequestDto bookDto){
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setPrice(bookDto.getPrice());
        book.setStockQuantity(bookDto.getStockQuantity());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());

        return book;
    }
}
