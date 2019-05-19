package com.my.iot.netty.server.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.my.iot.kafka.threads.ConcumeWorker;
import com.my.iot.kafka.threads.ProduceWorker;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * ServerRequestHandler.java
 * 
 * 실제 Request가 올 때, 응답을 정의할 수 있는 핸들러.
 * QueryString으로 cmd=run 을 주면, 
 * ProducerAndConsumerSimulator의 main함수와 똑같은 동작을 테스트해볼 수 있다.
 * 
 * @author 효민영♥
 *
 */
public class ServerRequestHandler extends SimpleChannelInboundHandler<Object> {
	private static final String KAFKA_TOPIC = "test";

	private static final FastThreadLocal<DateFormat> FORMAT = new FastThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		}
	};

	private static final ByteBuf CONTENT_BUFFER = Unpooled
			.unreleasableBuffer(Unpooled.directBuffer().writeBytes("Hello, World!".getBytes(CharsetUtil.UTF_8)));
	private static final CharSequence contentLength = HttpHeaders
			.newEntity(String.valueOf(CONTENT_BUFFER.readableBytes()));

	private static final CharSequence TYPE_PLAIN = HttpHeaders.newEntity("text/plain; charset=UTF-8");
	private static final CharSequence TYPE_JSON = HttpHeaders.newEntity("application/json; charset=UTF-8");
	private static final CharSequence SERVER_NAME = HttpHeaders.newEntity("Netty");
	private static final CharSequence CONTENT_TYPE_ENTITY = HttpHeaders.newEntity(HttpHeaders.Names.CONTENT_TYPE);
	private static final CharSequence DATE_ENTITY = HttpHeaders.newEntity(HttpHeaders.Names.DATE);
	private static final CharSequence CONTENT_LENGTH_ENTITY = HttpHeaders.newEntity(HttpHeaders.Names.CONTENT_LENGTH);
	private static final CharSequence SERVER_ENTITY = HttpHeaders.newEntity(HttpHeaders.Names.SERVER);
	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper();
		MAPPER.registerModule(new AfterburnerModule());
	}

	private volatile CharSequence date = HttpHeaders.newEntity(FORMAT.get().format(new Date()));

	ServerRequestHandler(ScheduledExecutorService service) {
		service.scheduleWithFixedDelay(new Runnable() {
			private final DateFormat format = FORMAT.get();

			public void run() {
				date = HttpHeaders.newEntity(format.format(new Date()));
			}
		}, 1000, 1000, TimeUnit.MILLISECONDS);

	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			System.out.println(msg);
			// Query Pram Decoder
			QueryStringDecoder decoder = new QueryStringDecoder(((HttpRequest) msg).getUri());
			Map<String, List<String>> requestMap = decoder.parameters();

			// send query parameter to kafka
			System.out.println("=============Command Option===========");
			System.out.println("run: Bulk Produce Job (10000 Times) + Consumer Work (Will Insert Data To InfluxDB)");
			System.out.println("======================================");
			
			String cmd = requestMap.get("cmd").get(0);
			
			Map<String,String> returnMap = new HashMap<>();
			if(cmd == null) {
				returnMap.put("result", "wrong command.");
				returnMap.put("statusCode", "400");
				byte[] jsonRes = MAPPER.writeValueAsBytes(returnMap);
				writeResponse(ctx, request, Unpooled.wrappedBuffer(jsonRes), TYPE_JSON, String.valueOf(jsonRes.length));
			}
			
			// http://localhost:8080?cmd=run
			if("run".equals(cmd)) {
				new ProduceWorker().run();
			}
			
			
			returnMap.put("statusCode", "200");
			returnMap.put("result", "Success");
			byte[] jsonRes = MAPPER.writeValueAsBytes(returnMap);
			writeResponse(ctx, request, Unpooled.wrappedBuffer(jsonRes), TYPE_JSON, String.valueOf(jsonRes.length));
			return;
		}
	}

	
	private void writeResponse(ChannelHandlerContext ctx, HttpRequest request, ByteBuf buf, CharSequence contentType,
			CharSequence contentLength) {
		boolean keepAlive = HttpHeaders.isKeepAlive(request);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf,
				false);
		HttpHeaders headers = response.headers();
		headers.set(CONTENT_TYPE_ENTITY, contentType);
		headers.set(SERVER_ENTITY, SERVER_NAME);
		headers.set(DATE_ENTITY, date);
		headers.set(CONTENT_LENGTH_ENTITY, contentLength);

		if (!keepAlive) {
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		} else {
			ctx.write(response, ctx.voidPromise());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}
