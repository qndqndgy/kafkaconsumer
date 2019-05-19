package com.my.iot.kafka.util;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MyConsumer.java
 * 컨슈머 테스트용으로 사용했던 클래스.
 * 현재는 사용할 일 없음.
 * @author 효민영♥
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class MyConsumer implements Runnable{
	private int no;
	private String topic;
	private Properties props;

	@Override
	public void run() {
		// Kafka에서 제공하는 consumer 클라이언트.
		// Kafka에서 제공하는 클라이언트는 객체를 생성할 때, property 객체를 넘겨주어야 함.
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// test용 Topic 구독
		consumer.subscribe(Arrays.asList(topic));
		System.out.println("Thread ("+(no)+") started to subscribe to topic " + topic);
		int no = 0;
		
		// 구독해서 자신에게 할당된 record를 단순히 출력하고 종료한다.
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("Thread (%d) ::: offset = %d, key = %s, value = %s\n", no, record.offset(), record.key(), record.value());
		}
	}
	
	
}
