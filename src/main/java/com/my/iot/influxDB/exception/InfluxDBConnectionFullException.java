package com.my.iot.influxDB.exception;

import org.json.simple.JSONObject;

/**
 * InfluxDBConnectionFullException.java
 * 
 * InfluxDB Connection Pool이 가득 차면 raise되는 exception 클래스
 * @author 효민영♥
 *
 */
public class InfluxDBConnectionFullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -39387173860354773L;

	public InfluxDBConnectionFullException() {
		super();
	}

	public InfluxDBConnectionFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace , JSONObject jsonObject) {
		super(message + " Connection Pool is Full.. Need to take a time...", cause, enableSuppression, writableStackTrace);
	}

	public InfluxDBConnectionFullException(String message, Throwable cause , JSONObject jsonObject) {
		super(message + " Connection Pool is Full.. Need to take a time...", cause);
	}

	public InfluxDBConnectionFullException(String message , JSONObject jsonObject) {
		super(message + " Connection Pool is Full.. Need to take a time...");
	}

	public InfluxDBConnectionFullException(Throwable cause , JSONObject jsonObject) {
		super(cause);
	}

}
