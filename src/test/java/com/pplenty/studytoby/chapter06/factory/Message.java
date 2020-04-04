package com.pplenty.studytoby.chapter06.factory;

/**
 * Created by yusik on 2020/04/05.
 */
public class Message {

    String text;

    private Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message getMessage(String text) {
        return new Message(text);
    }
}
