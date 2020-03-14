package springbook.learningtest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitTest {

    private static JUnitTest testObject;

    @Test
    void test1() throws Exception {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

    @Test
    void test2() throws Exception {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

    @Test
    void test3() throws Exception {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

}
