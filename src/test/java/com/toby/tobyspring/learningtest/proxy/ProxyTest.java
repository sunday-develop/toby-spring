package com.toby.tobyspring.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
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

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertEquals("HELLO DAHYE", proxiedHello.sayHello("Dahye"));
        assertEquals("HI DAHYE", proxiedHello.sayHi("Dahye"));
        assertEquals("Thank You Dahye", proxiedHello.sayThankYou("Dahye"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    @DisplayName("확장 포인트 컷 테스트")
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };

        pointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), pointcut, true);

        class HelloWorld extends HelloTarget {
        }
        checkAdviced(new HelloWorld(), pointcut, false);

        class HelloToby extends HelloTarget {
        }
        checkAdviced(new HelloToby(), pointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertEquals("HELLO DAHYE", proxiedHello.sayHello("Dahye"));
            assertEquals("HI DAHYE", proxiedHello.sayHi("Dahye"));
            assertEquals("Thank You Dahye", proxiedHello.sayThankYou("Dahye"));
        } else {
            assertEquals("Hello Dahye", proxiedHello.sayHello("Dahye"));
            assertEquals("Hi Dahye", proxiedHello.sayHi("Dahye"));
            assertEquals("Thank You Dahye", proxiedHello.sayThankYou("Dahye"));
        }
    }
}