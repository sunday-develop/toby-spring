package com.study.spring.learningtest;

import com.study.spring.user.learningtest.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcSumTest {

    @DisplayName("파일의 숫자 합을 계산하는 코드의 테스트")
    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        Integer sum = calculator.calSum(getClass().getResource("/number.txt").getPath());
        assertEquals(sum, 10);
    }
}
