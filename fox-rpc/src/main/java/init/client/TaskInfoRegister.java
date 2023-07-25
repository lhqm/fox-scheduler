package init.client;

import com.fox.entity.TaskedMethod;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import utils.RpcMessageDecoder;

import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/25 10:08
 * 使用netty进行服务器通信,初始化客户端
 */
public class TaskInfoRegister {
    public static void sendTasks(List<TaskedMethod> taskedMethods,String addr,Integer port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcMessageDecoder()); // 注册编码器
                        }
                    });
            // 建立与服务端的连接(引入配置的事件进行调用)
            ChannelFuture future = bootstrap.connect(addr, port).sync();
            // 发送对象集合到服务端
            future.channel().writeAndFlush(taskedMethods).sync();
            // 关闭连接
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
