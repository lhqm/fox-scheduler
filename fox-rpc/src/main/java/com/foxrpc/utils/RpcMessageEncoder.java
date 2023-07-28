package com.foxrpc.utils;

import com.foxrpc.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/25 09:41:46
 * netty编码器
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) throws Exception {
        // 将RpcMessage对象转换为JSON字符串
        String json = rpcMessage.toJsonString();
        // 将JSON字符串转换为字节流
        byte[] bytes = json.getBytes(CharsetUtil.UTF_8);
        // 写入字节流长度和字节数据到ByteBuf
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}