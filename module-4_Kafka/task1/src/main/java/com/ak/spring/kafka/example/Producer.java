package com.ak.spring.kafka.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class Producer {

    private final org.apache.kafka.clients.producer.KafkaProducer<String, String> kafkaProducer;

    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        PROPERTIES.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        PROPERTIES.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        PROPERTIES.setProperty(ProducerConfig.RETRIES_CONFIG, "0");
        PROPERTIES.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "10");
        PROPERTIES.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
    }

    public Producer(String bootstrapServers) {
        PROPERTIES.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        this.kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<>(PROPERTIES);
    }

    public void produce(String topic, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

        kafkaProducer.send(producerRecord, (metadata, exception) -> {
            if (exception == null) {
                log.info(
                        "Request completed: Key: " + key +
                                " | Partition: " + metadata.partition() +
                                " | Offset: " + metadata.offset() +
                                " | Timestamp: " + metadata.timestamp()
                );
            } else {
                log.error("Error while producing " + exception);
            }
        });

    }

    public void close() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
