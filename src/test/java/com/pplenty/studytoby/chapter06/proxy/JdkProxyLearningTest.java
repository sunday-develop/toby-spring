package com.pplenty.studytoby.chapter06.proxy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("대문자 변경 프록시")
    @Test
    void sayHi() {

        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";
        Hello hello = new HelloUppercase(new HelloTarget());

        assertThat(hello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(hello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(hello.sayThankYou(myName)).isEqualTo("THANK YOU " + myNameUppercase);
    }

    @DisplayName("테스트 이름")
    @Test
    void sayThankYou() {

        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));

        String r = proxiedHello.sayHello("zzz");
        System.out.println(proxiedHello.toString());
        System.out.println(r);

    }
}