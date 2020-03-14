package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filePath) throws IOException {
        final BufferedReaderCallback sumCallback = br -> {
            int sum = 0;
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isBlank()) {
                    break;
                }

                sum += Integer.parseInt(line);
            }
            return sum;
        };
        return fileReadTemplate(filePath, sumCallback);
    }

    public int calcMultiply(String filePath) throws IOException {
        final BufferedReaderCallback multiplyCallback = br -> {
            int multiply = 1;
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isBlank()) {
                    break;
                }

                multiply *= Integer.parseInt(line);
            }
            return multiply;
        };
        return fileReadTemplate(filePath, multiplyCallback);
    }

    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            return callback.doSomethingWithReader(br);
        }
    }

}
