package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filePath) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            int sum = 0;
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isBlank()) {
                    break;
                }

                sum += Integer.parseInt(line);
            }
            return sum;
        }
    }

}
