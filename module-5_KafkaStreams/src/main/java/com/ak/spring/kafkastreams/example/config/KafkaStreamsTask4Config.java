package com.ak.spring.kafkastreams.example.config;

import com.ak.spring.kafkastreams.example.data.Employee;
import com.ak.spring.kafkastreams.example.util.CustomSerdes;
import com.ak.spring.kafkastreams.example.util.EmployeeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsTask4Config {

    @Value("${kafka.task4.topic}")
    private String topic4;

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic topicTask4() {
        return TopicBuilder.name(topic4)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean(name = "task4Stream")
    public KStream<String, Employee> task4Stream(StreamsBuilder streamsBuilder) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "task4-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);

        KStream<String, Employee> employeeStream =
                streamsBuilder.stream(topic4, Consumed.with(Serdes.String(), CustomSerdes.employeeSerde()));

        employeeStream.filter((key, value) -> value != null)
                .peek((key, value) -> log.info("Received employee message key:{}, value:{}", key, value));

        return employeeStream;
    }

    @Bean
    public ProducerFactory<String, Employee> employeeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "kafkaEmployeeDtoTemplate")
    public KafkaTemplate<String, Employee> kafkaDistanceInfoTemplate() {
        return new KafkaTemplate<>(employeeProducerFactory());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EmployeeSerializer.class);
        return props;
    }
}
