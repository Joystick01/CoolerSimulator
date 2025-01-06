package dev.knoepfle.payloadgenerator;

import java.util.stream.Stream;

public class PayloadGeneratorStreamFactory {

    PayloadGenerator payloadGenerator;

    public PayloadGeneratorStreamFactory() {
        payloadGenerator = new PayloadGenerator(0);
    }

    public PayloadGeneratorStreamFactory(int offset) {
        payloadGenerator = new PayloadGenerator(offset);
    }

    public Stream<String> generateStream() {
        return Stream.generate(() -> payloadGenerator.generate());
    }
}
