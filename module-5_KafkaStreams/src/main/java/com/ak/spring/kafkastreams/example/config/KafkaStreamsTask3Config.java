package com.ak.spring.kafkastreams.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

import java.time.Duration;

@Slf4j
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsTask3Config {
    @Value("${kafka.task3-1.topic}")
    private String topic3_1;

    @Value("${kafka.task3-2.topic}")
    private String topic3_2;

    @Value("${kafka.task3-result.topic}")
    private String topic3_result;

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic topicTask3_1() {
        return TopicBuilder.name(topic3_1)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicTask3_2() {
        return TopicBuilder.name(topic3_2)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicTask3_result() {
        return TopicBuilder.name(topic3_result)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public KStream<Integer, String> kafkaJoinStreams(StreamsBuilder streamsBuilder) {
        KStream<Integer, String> task3_1Stream = streamsBuilder.stream(topic3_1,
                        Consumed.with(Serdes.Integer(), Serdes.String()))
                .filter((key, value) -> value != null && value.contains(":"))
                .selectKey((key, value) -> Integer.parseInt(value.split(":")[0]))
                .peek((key, value) ->
                        log.info("Stream {} received {}:{}", topic3_1, key, value));

        KStream<Integer, String> task3_2Stream = streamsBuilder.stream(topic3_2,
                        Consumed.with(Serdes.Integer(), Serdes.String()))
                .filter((key, value) -> value != null && value.contains(":"))
                .selectKey((key, value) -> Integer.parseInt(value.split(":")[0]))
                .peek((key, value) ->
                        log.info("Stream {} received {}:{}", topic3_2, key, value));

        ValueJoiner<String, String, String> valueJoiner = (leftValue, rightValue) -> {
            return leftValue + " JOINED WITH " + rightValue;
        };

        final KStream<Integer, String> joinedStream = task3_1Stream.join(
                task3_2Stream,
                valueJoiner,
                JoinWindows.ofTimeDifferenceAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(30))
        );
        joinedStream
                .peek((key, value) -> log.info("Joined message key: {} value: {}", key, value));

        joinedStream
                .to(topic3_result, Produced.with(Serdes.Integer(), Serdes.String()));

        return joinedStream;
    }
}
