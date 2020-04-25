package com.pplenty.studytoby.sqlservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by yusik on 2020/04/26.
 */
@Configuration
@Profile("prod")
public class ProductionAppContext {
    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("alt1.gmail-smtp-in.l.google.com");
        return mailSender;
    }
}
