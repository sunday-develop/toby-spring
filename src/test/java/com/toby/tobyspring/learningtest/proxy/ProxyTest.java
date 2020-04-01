package com.toby.tobyspring.learningtest.proxy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {
    @Test
    @DisplayName("클라이언트 역할의 테스트")
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertEquals("Hello Dahye", hello.sayHello("Dahye"));
        assertEquals("Hi Dahye", hello.sayHi("Dahye"));
        assertEquals("Thank You Dahye", hello.sayThankYou("Dahye"));
    }

    @Test
    @DisplayName("HelloUppercase 프록시 테스트")
    public void proxyUppercase() {
        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertEquals("HELLO DAHYE", proxiedHello.sayHello("Dahye"));
        assertEquals("HI DAHYE", proxiedHello.sayHi("Dahye"));
        assertEquals("THANK YOU DAHYE", proxiedHello.sayThankYou("Dahye"));
    }
}