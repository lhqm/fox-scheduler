package com.fox.aspect;

import com.fox.handler.ServerHandler;
import com.fox.utils.RpcMessageDecoder;
import com.fox.utils.banner.PrintFireFox;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 16:39
 * 服务器切面。用于服务器初始化时监听指定端口
 */
@Component
@Slf4j
public class FoxServerAspect {
    @Value("${fox.server.port:-1}")
    private int port;

    /**
     * 启动netty服务器
     * @param port 监听端口
     */
    public void startNetty(int port) throws InterruptedException {
//        配置文件没配置或者非法，读注解
        if (this.port<0){
//            注解上有且合法，直接读注解
            if (port>0){
                this.port=port;
            }else {
//                全都失效，配置到8000端口
                this.port=8000;
            }
        }
//        开始启动netty
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcMessageDecoder()); // 解码器
                            ch.pipeline().addLast(new ServerHandler()); // 服务端处理器
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收连接
            ChannelFuture future = bootstrap.bind(this.port).sync();
            System.out.println("启动成功，正在等待客户端请求");
            PrintFireFox.printFireFox();
            // 等待服务器套接字关闭
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
