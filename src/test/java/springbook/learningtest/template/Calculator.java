package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public int calcMultiplyWithBiFunction(String filePath) throws IOException {
        final BiFunction<String, Integer, Integer> multiplyCallback = (line, value) -> value * Integer.parseInt(line);
        return lineReadTemplateWithBiFunction(filePath, multiplyCallback, 1);
    }

    public <T> T lineReadTemplateWithBiFunction(String filePath, BiFunction<String, T, T> function, T initVal) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            T res = initVal;
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isBlank()) {
                    break;
                }
                res = function.apply(line, res);
            }

            return res;
        }
    }

    public int calcMultiplyWithStream(String filePath) throws IOException {
        final Function<String, Integer> mappingFunction = Integer::parseInt;
        final BinaryOperator<Integer> operator = (a, b) -> a * b;

        return lineReadTemplateWithStream(filePath, mappingFunction, operator, 1);
    }

    public <T> T lineReadTemplateWithStream(String filePath, Function<String, T> mappingFunction, BinaryOperator<T> operator, T initVal) throws IOException {
        try (final Stream<String> lines = Files.lines(Paths.get(filePath));) {
            return lines.map(mappingFunction)
                    .reduce(initVal, operator);
        }
    }


    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            return callback.doSomethingWithReader(br);
        }
    }

}
