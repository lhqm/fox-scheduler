package com.foxrpc.handler;

import com.foxrpc.protocol.RpcMessage;
import com.foxrpc.utils.RpcMessageDecoder;
import com.foxrpc.utils.RpcMessageEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 10:00
 * server端处理器
 */

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object data) {
        // 添加编码器
        context.pipeline().addLast(new RpcMessageEncoder());
        context.pipeline().addLast(new RpcMessageDecoder());
        // 获取到全限定类名和方法名
        RpcMessage message = (RpcMessage) data;
        Class<?> initClass = null;
        try {
            initClass = Class.forName(message.getServiceName());
        } catch (ClassNotFoundException e) {
            log.error("不存在的类");
            throw new RuntimeException(e);
        }
        Method method = null;
        try {
            method = initClass.getMethod(message.getMethodName(), ChannelHandlerContext.class, RpcMessage.class);
        } catch (NoSuchMethodException e) {
            log.error("不存在的方法！请检查方法名或参数列表是否有误！");
            throw new RuntimeException(e);
        }
        try {
            method.invoke(initClass.getDeclaredConstructor().newInstance(),context,data);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            log.info("调用失败！请检查该错误！");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
