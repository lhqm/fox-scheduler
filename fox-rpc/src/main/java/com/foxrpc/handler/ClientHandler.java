package com.foxrpc.handler;

import com.foxrpc.protocol.RpcMessage;
import com.foxrpc.utils.RpcMessageDecoder;
import com.foxrpc.utils.RpcMessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext context, Object msg) {
            // 处理服务端响应
            // 获取到全限定类名和方法名
            RpcMessage message = (RpcMessage) msg;
            if (((RpcMessage) msg).getServiceName()==null){return;}
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
                method.invoke(initClass.getDeclaredConstructor().newInstance(),context,msg);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                log.info("调用失败！请检查该错误！");
                throw new RuntimeException(e);
            }
        }
    }