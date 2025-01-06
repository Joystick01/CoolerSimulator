package dev.knoepfle.payloadwriters;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class BufferPayloadWriter implements PayloadWriter<BufferPayloadWriter> {

    Iterator<String> stream;
    int messageCount;
    ByteArrayOutputStream buffer;


    public BufferPayloadWriter(Stream<String> stream, int messageCount) {
        this.stream = stream.iterator();
        this.messageCount = messageCount;
        this.buffer = new ByteArrayOutputStream(620 * messageCount);
    }

    @Override
    public BufferPayloadWriter write() {
        for (int i = 0; i < messageCount; i++) {
            byte[] bytes = stream.next().getBytes();
            buffer.write(bytes, 0, bytes.length);
        }
        return this;
    }

    public byte[] getBuffer() {
        return buffer.toByteArray();
    }

    public BufferPayloadWriter resetBuffer() {
        buffer.reset();
        return this;
    }

    protected ByteArrayOutputStream getBufferStream() {
        return buffer;
    }

}
