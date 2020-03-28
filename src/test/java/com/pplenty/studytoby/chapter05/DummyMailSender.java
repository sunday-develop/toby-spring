package com.pplenty.studytoby.chapter05;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by yusik on 2020/03/29.
 */
public class DummyMailSender implements MailSender {
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        System.out.println("[DummyMailServer]");
        System.out.println(simpleMessage);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        System.out.println("[DummyMailServer]");
        for (SimpleMailMessage simpleMessage : simpleMessages) {
            System.out.println(simpleMessage);
        }
    }
}
