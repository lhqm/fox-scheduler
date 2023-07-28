package com.foxrpc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fox.entity.ClientData;
import com.fox.entity.TaskedMethod;
import com.fox.utils.banner.PrintFireFox;
import com.foxrpc.protocol.RpcMessage;
import com.foxrpc.utils.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    public void sendTasks(List<TaskedMethod> taskedMethods, String addr, Integer port, String name) {
        log.info("RPC模块准备发送任务到服务端,请等候...");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 设置超时时间
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcMessageEncoder()); // 注册编码器
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            // 建立与服务端的连接(引入配置的事件进行调用)
            ChannelFuture future = null;
            try {
                future = bootstrap.connect(addr, port).sync();
//                打印客户端标志
                PrintFireFox.printClient();
            } catch (Exception e) {
                log.error("发送失败,服务器地址或端口错误，请查证后再试！");
                throw new RuntimeException(e);
            }
            // 发送对象集合到服务端
            try {
                // 封装内容
                ClientData clientData = new ClientData(taskedMethods, name);
                // 封装到请求消息
                RpcMessage rpcMessage = new RpcMessage();
                // 手动放入传递参数的JSON序列
                rpcMessage.setServiceName("com.foxrpc.handler.ClientInit");
                rpcMessage.setMethodName("initClient");
                rpcMessage.setParameters(JSONObject.parseObject(JSON.toJSONString(clientData)));
                // 构造发送
                ChannelFuture sendFuture = future.channel().writeAndFlush(rpcMessage);

                // 添加监听器，等待服务端响应
                CountDownLatch latch = new CountDownLatch(1);
                sendFuture.addListener((ChannelFutureListener) futureListen -> {
                    if (futureListen.isSuccess()) {
                        log.info("发送任务成功，等待服务端响应...");
                        latch.countDown();
                    } else {
                        log.error("发送任务失败！");
                    }
                });
                // 等待计数器为0
                latch.await();
                // 等待连接关闭
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("数据发送遇到意外状况!");
                throw new RuntimeException(e);
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext context, Object msg) {
            // 处理服务端响应
            ByteBuf buf = (ByteBuf) msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
            context.close();
        }
    }
}
