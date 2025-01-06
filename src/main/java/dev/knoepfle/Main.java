package dev.knoepfle;

import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;
import dev.knoepfle.payloadwriters.KafkaPayloadWriter;
import dev.knoepfle.payloadwriters.PayloadWriter;
import dev.knoepfle.payloadwriters.PayloadWriterFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        PayloadWriterFactory payloadWriterFactory = new PayloadWriterFactory();
        PayloadWriter payloadWriter = payloadWriterFactory.getPayloadWriter();
        payloadWriter.write();
    }
}