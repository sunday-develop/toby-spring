package com.pplenty.studytoby.chapter06.proxy;

/**
 * Created by yusik on 2020/04/04.
 */
public class HelloTargetWithoutInterface {

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public String sayHi(String name) {
        return "Hi " + name;
    }

    public String sayThankYou(String name) {
        return "Thank you " + name;
    }
}
