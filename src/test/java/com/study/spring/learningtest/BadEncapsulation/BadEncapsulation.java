package com.study.spring.learningtest.BadEncapsulation;

import org.junit.jupiter.api.Test;

public class BadEncapsulation {


    // 캡슐화가 무너지면 이렇듯 클래스 사용 방법과 관련하여 알아야할 사항들이 많이 등장한다.
    // - 복용해야 할 약의 종류
    // - 복용해야 할 약의 순서
    @Test
    public void take() {
        ColdPatient coldPatient = new ColdPatient();
        coldPatient.takeSinivelCal(new SinivelCap());
        coldPatient.takeSneezeCap(new SneezeCap());
        coldPatient.takeSnuffleCap(new SnuffleCap());
    }
}
