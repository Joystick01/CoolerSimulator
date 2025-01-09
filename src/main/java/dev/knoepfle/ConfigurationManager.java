package dev.knoepfle;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private final Map<String, Object> configuration = new HashMap<>();

    private final String[] parameters = {
            "ERROR_RATE",
            "DUPPLICATE_RATE",
            "PAYLOAD_WRITER_TYPE",
            "PAYLOAD_WRITER_OFFSET",
            "CONSOLE_PAYLOAD_WRITER_LIMIT",
            "FILE_PAYLOAD_WRITER_PATH",
            "FILE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT",
            "FILE_PAYLOAD_WRITER_FILE_COUNT",
            "DATALAKE_PAYLOAD_WRITER_ENDPOINT",
            "DATALAKE_PAYLOAD_WRITER_SAS_TOKEN",
            "DATALAKE_PAYLOAD_WRITER_FILESYSTEM",
            "DATALAKE_PAYLOAD_WRITER_FILE_MESSAGE_COUNT",
            "DATALAKE_PAYLOAD_WRITER_FILE_COUNT",
            "KAFKA_PAYLOAD_WRITER_TOPIC",
            "KAFKA_PAYLOAD_WRITER_BOOTSTRAP_SERVERS",
            "KAFKA_PAYLOAD_WRITER_FLOOD_MESSAGES",
            "KAFKA_PAYLOAD_WRITER_SEND_RATE"
    };

    private ConfigurationManager() {
        for (String parameter : parameters) {
            configuration.put(parameter, System.getenv(parameter));
        }
        configuration.putIfAbsent("PAYLOAD_WRITER_OFFSET", "0");
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public String getString(String key) {
        if (configuration.get(key) == null) {
            throw new IllegalArgumentException("Configuration key not found: " + key);
        }
        return (String) configuration.get(key);
    }

    public int getInt(String key) {
        if (configuration.get(key) == null) {
            throw new IllegalArgumentException("Configuration key not found: " + key);
        }
        return Integer.parseInt((String) configuration.get(key));
    }
}