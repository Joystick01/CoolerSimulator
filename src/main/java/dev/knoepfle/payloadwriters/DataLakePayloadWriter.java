package dev.knoepfle.payloadwriters;

import com.azure.core.util.BinaryData;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClientBuilder;

import java.util.stream.Stream;

public class DataLakePayloadWriter implements PayloadWriter {

    private final DataLakeFileSystemClient dataLakeFileSystemClient;
    private DataLakeFileClient dataLakeFileClient;

    private final BufferPayloadWriter bufferPayloadWriter;
    private final int fileCount;

    public DataLakePayloadWriter(Stream<String> stream, String endpoint, String sasToken, String fileSystem, int fileMessageCount, int fileCount) {
        this.fileCount = fileCount;
        this.bufferPayloadWriter = new BufferPayloadWriter(stream, fileMessageCount);
        this.dataLakeFileSystemClient = new DataLakeFileSystemClientBuilder()
            .endpoint(endpoint)
            .sasToken(sasToken)
            .fileSystemName(fileSystem)
            .buildClient();
    }

    @Override
    public void write() {
        int padding = (int) (Math.log10(fileCount) + 1);
        for (int i = 0; i < fileCount; i++) {
            System.out.println("Uploading file " + i + " / " + fileCount);
            dataLakeFileClient = dataLakeFileSystemClient.getFileClient(String.format("%0" + padding + "d.json", i));
            bufferPayloadWriter.write();
            dataLakeFileClient.upload(BinaryData.fromBytes(bufferPayloadWriter.getBuffer()));
        }
    }
}
