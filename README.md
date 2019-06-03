# MY Kafka Producer & Consumer Sample

----

## 개발 도구
```
OS : Windows 7
개발 언어 : JDK 1.8
IDE : STS(Eclipse)
```


## 빌드 도구
```
Maven : 3.3.9
```


## 메인 Framework
```
netty: 4.0.21.Final
Lombok : 1.18.8
Kafka-client: 2.0.1
influxdb-java: 2.15
Junit: 4.12

```


## Agent 도구
```
InfluxDB
Kafka
Zookeeper
```


## 기타 사항
```
Java 기능 단위 Simulation (ProducerAndConsumerSimulator.java 내 main함수 실행)
Kafka 실행에 필요한 바이너리, bat파일등을 통으로 묶어서 ext/ 이하에 kafka_2.11-2.2.0.tar 로 추가.
```

----


#### 실행 환경 Setting

## InfluxDB Agent 시작 (Windows만 작성함)
```
(Source_Root_Directory\ext\start_influxdb.bat
```
----

## Zookeeper Agent 시작 
```
(Source_Root_Directory\ext\kafka_2.11-2.2.0\bin\windows\1_my_zookeeper_start.bat
```
----

## Kafka Agent 시작 
```
(Source_Root_Directory\ext\kafka_2.11-2.2.0\bin\windows\1_my_kafka_start.bat
```
----

## Netty Http 서버 구동 (서버로 테스트 시)
```
Run Java Program : NettyWebServerMain.java
서버 실행 후, 다음 커맨드 입력 (Kafka Producer -> Consumer 순서로 작업 수행)
 - http://localhost:8080?cmd=run
```
----

----------
