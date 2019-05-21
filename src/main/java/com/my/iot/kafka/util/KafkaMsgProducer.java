package com.my.iot.kafka.util;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.my.iot.common.util.PropertyReader;

/**
 * KafkaMsgProducer.java
 * 
 * producer Thread들의 핵심 역할을 하는 유틸 클래스
 * @author 효민영♥
 *
 */
public class KafkaMsgProducer {

	// Kafka에 Msg를 전송하는 메서드
	public static void sendToKafkaQueue(String topicName, byte[] msg) throws Exception {

		// create instance for properties to access producer configs
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("group.id", "test");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		Thread.currentThread().setContextClassLoader(null);
		Producer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);

		producer.send(new ProducerRecord<String, byte[]>(topicName, msg));
		producer.close();
	}
}
