package com.toby.tobyspring.learningtest.jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    @Test
    @DisplayName("리플렉션 학습 테스트")
    public void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";
        assertEquals(6, name.length());

        Method lengthMethod = String.class.getMethod("length");
        assertEquals(6, lengthMethod.invoke(name));

        assertEquals('S', name.charAt(0));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertEquals('S', charAtMethod.invoke(name, 0));
    }
}
