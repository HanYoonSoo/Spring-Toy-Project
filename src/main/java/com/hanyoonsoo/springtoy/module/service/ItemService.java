package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.item.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import com.hanyoonsoo.springtoy.module.entity.item.Album;
import com.hanyoonsoo.springtoy.module.entity.item.Book;
import com.hanyoonsoo.springtoy.module.entity.item.Item;
import com.hanyoonsoo.springtoy.module.entity.item.Movie;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemDto.BookDto saveBook(ItemRequestDto.BookRequestDto bookDto){
        Book book = Book.toEntity(bookDto);
        itemRepository.save(book);

        return new ItemDto.BookDto(book);
    }

    @Transactional
    public ItemDto.AlbumDto saveAlbum(ItemRequestDto.AlbumRequestDto albumDto){
        Album album = Album.toEntity(albumDto);
        itemRepository.save(album);

        return new ItemDto.AlbumDto(album);
    }

    @Transactional
    public ItemDto.MovieDto saveMovie(ItemRequestDto.MovieRequestDto movieDto){
        Movie movie = Movie.toEntity(movieDto);
        itemRepository.save(movie);

        return new ItemDto.MovieDto(movie);
    }

    @Transactional
    public ItemDto updateItem(Long itemId, ItemRequestDto itemDto){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new BusinessLogicException(ErrorCode.ITEM_NOT_FOUND));

        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setStockQuantity(itemDto.getStockQuantity());

        return new ItemDto(item);
    }

    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ItemDto convertToDto(Item item) {
        if(item instanceof Book){
            return new ItemDto.BookDto((Book) item);
        } else if(item instanceof Album){
            return new ItemDto.AlbumDto((Album) item);
        } else if(item instanceof Movie){
            return new ItemDto.MovieDto((Movie) item);
        } else{
            throw new BusinessLogicException(ErrorCode.INVALID_ITEM);
        }
    }

    @Transactional
    public String deleteItem(Long itemId) {
        itemRepository.delete(
                itemRepository.findById(itemId).orElseThrow(() -> new BusinessLogicException(ErrorCode.ITEM_NOT_FOUND))
        );

        return "Item 삭제가 완료되었습니다.";
    }
}
