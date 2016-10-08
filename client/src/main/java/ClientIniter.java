import com.cn.common.core.coder.RequestEncoder;
import com.cn.common.core.coder.ResponseDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by 1115 on 2016/9/21.
 */
public class ClientIniter extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ResponseDecoder());
        pipeline.addLast(new RequestEncoder());
        pipeline.addLast("chat",new ChatClientHandler());
    }
}
