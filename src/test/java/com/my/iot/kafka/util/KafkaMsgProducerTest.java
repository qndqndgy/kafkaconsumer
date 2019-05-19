package com.my.iot.kafka.util;

import org.junit.Test;

public class KafkaMsgProducerTest {
	
	@Test
	void sendToKafkaQueueTest() throws Exception {
		// topic = test
		KafkaMsgProducer.sendToKafkaQueue("test", "Hello Kafka!".getBytes());
	}

}
