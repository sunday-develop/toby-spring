package springbook.learningtest.template;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {

    @Test
    void sumOfNumbers() throws Exception {
        final Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("/numbers.txt").getPath());

        assertThat(sum).isEqualTo(10);
    }

}
