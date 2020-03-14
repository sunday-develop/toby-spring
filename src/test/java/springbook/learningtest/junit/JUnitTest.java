package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitTest {

    private static Set<JUnitTest> testObjects = new HashSet<>();

    @Test
    void test1() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);
    }

    @Test
    void test2() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);
    }

    @Test
    void test3() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);
    }

}
