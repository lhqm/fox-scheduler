package com.foxrpc.handler;

import com.alibaba.fastjson.JSONObject;
import com.fox.entity.ClientData;
import com.foxrpc.protocol.RpcMessage;
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

    }
}
