package dev.knoepfle.payloadwriters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

public interface PayloadWriter<SELF extends PayloadWriter<SELF>> {
    public SELF write() throws IOException;
}
