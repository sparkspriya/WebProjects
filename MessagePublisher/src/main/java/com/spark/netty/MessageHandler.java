package com.spark.netty;

import java.util.List;
import java.util.Map;

import com.spark.constants.AppConstants;
import com.spark.kafka.KafkaMessagePublisher;
import com.spark.netty.Employee.EmployeeInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

public class MessageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	KafkaMessagePublisher publisher;
	public MessageHandler() {
		publisher=new KafkaMessagePublisher();
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
		String responseMsg="";
		System.out.println(httpRequest.getUri());
		QueryStringDecoder queryParamDecoder=new QueryStringDecoder(httpRequest.getUri());
		Map<String, List<String>>queryParams=queryParamDecoder.parameters();
		System.out.println(queryParams);
		//Query params check
		if(queryParams.isEmpty()) {
			responseMsg="No query params passed to publish the message.";
			System.out.println(responseMsg);
			sendResponse(ctx,responseMsg);
			return;
		}
		
		String empName="",dept="";
		
		//Constructing a Protobuff
		EmployeeInfo.Builder employee=EmployeeInfo.newBuilder();
		if(queryParams.containsKey(AppConstants.EMP_NAME)) {
			empName=queryParams.get(AppConstants.EMP_NAME).get(0);
			employee.setName(empName);
		}else if(queryParams.containsKey(AppConstants.EMP_DEPT)) {
			dept=queryParams.get(AppConstants.EMP_DEPT).get(0);
			employee.setDept(dept);
		}else {
			responseMsg="Please pass atleast any of the query params 'name' or 'dept'";
			sendResponse(ctx,responseMsg);
			System.out.println("Not processing as there is no expected query params");
			return;
		}
		System.out.println(empName+" "+dept);		
		System.out.println("Employee object constructed:"+employee);
		System.out.println("Going to publish the message");
		
		//Publishing the message to Kafka queue
		publisher.publishMessage(employee.build().toString());
		responseMsg="Published successfully.";
		sendResponse(ctx,responseMsg);
		
	}
	
	private void sendResponse(ChannelHandlerContext ctx,String responseMsg) {
		ByteBuf content = Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
