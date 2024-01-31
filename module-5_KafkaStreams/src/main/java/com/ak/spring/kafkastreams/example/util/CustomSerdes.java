package com.ak.spring.kafkastreams.example.util;

import com.ak.spring.kafkastreams.example.data.Employee;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class CustomSerdes {
    private CustomSerdes() {}

    public static Serde<Employee> employeeSerde() {
        EmployeeSerializer serializer = new EmployeeSerializer();
        EmployeeDeserializer deserializer = new EmployeeDeserializer();
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
