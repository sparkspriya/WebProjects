package com.spark.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelPipeline pipeline=channel.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new MessageHandler());
		
	}

}
