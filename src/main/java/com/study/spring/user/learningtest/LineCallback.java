package com.study.spring.user.learningtest;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
