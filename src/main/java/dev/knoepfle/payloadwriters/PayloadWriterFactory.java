package dev.knoepfle.payloadwriters;

import dev.knoepfle.ConfigurationManager;
import dev.knoepfle.payloadgenerator.PayloadGeneratorStreamFactory;

import java.nio.file.Path;

public class PayloadWriterFactory {

    ConfigurationManager configManager = ConfigurationManager.getInstance();;
    PayloadGeneratorStreamFactory payloadGeneratorStreamFactory = new PayloadGeneratorStreamFactory(configManager.getInt("PAYLOAD_WRITER_OFFSET"));

    public PayloadWriter getPayloadWriter() {
        switch (configManager.getString("PAYLOAD_WRITER_TYPE")) {
            case "CONSOLE":
                return new ConsolePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        configManager.getInt("CONSOLE_PAYLOAD_WRITER_LIMIT")
                );
            case "FILE":
                return new FilePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        configManager.getInt("FILE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT"),
                        configManager.getInt("FILE_PAYLOAD_WRITER_FILE_COUNT"),
                        Path.of(configManager.getString("FILE_PAYLOAD_WRITER_PATH"))
                );
            case "DATALAKE":
                return new DataLakePayloadWriter(
                        payloadGeneratorStreamFactory.generateStream(),
                        configManager.getString("DATALAKE_PAYLOAD_WRITER_ENDPOINT"),
                        configManager.getString("DATALAKE_PAYLOAD_WRITER_SAS_TOKEN"),
                        configManager.getString("DATALAKE_PAYLOAD_WRITER_FILESYSTEM"),
                        configManager.getInt("DATALAKE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT"),
                        configManager.getInt("DATALAKE_PAYLOAD_WRITER_FILE_COUNT")
                );
            case "KAFKA":
                return new KafkaPayloadWriter(
                        payloadGeneratorStreamFactory.generateKVStream(),
                        configManager.getInt("KAFKA_PAYLOAD_WRITER_FLOOD_MESSAGES"),
                        configManager.getInt("KAFKA_PAYLOAD_WRITER_SEND_RATE"),
                        configManager.getString("KAFKA_PAYLOAD_WRITER_TOPIC"),
                        configManager.getString("KAFKA_PAYLOAD_WRITER_BOOTSTRAP_SERVERS")
                );
            default:
                throw new IllegalArgumentException("Invalid payload writer type");
        }
    }
}