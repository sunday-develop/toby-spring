package com.study.spring.learningtest.GoodEncapsulation;

import org.junit.jupiter.api.Test;

public class OneClassEncapsulation {

    @Test
    public void take() {
        ColdPatient coldPatient = new ColdPatient();
        coldPatient.takeSinue(new SinusCap());
    }
}
