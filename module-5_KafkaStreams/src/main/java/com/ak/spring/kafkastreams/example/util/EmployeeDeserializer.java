package com.ak.spring.kafkastreams.example.util;

import com.ak.spring.kafkastreams.example.data.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class EmployeeDeserializer implements Deserializer<Employee> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Employee deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.warn("Null received during deserialization.");
                return null;
            }
            log.info("Deserializing...");
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), Employee.class);
        } catch (Exception e) {
            throw new SerializationException("Error while deserializing byte[] to Employee.");
        }
    }

    @Override
    public Employee deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
