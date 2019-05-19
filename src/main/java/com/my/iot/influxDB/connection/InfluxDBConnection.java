package com.my.iot.influxDB.connection;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**InfluxDBConnection.java
 * InfluxDB와의 커넥션을 관리하는 클래스
 * (api server에서 그대로 가져왔다.)
 * 
 * @author 효민영♥
 *
 */
public class InfluxDBConnection {
	
	private boolean idle;
	private InfluxDB db;
	
	InfluxDBConnection(final String connectUri, String username, String password){
		this.db = InfluxDBFactory.connect(connectUri, username, password);
		this.idle = true;
	}
	
	boolean isIdle() {
		return this.idle;
	}
	
	InfluxDBConnection consume() {
		this.idle = false;
		return this;
	}
	
	void returned() {
		this.idle = true;
	}
	
	public InfluxDB getDb() {
		return this.db;
	}
	
	void closeConnection() {
		this.db.close();
	}
}
