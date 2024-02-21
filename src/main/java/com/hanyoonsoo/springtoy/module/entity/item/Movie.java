package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.ItemDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")
@Getter @Setter
public class Movie extends Item{

    private String director;
    private String actor;

    public static Movie toEntity(ItemDto.MovieDto movieDto){
        Movie movie = new Movie();
        movie.setName(movieDto.getName());
        movie.setPrice(movieDto.getPrice());
        movie.setStockQuantity(movieDto.getStockQuantity());
        movie.setDirector(movieDto.getDirector());
        movie.setActor(movieDto.getActor());

        return movie;
    }
}
