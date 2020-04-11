package com.pplenty.studytoby.chapter06.proxy;

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

    @DisplayName("스프링 프록시 팩토리 빈 사용(인터페이스 없는 경우)")
    @Test
    void proxyFactoryBeanWithoutInterface() {

        // given
        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";

        // when
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTargetWithoutInterface());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        HelloTargetWithoutInterface proxiedHello = (HelloTargetWithoutInterface) proxyFactoryBean.getObject();
        System.out.println(proxiedHello.getClass());

        // then
        assertThat(proxiedHello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(proxiedHello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(proxiedHello.sayThankYou(myName)).isEqualTo("THANK YOU " + myNameUppercase);

    }

    @DisplayName("스프링 프록시 팩토리 빈 사용(pointcut 적용")
    @Test
    void proxyFactoryBeanWithPointcut() {

        // given
        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";

        // when
        // 포인트컷 설정
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        // 프록시 팩토리 빈
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvisor((new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice())));
//        proxyFactoryBean.setProxyTargetClass(true); // 강제로 클래스 프록시로 만들도록 설정

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        System.out.println(proxiedHello.getClass());

        // then
        assertThat(proxiedHello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
        assertThat(proxiedHello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        assertThat(proxiedHello.sayThankYou(myName)).isEqualTo("Thank you " + myName);

    }

    @DisplayName("확장 포인트컷 테스트")
    @Test
    void name() {

        // given
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        // when then
        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {
        }
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {
        }
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {

        final String myName = "yusik";
        final String myNameUppercase = "YUSIK";

        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello(myName)).isEqualTo("HELLO " + myNameUppercase);
            assertThat(proxiedHello.sayHi(myName)).isEqualTo("HI " + myNameUppercase);
        } else {
            assertThat(proxiedHello.sayHello(myName)).isEqualTo("Hello " + myName);
            assertThat(proxiedHello.sayHi(myName)).isEqualTo("Hi " + myName);
        }
        assertThat(proxiedHello.sayThankYou(myName)).isEqualTo("Thank you " + myName);
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}