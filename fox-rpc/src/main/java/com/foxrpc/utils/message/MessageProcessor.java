package com.foxrpc.utils.message;

import com.alibaba.fastjson.JSONObject;
import com.foxrpc.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/28 17:21
 * 消息处理器类。用于快速处理rpc消息
 */
@Slf4j
public class MessageProcessor {
    /**
     * 异步发送。可以知道消息是否被成功发送来执行不同逻辑
     * @param context 内容
     * @param msg 消息
     * @return 异步执行结果
     */
    public static CompletableFuture<Boolean> sendFuture(ChannelHandlerContext context, RpcMessage msg){
        //        TODO:包含一个暂时无法解决的问题。序列化编码器似乎对rpcMessage不能以byteBuf正常写入缓冲区
        ByteBuf buffer = Unpooled.buffer(); // 创建一个ByteBuf
//      将响应对象序列化为ByteBuf，具体的实现取决于您使用的序列化框架
//      使用JSON序列化
        byte[] responseBytes = JSONObject.toJSONBytes(msg);
        buffer.writeBytes(responseBytes);
//        定义异步返回
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();
//      发送响应
        ChannelFuture sendFuture = context.writeAndFlush(buffer);
        boolean success;
//      添加监听器，以处理发送结果
        sendFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("发送消息成功");
                resultFuture.complete(true); // 设置执行结果为成功
            } else {
                log.error("发送消息失败");
                resultFuture.complete(false); // 设置执行结果为失败
                future.cause().printStackTrace();

            }
        });
        return resultFuture;
    }
    public static void sendSync(ChannelHandlerContext context, RpcMessage msg){
        //        TODO:包含一个暂时无法解决的问题。序列化编码器似乎对rpcMessage不能以byteBuf正常写入缓冲区
        ByteBuf buffer = Unpooled.buffer(); // 创建一个ByteBuf
//      将响应对象序列化为ByteBuf，具体的实现取决于您使用的序列化框架
//      使用JSON序列化
        byte[] responseBytes = JSONObject.toJSONBytes(msg);
        buffer.writeBytes(responseBytes);
//      发送响应
        context.writeAndFlush(buffer);
    }
}
