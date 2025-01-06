package dev.knoepfle.payloadwriters;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.stream.Stream;

public class BufferPayloadWriter implements PayloadWriter {

    Iterator<String> stream;
    int messageCount;
    ByteArrayOutputStream buffer;


    public BufferPayloadWriter(Stream<String> stream, int messageCount) {
        this.stream = stream.iterator();
        this.messageCount = messageCount;
        this.buffer = new ByteArrayOutputStream(620 * messageCount);
    }

    @Override
    public void write() {
        buffer.reset();
        for (int i = 0; i < messageCount; i++) {
            byte[] bytes = stream.next().getBytes();
            buffer.write(bytes, 0, bytes.length);
        }
    }

    public byte[] getBuffer() {
        return buffer.toByteArray();
    }

    protected ByteArrayOutputStream getBufferStream() {
        return buffer;
    }

}
