package com.toby.tobyspring.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    @DisplayName("InvocationHandler 테스트")
    public void invocationHandler() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));

        assertEquals("HELLO DAHYE", proxiedHello.sayHello("Dahye"));
        assertEquals("HI DAHYE", proxiedHello.sayHi("Dahye"));
        assertEquals("THANK YOU DAHYE", proxiedHello.sayThankYou("Dahye"));
    }

    @Test
    @DisplayName("스프링 ProxyFactoryBean을 이용한 다이내믹 프록시 테스트")
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertEquals("HELLO DAHYE", proxiedHello.sayHello("Dahye"));
        assertEquals("HI DAHYE", proxiedHello.sayHi("Dahye"));
        assertEquals("THANK YOU DAHYE", proxiedHello.sayThankYou("Dahye"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}