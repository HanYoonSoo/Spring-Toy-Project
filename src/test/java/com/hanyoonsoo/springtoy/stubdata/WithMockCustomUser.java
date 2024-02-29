package com.hanyoonsoo.springtoy.stubdata;

import com.hanyoonsoo.springtoy.module.constants.Authority;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithMockCustomUser {
    String name() default "testName";

    String email() default "email@gmail.com";

    String role() default "ROLE_USER";

    String password() default "SIBAL";

}
