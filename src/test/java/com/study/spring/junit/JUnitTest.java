package com.study.spring.junit;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class JUnitTest {

    static JUnitTest testObject;
    static Set<JUnitTest> testObjects = new HashSet<>();

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

    @Test
    public void test4() {
        assertFalse(testObjects.contains(testObject));
        testObjects.add(this);
    }

    @Test
    public void test5() {
        assertFalse(testObjects.contains(testObject));
        testObjects.add(this);
    }

    @Test
    public void test6() {
        assertFalse(testObjects.contains(testObject));
        testObjects.add(this);
    }
}
