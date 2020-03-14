package com.toby.tobyspring.learnigtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        return this.fileReadTemplate(filepath, br -> {
            Integer sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                sum += Integer.valueOf(line);
            }
            return sum;
        });
    }

    public Integer calcMultiply(String filepath) throws IOException {
        return this.fileReadTemplate(filepath, br -> {
           Integer multiply = 1;
           String line = null;
           while ((line = br.readLine()) != null) {
               multiply *= Integer.valueOf(line);
           }
           return multiply;
        });
    }

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            return callback.doSomethingWithReader(br);
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
