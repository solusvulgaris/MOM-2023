package com.ak.spring.kafka.example;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class KafkaContainerTest {
    private static final DockerImageName KAFKA_TEST_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.3.2");
    public static final String KEY = "test_id_1";
    public static final String VALUE = "test_value";

    @Test
    public void testKafkaContainers() throws ExecutionException, InterruptedException, TimeoutException {
        try (KafkaContainer kafka = new KafkaContainer(KAFKA_TEST_IMAGE)) {
            kafka.start();

            final String bootstrapServers = kafka.getBootstrapServers();
            final int partitions = 2;
            final int replicationFactor = 1;
            String topic = "task1_topic";
            String groupId = "task1-consumer-test-containers";

            Producer producer = new Producer(bootstrapServers);
            Consumer consumer = new Consumer(bootstrapServers, groupId, topic);

            try (AdminClient adminClient = AdminClient.create(ImmutableMap.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
                Collection<NewTopic> topics = Collections.singletonList(new NewTopic(topic, partitions, (short) replicationFactor));
                adminClient.createTopics(topics).all().get(30, TimeUnit.SECONDS);

                producer.produce(topic, KEY, VALUE);
                final ConsumerRecords<String, String> records = consumer.consume(10000);

                assertThat(records)
                        .hasSize(1)
                        .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                        .containsExactly(tuple(topic, KEY, VALUE));
            } finally {
                producer.close();
                consumer.close();
            }
        }
    }
}
