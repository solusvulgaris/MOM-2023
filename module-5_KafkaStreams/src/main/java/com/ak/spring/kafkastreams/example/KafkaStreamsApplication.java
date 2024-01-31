package com.ak.spring.kafkastreams.example;

import com.ak.spring.kafkastreams.example.data.Employee;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.BreakIterator;
import java.util.Locale;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class KafkaStreamsApplication implements CommandLineRunner {
	@Value("${kafka.task1-1.topic}")
	private String topic1_1;

	@Value("${kafka.task2.topic}")
	private String topic2;

	@Value("${kafka.task3-1.topic}")
	private String topic3_1;

	@Value("${kafka.task3-2.topic}")
	private String topic3_2;

	@Value("${kafka.task4.topic}")
	private String topic4;

	@Autowired
	@Qualifier("kafkaTemplate")
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	@Qualifier("kafkaEmployeeDtoTemplate")
	KafkaTemplate<String, Employee> kafkaEmployeeDtoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		//Task 1
		//executeTask1();

		//Task2
		//executeTask2();

		//Task3
		//executeTask3();

		//Task4
		executeTask4();
	}

	private void executeTask4() {
		Employee employee = new Employee("My Employee", "EPAM", "developer", 5);

		for (int i = 1; i <= 5; i++) {
			employee.setExperience(i);
			kafkaEmployeeDtoTemplate.send(topic4, employee.getName(), employee);
		}
	}

	private void executeTask3() {
		File data = new File(KafkaStreamsApplication.class.getClassLoader().getResource("task3_mock_data.txt").getFile());
		int index = 0;
		try (Scanner scanner = new Scanner(data)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				kafkaTemplate.send(topic3_1, index, line);
				kafkaTemplate.send(topic3_2, index, line);
				index++;

				Thread.sleep(100);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + e);
		} catch (InterruptedException e) {
			throw new RuntimeException("Thread interrupted: " + e);
		}
	}

	@SneakyThrows
	private void executeTask2() {
		sendSentencesToTopic(topic2);
	}

	private void executeTask1() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			kafkaTemplate.send(topic1_1, i, "Value-" + i);
			Thread.sleep(100);
		}
	}

	private void sendSentencesToTopic(String topic) throws IOException {
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		File book = new File(KafkaStreamsApplication.class.getClassLoader().getResource("The-fixer-George-O-Geor.txt").getFile());

		String source = new String(Files.readAllBytes(book.toPath()));

		iterator.setText(source);
		int start = iterator.first();
		int index = 0;
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
			final String sentence = source.substring(start, end);
			kafkaTemplate.send(topic, index, sentence);
			index++;
		}
	}
}
