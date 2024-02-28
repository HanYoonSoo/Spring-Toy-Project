package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.item.ItemDto;
import com.hanyoonsoo.springtoy.module.dto.item.ItemRequestDto;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity items(){
        List<ItemDto> response = itemService.findAll();
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/new/book")
    public ResponseEntity newBook(@Valid @RequestBody ItemRequestDto.BookRequestDto bookDto){
        ItemDto.BookDto response = itemService.saveBook(bookDto);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/new/album")
    public ResponseEntity newAlbum(@Valid @RequestBody ItemRequestDto.AlbumRequestDto albumDto){
        ItemDto.AlbumDto response = itemService.saveAlbum(albumDto);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/new/movie")
    public ResponseEntity newMovie(@Valid @RequestBody ItemRequestDto.MovieRequestDto movieDto){
        ItemDto.MovieDto response = itemService.saveMovie(movieDto);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/edit")
    public ResponseEntity updateItem(@PathVariable("itemId") Long itemId, @Valid @RequestBody ItemRequestDto itemDto){
        ItemDto response = itemService.updateItem(itemId, itemDto);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteItem(@PathVariable("itemId") Long itemId){
        String response = itemService.deleteItem(itemId);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
