package com.spark.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
impiiort io.netty.handler.codec.http.HttpServerCodec;
impiiort io.netty.handler.codec.http.HttpObjectAggregator;
public class ServerChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelPipeline pipeline=channel.pipeline();
		pipeline.addLast(new HttpServerCodec()); 
		pipeline.addLast(new HttpObjectAggregator(Short.MAX_VALUE));
		pipeline.addLast(new MessageHandler());
		
	}

}
