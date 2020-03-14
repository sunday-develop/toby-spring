package com.toby.tobyspring.learningtest.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/junit.xml")
@DisplayName("스프링의 어플리케이션 컨텍스트는 테스트 개수와 상관없이 한 개만 만들어질까?")
public class JUnitApplicationContextTest {
    @Autowired
    static ApplicationContext context;

    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        Assertions.assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test2() {
        Assertions.assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test3() {
        Assertions.assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
}
