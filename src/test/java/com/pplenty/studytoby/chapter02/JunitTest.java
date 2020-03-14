package com.pplenty.studytoby.chapter02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by yusik on 2020/03/14.
 */
@DisplayName("Junit5 스프링 컨텍스트 학습 테스트")
@SpringBootTest
@ContextConfiguration("/junit.xml")
class JunitTest {

    @Autowired
    ApplicationContext context;

    static Set<JunitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;

    @DisplayName("테스트1")
    @Test
    void test1() {

        assertThat(testObjects).isEmpty();
        testObjects.add(this);
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;

    }

    @DisplayName("테스트2")
    @Test
    void test2() {

        testObjects.add(this);
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;

    }

    @DisplayName("테스트3")
    @Test
    void test3() {

        testObjects.add(this);
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;

    }
}
