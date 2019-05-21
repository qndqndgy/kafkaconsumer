package com.my.iot.kafka.threads;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConcumeWorker implements Runnable {
	int total;
	@SuppressWarnings("resource")
	@Override
	public void run() {
		String topic = "test";
		String group = "test";
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", group);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList(topic));
		
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10);
			
			if(records.isEmpty())
				try {
					System.out.println("Currently nothing to do .. (Queue is empty)");
					Thread.sleep(3000);
					continue;
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());
				}
			int cnt = records.count();
			new MyInfluxDBWriter(records).run();
			total+=cnt;
			System.out.println(String.format("%d data added to influxDB (total = %d)", cnt,total));
			System.out.println("100 Message Finished. need to commit.");
			System.out.println(" Continue to work.");
			consumer.commitAsync();
		}
	}

}
