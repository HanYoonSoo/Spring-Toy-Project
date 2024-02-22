package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.ItemRequestDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")
@Getter @Setter
@NoArgsConstructor
public class Movie extends Item{

    private String director;
    private String actor;

    public static Movie toEntity(ItemRequestDto.MovieRequestDto movieDto){
        Movie movie = new Movie();
        movie.setName(movieDto.getName());
        movie.setPrice(movieDto.getPrice());
        movie.setStockQuantity(movieDto.getStockQuantity());
        movie.setDirector(movieDto.getDirector());
        movie.setActor(movieDto.getActor());

        return movie;
    }
}
