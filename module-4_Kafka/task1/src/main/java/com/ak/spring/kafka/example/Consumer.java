package com.ak.spring.kafka.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class Consumer {
    private final org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;

    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        PROPERTIES.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        PROPERTIES.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        PROPERTIES.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "101");
        PROPERTIES.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "135");
        PROPERTIES.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        PROPERTIES.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        PROPERTIES.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000");
    }

    public Consumer(String bootstrapServers, String groupId, String topic) {
        PROPERTIES.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        PROPERTIES.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        this.kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(PROPERTIES);

        kafkaConsumer.subscribe(Collections.singleton(topic));
    }

    public ConsumerRecords<String, String> consume(long durationMs) {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(durationMs));
        for (ConsumerRecord<String, String> recordEntry : records) {
            log.info(
                    "Consumer received - " +
                            "Key: " + recordEntry.key() + " " +
                            "Value: " + recordEntry.value() + " " +
                            "Offset: " + recordEntry.offset() + " " +
                            "Partition: " + recordEntry.partition() + " "
            );
        }
        return records;
    }

    public void close() {
        kafkaConsumer.close();
    }
}
