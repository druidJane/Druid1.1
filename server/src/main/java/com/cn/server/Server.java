package com.cn.server;

import com.cn.common.core.coder.RequestDecoder;
import com.cn.common.core.coder.RequestEncoder;
import com.cn.common.core.coder.ResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by 1115 on 2016/9/23.
 */
@Component
public class Server {
    @Autowired
    protected static RedisTemplate<String, String> redisTemplate;
    /**
     * 启动
     */
    public void start() {
        System.out.println(redisTemplate.boundValueOps("test").get());
        // 服务类
        ServerBootstrap b = new ServerBootstrap();
        // 创建boss和worker
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 设置循环线程组事例
            b.group(bossGroup, workerGroup)
            // 设置channel工厂
            .channel(NioServerSocketChannel.class)
            // 设置管道
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //ch.pipeline().addLast(new RequestDecoder());
                    //ch.pipeline().addLast(new ResponseEncoder());
                    ch.pipeline().addLast("http-codec",new HttpServerCodec());
                    ch.pipeline().addLast("agreegator",new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                    ch.pipeline().addLast(new WebsocketServerHandler());
                    //ch.pipeline().addLast(new ServerHandler());
                }
            });
            // 链接缓冲池队列大小
            b.option(ChannelOption.SO_BACKLOG, 2048);// 链接缓冲池队列大小
            // 绑定端口
            b.bind(8989).sync();
            System.out.println("start!!");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
