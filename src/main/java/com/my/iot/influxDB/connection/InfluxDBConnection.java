package com.my.iot.common.influxdb.connection;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * InfluxDBConnection.java
 * @author 효민영♥
 *
 */
public class InfluxDBConnection {
	
	private boolean idle;
	private InfluxDB db;
	
	// InfluxDB와의 Connection을 직접적으로 갖는 InfluxDB 객체를 캡슐화 한 클래스.
	// InfluxDB 객체는 외부에서 접근 하지 못하고, InfluxDBConnection 객체를 통해서만 접근 해야 함.
	InfluxDBConnection(final String connectUri, String username, String password){
		this.db = InfluxDBFactory.connect(connectUri, username, password);
		this.idle = true;
	}
	
	// 이 idle 상태를 보고, InfluxDBConnectionPool에서 빌려주거나, 대상에서 제외 시킴.
	boolean isIdle() {
		return this.idle;
	}
	
	// 이쪽은 모두 접근제한이 패키지이기 때문에, InfluxDBConnectionFactory에서만 접근이 가능.
	InfluxDBConnection consume() {
		this.idle = false;
		return this;
	}
	
	// InfluxDBConnectionFactory가 객체를 빌려줄 때 consume(), 돌려받았을 때 returned() 호출함.
	void returned() {
		this.idle = true;
	}
	
	public InfluxDB getDb() {
		return this.db;
	}
	
	// Connection을 닫을 경우. (객체 버리기 전에 호출 필요.)
	void closeConnection() {
		this.db.close();
	}
}
