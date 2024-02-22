package com.hanyoonsoo.springtoy.module.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),

    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),

    INVALID_GENDER(HttpStatus.NOT_ACCEPTABLE, "성별이 잘못되었습니다."),

    NOT_ENOUGH_STOCK(HttpStatus.NOT_ACCEPTABLE, "재고가 충분하지 않습니다."),

    INVALID_CANCEL(HttpStatus.NOT_ACCEPTABLE, "이미 배송 출발하여 취소가 불가능합니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다."),

    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메시지를 발송할 수 없습니다."),

    USER_EXISTS(HttpStatus.CONFLICT, "유저가 이미 존재합니다."),

    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL CreateCode exception occur"),

    VERIFIED_USER(HttpStatus.CONFLICT, "이미 인증된 유저입니다."),

    INVALID_CODE(HttpStatus.BAD_REQUEST, "잘못된 코드입니다."),

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 아이템을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 주문을 찾을 수 없습니다."),

    INVALID_ITEM(HttpStatus.NO_CONTENT, "존재하지 않는 아이템 종류입니다.");

    private final HttpStatus httpStatus;
    private final String message;


}
