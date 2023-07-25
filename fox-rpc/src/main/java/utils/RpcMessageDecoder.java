package utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import protocol.RpcMessage;

import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/25 09:57:25
 * netty解码器
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            // 如果可读字节不足4个，直接返回
            return;
        }

        // 标记当前读索引位置
        in.markReaderIndex();
        // 读取字节流长度
        int length = in.readInt();

        if (in.readableBytes() < length) {
            // 如果可读字节不足消息长度，重置读索引，等待更多数据到达
            in.resetReaderIndex();
            return;
        }

        // 读取字节数据
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        // 将字节数据转换为JSON字符串
        String json = new String(bytes, CharsetUtil.UTF_8);
        // 将JSON字符串转换为RpcMessage对象
        RpcMessage rpcMessage = RpcMessage.fromJsonString(json);
        out.add(rpcMessage);
    }
}