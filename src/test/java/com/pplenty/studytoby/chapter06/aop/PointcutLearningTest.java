package com.pplenty.studytoby.chapter06.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/04/11.
 */
class PointcutLearningTest {

    @DisplayName("메소드 시그니처")
    @Test
    void methodSignature() throws NoSuchMethodException {
        // given
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
                "execution(public int com.pplenty.studytoby.chapter06.aop.Target.minus(int,int) throws java.lang.RuntimeException)");

        // when then
        assertThat(
                pointcut.getClassFilter().matches(Target.class)
                        && pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null))
                .isTrue();

        assertThat(
                pointcut.getClassFilter().matches(Target.class)
                        && pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null))
                .isFalse();

        assertThat(
                pointcut.getClassFilter().matches(Bean.class)
                        && pointcut.getMethodMatcher().matches(Bean.class.getMethod("method"), null))
                .isFalse();

        // then
    }
}