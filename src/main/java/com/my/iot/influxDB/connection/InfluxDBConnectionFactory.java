package com.my.iot.common.influxdb.connection;

import java.util.ArrayList;
import java.util.List;

import com.my.iot.common.exception.InfluxDBConnectionFullException;

/**
 * InfluxDBConnectionFactory.java
 * @author 효민영♥
 *
 */
public class InfluxDBConnectionFactory {
	private static final int connectionMaxNum = 10;
	
//	@Autowired InfluxDBConfig config;
	
	private static final String connectUri = "http://127.0.0.1:3000";
	private static final String username = "telegraf";
	private static final String password = "telegraf";
	
	private static final List<InfluxDBConnection> connectionPool = new ArrayList<InfluxDBConnection>(); 
	
	public static final InfluxDBConnection getConnection() throws InfluxDBConnectionFullException {
		for(InfluxDBConnection con : connectionPool)
			if(con.isIdle()) return con.consume();
		
		// Pool에 새로운 DBConnection을 생성 후 리턴
		// 하지만, Pool의 최대치는 10개이므로, 현재 최대치라면 Exception을 발생시킨다.
		
		if(connectionMaxNum <= connectionPool.size()) throw new InfluxDBConnectionFullException(); 
		
		synchronized (connectionPool) {
			// connectionPool은, Thread-safe 시키기 위해 동기화 시킨다.
			InfluxDBConnection newConnection = new InfluxDBConnection(connectUri, username, password);
			connectionPool.add(newConnection);
			return newConnection.consume();
		}
	}
	
	//getConnection 이후에, 반드시 호출해줘야 하는 메서드
	public static final void endConnection(InfluxDBConnection con) {
		con.returned();
	}
	
}
