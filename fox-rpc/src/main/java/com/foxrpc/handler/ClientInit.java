package com.foxrpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.fox.entity.ClientData;
import com.foxrpc.protocol.RpcMessage;
import com.foxrpc.utils.message.MessageProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/28 16:52
 * 客户端注册方案
 */
@Slf4j
public class ClientInit {
    /**
     * 服务端逻辑
     * @param context 上下文
     * @param message 消息
     */
    public void initClient(ChannelHandlerContext context, RpcMessage message){
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
        MessageProcessor.sendFuture(context,response).thenAccept(result->{
            if (result){
                log.info("构造回执成功");
            }else {
                log.error("发送失败！！");
            }
        });
    }
    /**
     * 客户端逻辑
     * @param context 上下文
     * @param message 消息
     */

    public void clientReply(ChannelHandlerContext context, RpcMessage message){
        // 获取到全限定类名和方法名
        System.out.println(message.toJsonString());
    }
}
