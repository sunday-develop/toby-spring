package com.study.spring.proxy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertEquals(hello.sayHello("Toby"), "Hello Toby");
        assertEquals(hello.sayHi("Toby"), "Hi Toby");
        assertEquals(hello.sayThankYou("Toby"), "Thank You Toby");
    }

    @Test
    public void proxyTest() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertEquals(hello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(hello.sayHi("Toby"), "HI TOBY");
        assertEquals(hello.sayThankYou("Toby"), "THANK YOU TOBY");
    }
}
