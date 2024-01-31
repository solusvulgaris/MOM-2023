package com.ak.spring.kafkastreams.example;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaStreamsTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<Integer, String> inputTopic;
    private TestOutputTopic<Integer, String> outputTopic;

    @BeforeEach
    public void init() {
        Properties props = new Properties();
        props.put(APPLICATION_ID_CONFIG, "my-test-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");
        props.put(
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                Serdes.Integer().getClass().getName());
        props.put(
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<Integer, String> myFirstStream = builder.stream(
                "test-topic-1",
                Consumed.with(Serdes.Integer(), Serdes.String()));
        myFirstStream.to("test-topic-2");

        testDriver = new TopologyTestDriver(builder.build(), props);
        inputTopic = testDriver.createInputTopic(
                "test-topic-1",
                new IntegerSerializer(),
                new StringSerializer());
        outputTopic = testDriver.createOutputTopic(
                "test-topic-2",
                new IntegerDeserializer(),
                new StringDeserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void firstTaskTest() {
        StreamsBuilder builder = new StreamsBuilder();

        inputTopic.pipeInput(1, "TestMessage");
        KeyValue<Integer, String> result = outputTopic.readKeyValue();

        assertEquals(1, (int) result.key);
        assertEquals("TestMessage", result.value);
    }
}
