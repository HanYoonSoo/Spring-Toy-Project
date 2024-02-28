package com.hanyoonsoo.springtoy.global.snippets;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class AuthResponseSnippet {
    public static Snippet logoutSnippet() {
        return responseFields(
                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 데이터")
        );
    }

    public static Snippet reissueSnippet() {
        return responseFields(
                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 데이터")
        );
    }
}
