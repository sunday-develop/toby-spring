package springbook.learningtest.spring.pointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PointcutTest {

    @Test
    void methodSignaturePointcut() throws Exception {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);

        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
                "execution(public int springbook.learningtest.spring.pointcut.Target.minus(int, int) " +
                        "throws java.lang.RuntimeException)"
        );

        final Class<Target> targetClass = Target.class;
        final Method minusMethod = targetClass.getMethod("minus", int.class, int.class);
        assertThat(pointcut.getClassFilter().matches(targetClass)).isTrue();
        assertThat(pointcut.getMethodMatcher().matches(minusMethod, null)).isTrue();

        final Method plusMethod = targetClass.getMethod("plus", int.class, int.class);
        assertThat(pointcut.getMethodMatcher().matches(plusMethod, null)).isFalse();

        final Class<Bean> beanClass = Bean.class;
        final Method methodMethod = beanClass.getMethod("method");
        assertThat(pointcut.getClassFilter().matches(beanClass)).isFalse();
        assertThat(pointcut.getMethodMatcher().matches(methodMethod, null)).isFalse();
    }

    private void targetClassPointcutMatches(String expression, boolean... expected) throws Exception {
        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    private void pointcutMatches(String expression,
                                 boolean expected,
                                 Class<?> clazz,
                                 String methodName,
                                 Class<?>... args) throws Exception {

        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz)).isEqualTo(expected);
        assertThat(pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null)).isEqualTo(expected);
    }

}
