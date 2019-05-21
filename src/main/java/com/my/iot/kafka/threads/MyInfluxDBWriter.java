package com.my.iot.kafka.threads;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;

import com.my.iot.influxDB.connection.InfluxDBConnection;
import com.my.iot.influxDB.connection.InfluxDBConnectionFactory;
import com.my.iot.influxDB.exception.InfluxDBConnectionFullException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyInfluxDBWriter implements Runnable{

	ConsumerRecords<String, String> records;
	
	@Override
	public void run() {
		InfluxDBConnection con = null;
		try {
			con = InfluxDBConnectionFactory.getConnection();
		
			InfluxDB db = con.getDb();
			
			db.setDatabase("test");
			if(!db.isBatchEnabled()) db.enableBatch(BatchOptions.DEFAULTS);
			
			for (ConsumerRecord<String, String> record : records) {
				db.write(new StringBuilder("test,atag=test1 ").append("val=").append(record.value()).toString());
			}
			db.flush();
		} catch (InfluxDBConnectionFullException e) {
			System.out.println(e.getMessage());
		} finally {
			InfluxDBConnectionFactory.endConnection(con);
		}		
	}

}
