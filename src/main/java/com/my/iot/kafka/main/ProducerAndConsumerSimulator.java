package com.my.iot.kafka.main;
import com.my.iot.kafka.threads.ProduceWorker;


/**
 * Producer+Consumer 시뮬레이션을 하기 위해, 시작시켜야 하는 메인 클래스
 * Producer 먼저 끝난 뒤에, 체인으로 Consumer 호출 됨. 
 * 
 * Kafka나 Zookeeper가 서비스되어 있지 않거나 하는 등의 환경셋팅 예외처리는 하지 않음.
 * 
 * @author 효민영♥
 *
 */

public class ProducerAndConsumerSimulator {
	public static void main(String[] args) {
		
		
		new ProduceWorker().run();
//		new ConcumeWorker().run();
		
	}
}
