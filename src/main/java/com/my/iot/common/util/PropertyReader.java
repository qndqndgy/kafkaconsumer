package com.my.iot.common.util;

import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PropertyReader.java
 * 
 * config.properties 파일 읽기용.
 * 
 * @author 효민영♥
 *
 */

@NoArgsConstructor
@Getter @Setter
public class PropertyReader {

	private static volatile Properties instance = null;

	public static Properties propertyReader() {
		try {
			InputStream io = PropertyReader.class.getResourceAsStream("/config.properties");
			if (instance == null) {
				instance = new Properties();
				instance.load(io);
			}

		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}

		return instance;
	}
}
