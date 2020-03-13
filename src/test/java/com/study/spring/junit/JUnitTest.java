package com.study.spring.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class JUnitTest {

    static JUnitTest testObject;

    @Test
    public void test1() {
        assertNotSame(this, testObject);
        testObject = this;
    }

    @Test
    public void test2() {
        assertNotSame(this, testObject);
        testObject = this;
    }

    @Test
    public void test3() {
        assertNotSame(this, testObject);
        testObject = this;
    }

}
