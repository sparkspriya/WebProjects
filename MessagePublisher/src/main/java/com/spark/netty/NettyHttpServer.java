package com.spark.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyHttpServer {
public static void main(String[] args) {
	NioEventLoopGroup evtGroup=new NioEventLoopGroup();
	ServerBootstrap serverBootstrap=new ServerBootstrap();
	serverBootstrap.group(evtGroup)
		.handler(new LoggingHandler(LogLevel.INFO))
		.channel(NioServerSocketChannel.class)
		.childHandler(new ServerChannelInitializer());
	
	Channel channel;
	try {
		channel = serverBootstrap.bind(8090).sync().channel();
		channel.closeFuture().sync();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		evtGroup.shutdownGracefully();
	}
	
}
}
