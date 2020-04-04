package com.study.spring.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionTest {

    @DisplayName("리플렉션 학습 테스트")
    @Test
    void invokeMethod() throws Exception {

        String name = "Spring";

        assertEquals(name.length(), 6);

        Method lengthMethod = String.class.getMethod("length");
        assertEquals((Integer) lengthMethod.invoke(name), 6);

        assertEquals(name.charAt(0), 'S');

        Method charMethod = String.class.getMethod("charAt", int.class);
        assertEquals((Character) charMethod.invoke(name), 'S');
    }
}
