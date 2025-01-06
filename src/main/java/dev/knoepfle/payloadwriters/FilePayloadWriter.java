package dev.knoepfle.payloadwriters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FilePayloadWriter implements PayloadWriter<FilePayloadWriter> {

    BufferPayloadWriter bufferPayloadWriter;
    int fileMessageCount;
    int messageCount;

    public FilePayloadWriter(Stream<String> stream, int fileMessageCount, int messageCount, Path path) {

        this.bufferPayloadWriter = new BufferPayloadWriter(stream, fileMessageCount);
        this.fileMessageCount = fileMessageCount;
        this.messageCount = messageCount;

    }

    @Override
    public FilePayloadWriter write() throws IOException {

        int numberOfFiles = (int) Math.ceil((double) messageCount / fileMessageCount);
        File file;
        FileOutputStream fileOutputStream;

        for (int i = 0; i < numberOfFiles; i++) {
            file = new File(i + ".json");
            fileOutputStream = new FileOutputStream(file);

            bufferPayloadWriter.write();
            fileOutputStream.write(bufferPayloadWriter.getBuffer());
            fileOutputStream.close();
            bufferPayloadWriter.resetBuffer();
        }

        return this;
    }
}
