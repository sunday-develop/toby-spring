package com.study.spring.learningtest.GoodEncapsulation;

public class SinusCap {

    void sniTake() {
        System.out.println("콧물이 납니다.");
    }

    void sneTake() {
        System.out.println("재채기가 멎습니다.");
    }

    void snuTake() {
        System.out.println("코가 뻥 뚫립니다.");
    }

    void take() {
        sniTake();
        sneTake();
        snuTake();
    }
}
