package com.toby.tobyspring.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("템플릿/콜백 패턴 응용해보기")
class CalculatorTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    @DisplayName("파일 읽어 덧셈하기")
    public void sumOfNumbers() throws IOException {
        assertEquals(10, (int) calculator.calcSum(this.numFilepath));
    }

    @Test
    @DisplayName("파일 읽어 곱셈하기")
    public void multiplyOfNumbers() throws IOException {
        assertEquals(24, (int) calculator.calcMultiply(this.numFilepath));
    }

    @Test
    @DisplayName("파일 읽어 스트링 이어붙이기")
    public void concatenateStrings() throws IOException {
        assertEquals("1234", calculator.concatenate(this.numFilepath));
    }
}