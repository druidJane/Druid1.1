import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by 1115 on 2016/9/20.
 */
public class ChatServer {
    private final static int port = 8989;

    public static void main(String[] args) {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG,1024)
                        .childHandler(new ServerIniterHandler());
                //绑定端口、同步等待
                ChannelFuture future = serverBootstrap.bind(port).sync();
                //等待服务监听端口关闭
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //退出，释放线程等相关资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }

        }
}
