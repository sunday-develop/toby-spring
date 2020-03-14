package springbook.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {

    private Calculator calculator;
    private String numFilePath;

    @BeforeEach
    void setup() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    void sumOfNumbers() throws Exception {
        assertThat(calculator.calcSum(numFilePath)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws Exception {
        assertThat(calculator.calcMultiply(numFilePath)).isEqualTo(24);
    }

}
