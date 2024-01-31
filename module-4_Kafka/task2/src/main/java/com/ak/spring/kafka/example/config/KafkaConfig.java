package com.ak.spring.kafka.example.config;

import com.ak.spring.kafka.example.beans.DistanceInfo;
import com.ak.spring.kafka.example.beans.TaxiSignal;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${producer.bootstrap.servers}")
    private String producerBootstrapServers;

    @Bean
    public ProducerFactory<String, TaxiSignal> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, DistanceInfo> distanceInfoProducerFactoryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "kafkaSequentialTemplate")
    public KafkaTemplate<String, TaxiSignal> kafkaSequentialTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean(name = "kafkaDistanceInfoTemplate")
    public KafkaTemplate<String, DistanceInfo> kafkaDistanceInfoTemplate() {
        return new KafkaTemplate<>(distanceInfoProducerFactoryProducerFactory());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, TaxiPartitioner.class.getName());
        return props;
    }
}
