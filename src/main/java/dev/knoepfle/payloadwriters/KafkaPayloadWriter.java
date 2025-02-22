package dev.knoepfle.payloadwriters;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Stream;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaPayloadWriter implements PayloadWriter {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaPayloadWriter.class);

    final Properties props;
    final String topic;
    final Iterator<String[]> streamIterator;
    int floodMessages = 0;
    int sendRate = Integer.MAX_VALUE;
    final RateLimiter rateLimiter;

    public KafkaPayloadWriter(Stream<String[]> stream, String topic, String bootstrapServers) {
        logger.info("Creating KafkaPayloadWriter with topic: {} and bootstrap servers: {}", topic, bootstrapServers);
        this.props = new Properties() {{
            put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            put(KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class.getCanonicalName());
            put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(ACKS_CONFIG,                   "all");
        }};
        this.topic = topic;
        this.streamIterator = stream.iterator();
        this.rateLimiter = RateLimiter.create(sendRate);
    }

    public KafkaPayloadWriter(Stream<String[]> stream, int floodMessages, int sendRate, String topic, String bootstrapServers){
        logger.info("Creating KafkaPayloadWriter with topic: {}, bootstrap servers: {}, flood messages: {}, and send rate: {}", topic, bootstrapServers, floodMessages, sendRate);
        this.props = new Properties() {{
            put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            put(KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class.getCanonicalName());
            put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(ACKS_CONFIG,                   "all");
        }};
        this.topic = topic;
        this.streamIterator = stream.iterator();
        this.floodMessages = floodMessages;
        this.sendRate = sendRate;
        this.rateLimiter = RateLimiter.create(sendRate);
    }

    @Override
    public void write() {
        logger.info("Starting to write to Kafka topic: {}", topic);
        try(Producer<String, String> producer = new KafkaProducer<>(props)) {
            String[] kv;
            for (int i = 0; i < floodMessages; i++) {
                kv = streamIterator.next();
                producer.send(new ProducerRecord<>(topic, kv[0], kv[1]));
            }
            logger.info("Flooded {} messages to Kafka topic: {}", floodMessages, topic);
            while (streamIterator.hasNext()) {
                rateLimiter.acquire();
                kv = streamIterator.next();
                producer.send(new ProducerRecord<>(topic, kv[0], kv[1]));
            }
        }
    }

}
