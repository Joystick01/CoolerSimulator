package dev.knoepfle.payloadwriters;

import java.util.Iterator;
import java.util.stream.Stream;

public class ConsolePayloadWriter implements PayloadWriter {

    private final Iterator<String> stream;
    int limit;

    public ConsolePayloadWriter(Stream<String> stream, int limit) {
        this.stream = stream.iterator();
        this.limit = limit;
    }

    @Override
    public void write() {
        for (int i = 0; i < limit; i++) {
            System.out.println(stream.next());
        }
    }
}
