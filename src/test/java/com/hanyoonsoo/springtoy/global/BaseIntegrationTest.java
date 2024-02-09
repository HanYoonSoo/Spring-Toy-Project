package com.hanyoonsoo.springtoy.global;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Disabled // 해당 Annotation이 지정된 테스트 클래스 또는 테스트 메서드 실행 X
@Transactional // 데이터베이스 롤백
@SpringBootTest // Test를 위한 Application Context를 로딩하며 여러가지 속성 제공
@AutoConfigureMockMvc // @WebMvcTest가 아닌 @SpringBootTest 어노테이션을 사용하며 MockMvc를 이용한 테스트를 위한 어노테이션
@AutoConfigureRestDocs // Spring Rest Docs를 사용하기 위해 MockMvc 빈을 커스터마이즈.
//@ActiveProfiles("test")
//@WebMvcTest <- Web 계층만을 테스트할 때 사용하는 어노테이션, Web 계층 테스트에 필요한 Bean들만 등록(Security 함께 진행)
@ExtendWith(RestDocumentationExtension.class)
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
}
