package com.pplenty.studytoby.chapter06.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("팩토리 빈 학습테스트")
@SpringBootTest
@ContextConfiguration(locations = "classpath:chapter06/factoryBeanTest-context.xml")
class FactoryBeanLeaningTest {

    @Autowired
    ApplicationContext context;

    @DisplayName("팩토리 빈 테스트")
    @Test
    void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message.getClass()).isEqualTo(Message.class);
        assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }

    @DisplayName("팩토리 빈 반환 테스트")
    @Test
    void getFactoryBean() {
        Object message = context.getBean("&message");
        assertThat(message.getClass()).isEqualTo(MessageFactoryBean.class);
    }
}