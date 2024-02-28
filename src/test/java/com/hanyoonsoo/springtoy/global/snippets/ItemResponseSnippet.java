package com.hanyoonsoo.springtoy.global.snippets;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ItemResponseSnippet {

    public static Snippet newBookSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("책 이름"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("책 가격"),
                        fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("책 재고"),
                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("책 저자"),
                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("책 식별번호")
                )
        );
    }

    public static Snippet newAlbumSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("앨범 이름"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("앨범 가격"),
                        fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("앨범 재고"),
                        fieldWithPath("data.artist").type(JsonFieldType.STRING).description("앨범 가수"),
                        fieldWithPath("data.etc").type(JsonFieldType.STRING).description("앨범 ETC")
                )
        );
    }

    public static Snippet newMovieSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("영화 이름"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("영화 가격"),
                        fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("영화 재고"),
                        fieldWithPath("data.director").type(JsonFieldType.STRING).description("영화 감독"),
                        fieldWithPath("data.actor").type(JsonFieldType.STRING).description("영화 배우")
                )
        );
    }

    public static Snippet getItemsSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("결과 데이터"),
                        fieldWithPath("data[].itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("아이템 이름"),
                        fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("아이템 가격"),
                        fieldWithPath("data[].stockQuantity").type(JsonFieldType.NUMBER).description("아이템 재고"),
                        fieldWithPath("data[].director").type(JsonFieldType.STRING).optional().description("영화 감독"),
                        fieldWithPath("data[].actor").type(JsonFieldType.STRING).optional().description("영화 배우"),
                        fieldWithPath("data[].author").type(JsonFieldType.STRING).optional().description("책 저자"),
                        fieldWithPath("data[].isbn").type(JsonFieldType.STRING).optional().description("책 ISBN 번호"),
                        fieldWithPath("data[].artist").type(JsonFieldType.STRING).optional().description("음반 아티스트"),
                        fieldWithPath("data[].etc").type(JsonFieldType.STRING).optional().description("음반 기타 정보")
                )
        );
    }

    public static Snippet updateItemSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("영화 이름"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("영화 가격"),
                        fieldWithPath("data.stockQuantity").type(JsonFieldType.NUMBER).description("영화 재고")
                )
        );
    }

    public static Snippet deleteItemSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.STRING).description("결과 데이터")
                )
        );
    }
}
