package com.study.spring.user.learningtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calSum(final String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> Integer.valueOf(line) + value, 0);
    }

    public Integer calMultiply(final String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> Integer.valueOf(line) * value, 1);
    }

    public String concatenate(String filepath) throws IOException {
        return lineReadTemplate(filepath, (line, value) -> line + value, "");
    }

    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T intiVal) throws IOException {

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = intiVal;
            String line;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
