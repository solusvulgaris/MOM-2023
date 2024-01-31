package com.ak.spring.kafkastreams.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsTask2Config {
    @Value("${kafka.task2.topic}")
    private String topic2;

    @Value("${kafka.words-short.topic}")
    private String wordsShortTopic;

    @Value("${kafka.words-long.topic}")
    private String wordsLongTopic;

    @Bean
    public NewTopic topicTask2() {
        return TopicBuilder.name(topic2)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicWordsShort() {
        return TopicBuilder.name(wordsShortTopic)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicWordsLong() {
        return TopicBuilder.name(wordsLongTopic)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean(name = "sentenceStream")
    public Map<String, KStream<Integer, String>> sentenceStream(StreamsBuilder kStreamBuilder) {
        KStream<Integer, String> topic2Stream = kStreamBuilder.stream(topic2, Consumed.with(Serdes.Integer(), Serdes.String()));

        KStream<Integer, String> filteredStream = topic2Stream.filter((key, value) -> value != null);

        Map<String, KStream<Integer, String>> wordStreams = filteredStream
                .flatMapValues(value -> Arrays.asList(value.split("\\s+")))
                .filter((key, value) -> !value.trim().isEmpty())
                .map((key, value) -> new KeyValue<>(value.length(), value))
                .peek((key, value) -> log.info("Key: {}, Value: {}", key, value))
                .split(Named.as("words-"))
                .branch((key, value) -> value.length() < 10, Branched.as("short"))
                .branch((key, value) -> value.length() >= 10, Branched.as("long"))
                .defaultBranch();

        KStream<Integer, String> shortWordsStream = wordStreams.get("words-short");
        KStream<Integer, String> longWordsStream = wordStreams.get("words-long");

        shortWordsStream.to(wordsShortTopic);
        longWordsStream.to(wordsLongTopic);

        return wordStreams;
    }

    @Bean()
    @DependsOn("sentenceStream")
    public void filteredASentenceStream() {
        Map<String, KStream<Integer, String>> nonFilteredSentenceStreams = sentenceStream(new StreamsBuilder());

        Map<String, KStream<Integer, String>> filteredStreams = new HashMap<>();

        for (Map.Entry<String, KStream<Integer, String>> entry : nonFilteredSentenceStreams.entrySet()) {
            String streamName = entry.getKey();
            KStream<Integer, String> stream = entry.getValue();

            KStream<Integer, String> filteredStream = stream
                    .filter((key, value) -> value.contains("a"));

            filteredStreams.put(streamName, filteredStream);

            filteredStream
                    .peek((key, value) ->
                            log.info("branch map entry: {} with 'a' filtered message - key: {} and value: {} ", streamName, key, value));
        }
    }

    @Bean
    public KStream<Integer, String> mergedStream(StreamsBuilder kStreamBuilder) {
        KStream<Integer, String> shortWordsStream = kStreamBuilder.stream(wordsShortTopic, Consumed.with(Serdes.Integer(), Serdes.String()));
        KStream<Integer, String> longWordsStream = kStreamBuilder.stream(wordsLongTopic, Consumed.with(Serdes.Integer(), Serdes.String()));

        KStream<Integer, String> mergedStream = shortWordsStream.merge(longWordsStream);
        mergedStream.peek((key, value) -> log.info("Merged: Key: " + key + ", Value: " + value));

        return mergedStream;
    }
}
