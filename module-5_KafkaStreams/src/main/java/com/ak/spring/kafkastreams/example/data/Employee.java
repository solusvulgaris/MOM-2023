package com.ak.spring.kafkastreams.example.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String company;
    @Getter
    @Setter
    private String position;
    @Getter
    @Setter
    private int experience;
}
