package springbook.learningtest.spring.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTestConfig {

    @Bean
    public FactoryBean<Message> message() {
        return new MessageFactoryBean("Factory Bean");
    }

}
