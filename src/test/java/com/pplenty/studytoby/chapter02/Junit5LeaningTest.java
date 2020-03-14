package com.pplenty.studytoby.chapter02;

import com.pplenty.studytoby.User;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by yusik on 2020/03/14.
 */
@DisplayName("Junit5 학습 테스트")
class Junit5LeaningTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }

    @DisplayName("동일성 & 동등성")
    @Test
    void equalTo() {

        // given
        User user1 = new User("koh", "yusik", "1234");
        User user2 = new User("koh", "yusik", "1234");
        User user3 = new User("koh2", "yusik", "1234");

        // when
        // then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).isNotSameAs(user2);
        assertThat(user1).isSameAs(user1);
    }

    @DisplayName("[예외] assertThrows - UnsupportedOperationException")
    @Test
    void exceptionTest1() {
        Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not supported");
        });
        assertEquals(exception.getMessage(), "Not supported");
    }

    @DisplayName("[예외] assertThrows - IllegalArgumentException")
    @Test
    void exceptionTest2() {
        String nullStr = null;
        assertThrows(IllegalArgumentException.class, () -> {
            Integer.valueOf(nullStr);
        });
    }

    @DisplayName("[예외] assertThatNullPointerException")
    @Test
    void exceptionTest3() {
        String nullStr = null;
        assertThatNullPointerException()
                .isThrownBy(() -> nullStr.equals(""))
                .withMessage(null)
        ;
    }

    @DisplayName("[예외] assertThatThrownBy")
    @Test
    void exceptionTest4() {
        int base = 0;
        assertThatThrownBy(() -> System.out.println(2 / base))
                .hasNoCause()
                .hasMessageContaining("zero")
        ;
    }
}
