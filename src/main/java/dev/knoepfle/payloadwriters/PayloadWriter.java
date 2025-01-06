package dev.knoepfle.payloadwriters;

import java.io.IOException;

public interface PayloadWriter {
    void write() throws IOException;
}
