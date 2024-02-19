package com.hanyoonsoo.springtoy.global;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class UserResponseSnippet {

    public static Snippet getPatchSnippet() {

        return requestFields(
                List.of(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("회원 주소")
                )
        );
    }

    public static ResponseFieldsSnippet getUserResponseSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임 및 타입"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("작성자 이름"),
                        fieldWithPath("data.address").type(JsonFieldType.OBJECT).description("회원 주소"),
                        fieldWithPath("data.address.city").type(JsonFieldType.STRING).description("도시"),
                        fieldWithPath("data.address.street").type(JsonFieldType.STRING).description("도로주소"),
                        fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("data.gender").type(JsonFieldType.STRING).description("성별")
                )
        );
    }

    public static Snippet emailVerificationsResponseSnippet() {
        return responseFields(
                List.of(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                    fieldWithPath("data.message").type(JsonFieldType.STRING).description("결과 메시지")
                )
        );
    }

    public static Snippet emailVerificationsFailSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("Http Status"),
                        fieldWithPath("code").type(JsonFieldType.STRING).description("Error Code"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Error Message")
                )
        );
    }
}
