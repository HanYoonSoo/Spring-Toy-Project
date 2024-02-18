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
                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("프로필 이미지 url"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기 소개"),
                        fieldWithPath("nation").type(JsonFieldType.STRING).description("회원 국가")
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
}
