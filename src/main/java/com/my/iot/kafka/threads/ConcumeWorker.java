package com.my.iot.kafka.threads;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.my.iot.influxDB.connection.InfluxDBConnection;
import com.my.iot.influxDB.connection.InfluxDBConnectionFactory;
import com.my.iot.influxDB.exception.InfluxDBConnectionFullException;

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
			ConsumerRecords<String, String> records = consumer.poll(100);
			
			if(records.isEmpty())
				try {
					System.out.println("Currently nothing to do .. (Queue is empty)");
					Thread.sleep(3000);
					continue;
				} catch (InterruptedException e1) {
					System.out.println(e1.getMessage());
				}
			
			InfluxDBConnection con = null;
			try {
				con = InfluxDBConnectionFactory.getConnection();
			} catch (InfluxDBConnectionFullException e) {
				System.out.println(e.getMessage());
			}
			InfluxDB db = con.getDb();
			
			db.setDatabase("test");
			if(!db.isBatchEnabled()) db.enableBatch(BatchOptions.DEFAULTS);
			StringBuilder writeValues = new StringBuilder();
			writeValues.append("test,atag=test1 ");
			boolean firstVal = true;
			int cnt = records.count();
			for (ConsumerRecord<String, String> record : records) {
				if(firstVal) {
					writeValues.append("val=").append(record.value());
					firstVal = false;
				}else {
					writeValues.append(",val=").append(record.value());
				}
			}
			total+=cnt;
			System.out.println(String.format("%d data added to influxDB (total = %d)", cnt,total));
			db.write(writeValues.toString());
			db.flush();
			System.out.println("100 Message Finished. need to commit.");
			System.out.println(" Continue to work.");
			consumer.commitAsync();
			
			InfluxDBConnectionFactory.endConnection(con);
		}
	}

}
