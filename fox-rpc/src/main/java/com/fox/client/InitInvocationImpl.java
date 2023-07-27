package com.fox.client;

import com.fox.entity.ClientData;
import com.fox.entity.TaskedMethod;
import com.fox.utils.RpcMessageDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/25 10:08
 * 使用netty进行服务器通信,初始化客户端
 */
@Component
@Slf4j
public class InitInvocationImpl implements InitInvocation {
    @Override
    public void sendTasks(List<TaskedMethod> taskedMethods,String addr,Integer port,String name) {
        log.info("RPC模块准备发送任务到服务端,请等候...");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new RpcMessageDecoder()); // 注册编码器
                        }
                    });
            // 建立与服务端的连接(引入配置的事件进行调用)
            ChannelFuture future = null;
            try {
                future = bootstrap.connect(addr, port).sync();
            } catch (InterruptedException e) {
                log.error("发送失败,服务器地址或端口错误，请查证后再试！");
                throw new RuntimeException(e);
            }
            // 发送对象集合到服务端
            try {
                future.channel().writeAndFlush(new ClientData(taskedMethods,name)).sync();
            } catch (InterruptedException e) {
                log.error("数据发送遇到意外状况!");
                throw new RuntimeException(e);
            }
            // 关闭连接
            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("关闭group失败!");
                throw new RuntimeException(e);
            }
        }finally {
            group.shutdownGracefully();
        }
    }
}
