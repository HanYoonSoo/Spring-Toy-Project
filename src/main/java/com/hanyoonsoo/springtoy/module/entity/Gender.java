package com.hanyoonsoo.springtoy.module.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum Gender{
    MALE("남자"),
    FEMALE("여자");

    private final String value;

    Gender(String value) {
        this.value = value;
    }


    @JsonCreator
    public static Gender from(String sub){
        for(Gender gender : Gender.values()){
            if(gender.getValue().equals(sub)){
                return gender;
            }
        }
        log.debug("EnumCollection.Gender.from() exception occur sub: {}", sub);
        throw new BusinessLogicException(ErrorCode.INVALID_GENDER);
    }

    @JsonValue
    public String getValue(){
        return value;
    }

}
