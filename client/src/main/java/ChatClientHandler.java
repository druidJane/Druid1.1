import com.cn.common.core.model.Request;
import com.cn.common.core.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 1115 on 2016/9/20.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<Response> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("kkk");
        short cmd = 1;
        Request request = Request.valueOf(cmd, cmd, "firstMessage".getBytes());
        ctx.writeAndFlush(request);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Response msg) throws Exception {
        System.out.println(new String(msg.getData()));
        System.out.println("Client:"+msg.getData().toString());
    }

}
