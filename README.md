# MY Kafka Producer & Consumer (#Part 2)

----

본 프로젝트는 MY Api Server (#Part 1) 와 연결되는 개념으로, 이전 Springboot 기반 Restapi 서버에서 Basic한 서버의 구성과 웹호스팅,
InfluxDB + Telegraf로 저장되는 Time-Series 데이터 연동 시각화 Vue대시보드를 제공했다면, 본 프로젝트는 iot업계에서 쏟아져 나오는 방대한 양의 Data를 
어떻게 저장할 것이냐는 생각에서 출발했습니다. 막연히 일반 시스템처럼 CRUD를 처리한다면 많게는 수백, 수천개의 Sensor들에서 중앙으로 모이는 방대한 양의 Data를 단건별로 Database에 Insert했다간 HW자원을 아무리 늘려도 서버를 호스팅할 수 없을 것입니다. 

때문에 대용량의 Data를 처리할 수 있는 방안을 고민했고, 이쪽으로 많은 인기를 얻고 있는 Kafka를 활용해서 대량의 Data를 Time Series Database인 InfluxDB에 Insert 하는 기능을 제공합니다.

최초 목표는, Springboot Application에서 1차적으로 Data의 Insert요청을 받은 뒤, Kafka로 비동기 전달하여 Message Queue를 구성하며, Netty 기반 고성능 웹서버로 구성된 Consumer Server에서 
효율적으로 대량의 요청을 처리하는 것이 목적이었으나, 절충하여 Netty App 안에서 Producer와 Consumer 역할을 동시에 하는 것으로 대체하였습니다.

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
