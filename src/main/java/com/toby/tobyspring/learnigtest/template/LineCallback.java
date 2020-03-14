package com.toby.tobyspring.learnigtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
