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

		props.put("bootstrap.servers", PropertyReader.propertyReader().getProperty("bootstrap.servers"));
		props.put("acks", PropertyReader.propertyReader().getProperty("acks"));
		props.put("group.id", PropertyReader.propertyReader().getProperty("group.id"));
		props.put("retries", PropertyReader.propertyReader().getProperty("retries"));
		props.put("batch.size", PropertyReader.propertyReader().getProperty("batch.size"));
		props.put("linger.ms", PropertyReader.propertyReader().getProperty("linger.ms"));
		props.put("buffer.memory", PropertyReader.propertyReader().getProperty("buffer.memory"));
		props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		Thread.currentThread().setContextClassLoader(null);
		Producer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);

		producer.send(new ProducerRecord<String, byte[]>(topicName, msg));
		producer.close();
	}
}
