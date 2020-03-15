package com.study.spring.learningtest.GoodEncapsulation;

import com.study.spring.learningtest.BadEncapsulation.SinivelCap;
import com.study.spring.learningtest.BadEncapsulation.SneezeCap;
import com.study.spring.learningtest.BadEncapsulation.SnuffleCap;

public class ColdPatient {

    public void takeSinue(SinusCap sinusCap) {
        sinusCap.take();
    }
}
