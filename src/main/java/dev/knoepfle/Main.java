package dev.knoepfle;

import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;
import dev.knoepfle.payloadwriters.KafkaPayloadWriter;
import dev.knoepfle.payloadwriters.PayloadWriter;
import dev.knoepfle.payloadwriters.PayloadWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Application started");
        PayloadWriterFactory payloadWriterFactory = new PayloadWriterFactory();
        PayloadWriter payloadWriter = payloadWriterFactory.getPayloadWriter();
        payloadWriter.write();
    }
}