package com.toby.tobyspring.learningtest.factorybean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application-factory.xml")
@DisplayName("다이내믹 프록시를 위한 팩토리 빈 학습 테스트")
class FactoryBeanTest {
    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("팩토리 빈 학습 테스트")
    public void factoryBean() {
        Object message = applicationContext.getBean("message");
        assertEquals(Message.class, message.getClass());
        assertEquals("Factory Bean", ((Message) message).getText());
    }

    @Test
    @DisplayName("팩토리 빈을 가져오는 기능 테스트")
    public void getFactoryBean() {
        Object factory = applicationContext.getBean("&message");
        assertEquals(MessageFactoryBean.class, factory.getClass());
    }
}