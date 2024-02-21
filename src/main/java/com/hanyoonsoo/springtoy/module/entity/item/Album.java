package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.ItemDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item{

    private String artist;
    private String etc;

    public static Album toEntity(ItemDto.AlbumDto albumDto){
        Album album = new Album();
        album.setName(albumDto.getName());
        album.setPrice(albumDto.getPrice());
        album.setStockQuantity(albumDto.getStockQuantity());
        album.setArtist(albumDto.getArtist());
        album.setEtc(albumDto.getEtc());

        return album;
    }
}
