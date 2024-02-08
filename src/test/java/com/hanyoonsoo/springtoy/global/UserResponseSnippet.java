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
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임 및 타입"),
                        fieldWithPath("data.nation").type(JsonFieldType.STRING).description("회원 국가"),
                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("data.image").type(JsonFieldType.STRING).description("프로필 이미지 url"),
                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("자기소개"),
                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 역할"),
                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 가입일"),
                        fieldWithPath("data.lastModifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정일")
                )
        );
    }
}
