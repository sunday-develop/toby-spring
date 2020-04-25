package com.study.spring.factorybean;

import com.study.spring.config.TestApplicationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplicationContext.class)
public class FactoryBeanTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertEquals(message.getClass(), Message.class);
        assertEquals(((Message) message).getText(), "Factory Bean");
    }
}
