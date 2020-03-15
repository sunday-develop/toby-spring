package com.study.spring.learningtest.BadEncapsulation;

public class ColdPatient {
    public void takeSinivelCal(SinivelCap cap) {
        cap.take();
    }

    public void takeSneezeCap(SneezeCap cap) {
        cap.take();
    }

    public void takeSnuffleCap(SnuffleCap cap) {
        cap.take();
    }
}
