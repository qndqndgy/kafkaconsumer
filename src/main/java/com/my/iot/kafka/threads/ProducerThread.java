package com.my.iot.kafka.threads;

import com.my.iot.kafka.util.KafkaMsgProducer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@AllArgsConstructor
public class ProducerThread implements Runnable{
	
	private int no;
	
	@Override
	public void run() {
		System.out.println(String.format("Producer(%d)  starts working. ", no));
				
		int repeat = 1;
		while(repeat++ < 101) {
			try {
				KafkaMsgProducer.sendToKafkaQueue("test", String.valueOf(repeat).getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		System.out.println(String.format("Producer(%d)  finished working. ", no));
		
		// 이어서 Consumer 작업을 시작한다.
		if(this.no == 10) new ConcumeWorker().run();
		
	}

}
