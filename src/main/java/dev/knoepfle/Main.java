package dev.knoepfle;

import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        PayloadGeneratorStreamFactory payloadGeneratorStreamFactory = new PayloadGeneratorStreamFactory();
        Stream<String> stream = payloadGeneratorStreamFactory.generateStream();
    }
}