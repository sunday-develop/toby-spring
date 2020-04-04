package com.pplenty.studytoby.chapter06.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/04/04.
 */
@DisplayName("다이내믹 프록시")
class JdkProxyLearningTest {

    @DisplayName("타겟 테스트")
    @Test
    void simpleProxy() {

        final String myName = "yusik";
        Hello hello = new HelloTarget();

        assertThat(hello.sayHello(myName)).isEqualTo("Hello " + myName);
        assertThat(hello.sayHi(myName)).isEqualTo("Hi " + myName);
        assertThat(hello.sayThankYou(myName)).isEqualTo("Thank you " + myName);
    }

    @DisplayName("직접 프록시 패턴 구현")
    @Test
    void proxyPattern() {

        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";
        Hello hello = new HelloUppercase(new HelloTarget());

        assertThat(hello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(hello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(hello.sayThankYou(myName)).isEqualTo("THANK YOU " + myNameUppercase);
    }

    @DisplayName("JDK 다이내믹 프록시 생성")
    @Test
    void jdkDynamicProxy() {

        // given
        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";

        // when
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));

        // then
        assertThat(proxiedHello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(proxiedHello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(proxiedHello.sayThankYou(myName)).isEqualTo("THANK YOU " + myNameUppercase);

    }

    @DisplayName("스프링 프록시 팩토리 빈 사용")
    @Test
    void proxyFactoryBean() {

        // given
        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";

        // when
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());
//        proxyFactoryBean.setProxyTargetClass(true);

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        System.out.println(proxiedHello.getClass());

        // then
        assertThat(proxiedHello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(proxiedHello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(proxiedHello.sayThankYou(myName)).isEqualTo("THANK YOU " + myNameUppercase);

    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}