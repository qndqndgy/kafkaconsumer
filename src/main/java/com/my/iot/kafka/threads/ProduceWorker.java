package com.my.iot.kafka.threads;

import java.util.stream.IntStream;

public class ProduceWorker implements Runnable {

	@Override
	public void run() {
		//10개의 Thread를 생성하여, 1000번씩 Produce. 총 10000번의 Produce.
		
		System.out.println("Create 10 Threads. And every threads will produce 1000 Messages");
		
		IntStream.range(1, 11) // 1~10
				.mapToObj((idx)->{return new ProducerThread(idx);})
				.forEach(ProducerThread::run);
	}

}
