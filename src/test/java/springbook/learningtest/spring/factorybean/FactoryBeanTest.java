package springbook.learningtest.spring.factorybean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = MessageTestConfig.class)
@ExtendWith(SpringExtension.class)
public class FactoryBeanTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void getMessageFromFactoryBean() throws Exception {
        final Object message = context.getBean("message");

        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
    }

    @Test
    void getFactoryBean() throws Exception {
        final Object factory = context.getBean("&message");
        assertThat(factory).isInstanceOf(MessageFactoryBean.class);
    }

}
