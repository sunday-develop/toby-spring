package com.pplenty.studytoby.chapter06.factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by yusik on 2020/04/05.
 */
public class MessageFactoryBean implements FactoryBean<Message> {

    String text;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Message getObject() throws Exception {
        return Message.getMessage(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
