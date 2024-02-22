package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.dto.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.ItemRequestDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
@NoArgsConstructor
public class Album extends Item{

    private String artist;
    private String etc;

    public static Album toEntity(ItemRequestDto.AlbumRequestDto albumDto){
        Album album = new Album();
        album.setName(albumDto.getName());
        album.setPrice(albumDto.getPrice());
        album.setStockQuantity(albumDto.getStockQuantity());
        album.setArtist(albumDto.getArtist());
        album.setEtc(albumDto.getEtc());

        return album;
    }
}
