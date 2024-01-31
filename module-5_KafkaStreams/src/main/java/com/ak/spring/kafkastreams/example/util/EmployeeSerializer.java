package com.ak.spring.kafkastreams.example.util;

import com.ak.spring.kafkastreams.example.data.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class EmployeeSerializer implements Serializer<Employee> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Employee data) {
        try {
            if (data == null) {
                log.warn("Null received during serialization.");
                return null;
            }
            log.info("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error while serializing Employee to byte[]");
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Employee data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
