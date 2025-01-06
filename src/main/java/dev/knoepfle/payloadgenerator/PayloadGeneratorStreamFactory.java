package dev.knoepfle.payloadgenerator;

import java.util.stream.Stream;

public class PayloadGeneratorStreamFactory {

        PayloadGenerator payloadGenerator = new PayloadGenerator();


        public Stream<String> generateStream() {
            return Stream.generate(() -> payloadGenerator.generate());
        }
}
