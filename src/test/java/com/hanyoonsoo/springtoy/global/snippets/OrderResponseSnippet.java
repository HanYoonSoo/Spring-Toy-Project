package com.hanyoonsoo.springtoy.global.snippets;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class OrderResponseSnippet {
    public static Snippet newOrderSnippet() {
        return responseFields(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath("data.orderDate").type(JsonFieldType.STRING).description("주문 시각"),
                fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                fieldWithPath("data.address").type(JsonFieldType.OBJECT).description("주문 주소"),
                fieldWithPath("data.address.city").type(JsonFieldType.STRING).description("도시"),
                fieldWithPath("data.address.street").type(JsonFieldType.STRING).description("도로"),
                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING).description("우편번호"),
                fieldWithPath("data.orderItems[]").type(JsonFieldType.ARRAY).description("주문 아이템"),
                fieldWithPath("data.orderItems[].itemName").type(JsonFieldType.STRING).description("주문 아이템 이름"),
                fieldWithPath("data.orderItems[].orderPrice").type(JsonFieldType.NUMBER).description("주문 아이템 가격"),
                fieldWithPath("data.orderItems[].count").type(JsonFieldType.NUMBER).description("주문 아이템 개수")
        );
    }

    public static Snippet getOrdersSnippet() {
        return responseFields(
                fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("결과 데이터"),
                fieldWithPath("data[].orderId").type(JsonFieldType.NUMBER).description("주문 번호"),
                fieldWithPath("data[].nickName").type(JsonFieldType.STRING).description("유저 닉네임"),
                fieldWithPath("data[].orderDate").type(JsonFieldType.STRING).description("주문 시각"),
                fieldWithPath("data[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                fieldWithPath("data[].address").type(JsonFieldType.OBJECT).description("주문 주소"),
                fieldWithPath("data[].address.city").type(JsonFieldType.STRING).description("도시"),
                fieldWithPath("data[].address.street").type(JsonFieldType.STRING).description("도로"),
                fieldWithPath("data[].address.zipcode").type(JsonFieldType.STRING).description("우편번호"),
                fieldWithPath("data[].orderItems[]").type(JsonFieldType.ARRAY).description("주문 아이템"),
                fieldWithPath("data[].orderItems[].itemName").type(JsonFieldType.STRING).description("주문 아이템 이름"),
                fieldWithPath("data[].orderItems[].orderPrice").type(JsonFieldType.NUMBER).description("주문 아이템 가격"),
                fieldWithPath("data[].orderItems[].count").type(JsonFieldType.NUMBER).description("주문 아이템 개수")
        );
    }

    public static Snippet deleteOrdersSnippet() {
        return responseFields(
                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 데이터")
        );
    }
}
