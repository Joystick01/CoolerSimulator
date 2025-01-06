package dev.knoepfle.payloadwriters;

import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;

import java.nio.file.Path;

public class PayloadWriterFactory {

    String type;
    int offset;

    public PayloadWriterFactory() {
        if (System.getenv("PAYLOAD_WRITER_TYPE") == null) {
            throw new IllegalArgumentException("Payload writer type not set");
        }
        this.type = System.getenv("PAYLOAD_WRITER_TYPE").toUpperCase();
        this.offset = System.getenv("PAYLOAD_WRITER_OFFSET") == null ? 0: Integer.parseInt(System.getenv("PAYLOAD_WRITER_OFFSET"));
    }

    PayloadGeneratorStreamFactory payloadGeneratorStreamFactory = new PayloadGeneratorStreamFactory(offset);

    public PayloadWriter getPayloadWriter() {
        switch (type) {
            case "CONSOLE":
                if (System.getenv("CONSOLE_PAYLOAD_WRITER_LIMIT") == null) {
                    throw new IllegalArgumentException("Console payload writer limit not set");
                }
                return new ConsolePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        Integer.parseInt(System.getenv("CONSOLE_PAYLOAD_WRITER_LIMIT"))
                );
            case "FILE":
                if (System.getenv("FILE_PAYLOAD_WRITER_PATH") == null) {
                    throw new IllegalArgumentException("File payload writer path not set");
                } else if(System.getenv("FILE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT") == null) {
                    throw new IllegalArgumentException("File payload writer file message count not set");
                } else if(System.getenv("FILE_PAYLOAD_WRITER_FILE_COUNT") == null) {
                    throw new IllegalArgumentException("File payload writer file count not set");
                }
                return new FilePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        Integer.parseInt(System.getenv("FILE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT")),
                        Integer.parseInt(System.getenv("FILE_PAYLOAD_WRITER_FILE_COUNT")),
                        Path.of(System.getenv("FILE_PAYLOAD_WRITER_PATH"))
                );
            case "DATALAKE":
                if (System.getenv("DATALAKE_PAYLOAD_WRITER_ENDPOINT") == null) {
                    throw new IllegalArgumentException("DataLake payload writer endpoint not set");
                } else if(System.getenv("DATALAKE_PAYLOAD_WRITER_SAS_TOKEN") == null) {
                    throw new IllegalArgumentException("DataLake payload writer SAS token not set");
                } else if(System.getenv("DATALAKE_PAYLOAD_WRITER_FILESYSTEM") == null){
                    throw new IllegalArgumentException("DataLake payload writer filesystem not set");
                } else if(System.getenv("DATALAKE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT") == null) {
                    throw new IllegalArgumentException("DataLake payload writer file message count not set");
                } else if(System.getenv("DATALAKE_PAYLOAD_WRITER_FILE_COUNT") == null) {
                    throw new IllegalArgumentException("DataLake payload writer file count not set");
                }
                return new DataLakePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        System.getenv("DATALAKE_PAYLOAD_WRITER_ENDPOINT"),
                        System.getenv("DATALAKE_PAYLOAD_WRITER_SAS_TOKEN"),
                        System.getenv("DATALAKE_PAYLOAD_WRITER_FILESYSTEM"),
                        Integer.parseInt(System.getenv("DATALAKE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT")),
                        Integer.parseInt(System.getenv("DATALAKE_PAYLOAD_WRITER_FILE_COUNT"))
                );
            default:
                throw new IllegalArgumentException("Invalid payload writer type");
        }
    }
}
