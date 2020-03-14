package com.study.spring.learningtest;

import com.study.spring.user.learningtest.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcSumTest {

    private Calculator calculator;
    private String numFilepath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/number.txt").getPath();
    }

    @DisplayName("파일의 숫자 합을 계산하는 코드의 테스트")
    @Test
    public void sumOfNumbers() throws IOException {
        Integer sum = calculator.calSum(numFilepath);
        assertEquals(sum, 10);
    }

    @DisplayName("파일의 숫자 곱을 계산하는 코드의 테스트")
    @Test
    public void multiplyOfNumbers() throws IOException {
        Integer sum = calculator.calMultiply(numFilepath);
        assertEquals(sum, 24);
    }
}
