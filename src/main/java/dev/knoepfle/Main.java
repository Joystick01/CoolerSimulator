package dev.knoepfle;

import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;
import dev.knoepfle.payloadwriters.BufferPayloadWriter;
import dev.knoepfle.payloadwriters.ConsolePayloadWriter;
import dev.knoepfle.payloadwriters.FilePayloadWriter;
import dev.knoepfle.payloadwriters.PayloadWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        PayloadGeneratorStreamFactory payloadGeneratorStreamFactory = new PayloadGeneratorStreamFactory();
        Stream<String> stream = payloadGeneratorStreamFactory.generateStream();
        System.out.println(Paths.get(".").toAbsolutePath().toString());
        FilePayloadWriter filePayloadWriter = new FilePayloadWriter(stream, 3, 5, Paths.get("."));
        filePayloadWriter.write();
    }
}