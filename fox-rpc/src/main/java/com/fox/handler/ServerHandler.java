package com.fox.handler;

import com.alibaba.fastjson.JSONObject;
import com.fox.entity.ClientData;
import com.fox.protocol.RpcMessage;
import com.fox.utils.RpcMessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 10:00
 */

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object data) {
        // 添加编码器
        context.pipeline().addLast(new RpcMessageEncoder());

        // 获取参数
        RpcMessage message = (RpcMessage) data;

        // 还原
        ClientData clientData = ClientData.fromJsonString(message.getParameters().toJSONString());
        log.info("收到客户端名称为{}的消息", clientData.getClientName());
        clientData.getTaskedMethods().forEach(item -> {
            log.info("类限定名称:{},方法名：{}", item.getClazz(), item.getMethod());
            log.info(item.getAnnotationValue().toJSONString());
        });

        // 响应客户端
        RpcMessage response = new RpcMessage();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "ok");
        response.setResult(jsonObject);
//        TODO:包含一个暂时无法解决的问题。序列化编码器似乎对rpcMessage不能以byteBuf正常写入缓冲区
        ByteBuf buffer = Unpooled.buffer(); // 创建一个ByteBuf
//      将响应对象序列化为ByteBuf，具体的实现取决于您使用的序列化框架
//      使用JSON序列化
        byte[] responseBytes = JSONObject.toJSONBytes(response);
        buffer.writeBytes(responseBytes);

//      发送响应
        ChannelFuture sendFuture = context.writeAndFlush(buffer);

//      添加监听器，以处理发送结果
        sendFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("返回响应消息成功");
            } else {
                log.error("发送响应消息失败");
                future.cause().printStackTrace();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
