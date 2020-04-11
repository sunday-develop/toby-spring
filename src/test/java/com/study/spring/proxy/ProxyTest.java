package com.study.spring.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProxyTest {

    @Test
    void proxyTest_1() {
        Hello hello = new HelloTarget();
        assertEquals(hello.sayHello("Toby"), "Hello Toby");
        assertEquals(hello.sayHi("Toby"), "Hi Toby");
        assertEquals(hello.sayThankYou("Toby"), "Thank You Toby");
    }

    @Test
    void proxyTest_2() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertEquals(hello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(hello.sayHi("Toby"), "HI TOBY");
        assertEquals(hello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    @Test
    void proxyTest_3() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class }, new UppercaseHandler(new HelloTarget()));
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxiedHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    @Test
    void proxyTest_4() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        assert proxiedHello != null;
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxiedHello.sayThankYou("Toby"), "THANK YOU TOBY");
    }

    @Test
    void proxyTest_5() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        assert proxiedHello != null;
        assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
        assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
        assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You Toby");
    }

    @Test
    void classNamePointcutAdvisor() {

        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };

        classMethodPointcut.setMappedName("sayH*");
        checkAdvice(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {
        }
        checkAdvice(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {
        }
        checkAdvice(new HelloToby(), classMethodPointcut, true);

    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean advice) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        if (advice) {
            assert proxiedHello != null;
            assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
            assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
            assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You toby");
        } else {
            assert proxiedHello != null;
            assertEquals(proxiedHello.sayHello("Toby"), "Hello Toby");
            assertEquals(proxiedHello.sayHi("Toby"), "Hi Toby");
            assertEquals(proxiedHello.sayThankYou("Toby"), "Thank You Toby");
        }
    }
}
