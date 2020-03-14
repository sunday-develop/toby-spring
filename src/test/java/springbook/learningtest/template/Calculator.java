package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filePath) throws IOException {
        final LineCallback<Integer> sumCallback = ((line, value) -> value + Integer.parseInt(line));
        return lineReadTemplate(filePath, sumCallback, 0);
    }

    public int calcMultiply(String filePath) throws IOException {
        final LineCallback<Integer> multiplyCallback = (line, value) -> value * Integer.parseInt(line);
        return lineReadTemplate(filePath, multiplyCallback, 1);
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            T res = initVal;
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isBlank()) {
                    break;
                }
                res = callback.doSomethingWithLine(line, res);
            }

            return res;
        }
    }

    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            return callback.doSomethingWithReader(br);
        }
    }

}
