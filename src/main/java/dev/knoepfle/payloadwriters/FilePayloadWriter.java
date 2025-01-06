package dev.knoepfle.payloadwriters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FilePayloadWriter implements PayloadWriter {

    BufferPayloadWriter bufferPayloadWriter;
    int fileMessageCount;
    int fileCount;
    Path path;

    public FilePayloadWriter(Stream<String> stream, int fileMessageCount, int fileCount, Path path) {

        this.bufferPayloadWriter = new BufferPayloadWriter(stream, fileMessageCount);
        this.fileMessageCount = fileMessageCount;
        this.fileCount = fileCount;
        this.path = path;
    }

    @Override
    public void write() throws IOException {

        File file;
        FileOutputStream fileOutputStream;
        int padding = (int) (Math.log10(fileCount) + 1);
        Files.createDirectories(path);

        for (int i = 0; i < fileCount; i++) {
            file = new File(path.resolve(String.format("%0" + padding + "d.json", i)).toString());
            fileOutputStream = new FileOutputStream(file);

            bufferPayloadWriter.write();
            fileOutputStream.write(bufferPayloadWriter.getBuffer());
            fileOutputStream.close();
        }
    }
}
