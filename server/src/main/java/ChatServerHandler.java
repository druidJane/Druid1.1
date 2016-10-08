import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * Created by 1115 on 2016/9/20.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String>{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {

    }
    //public class ChatServerHandler extends SimpleChannelInboundHandler<UserProto.User>{
    /*AttributeKey<String> CLIENT_ATTR_KEY = AttributeKey.valueOf("ANTBOX_CLIENT");
    private int count = 0;
    private ChannelHandlerContext localCtx;
    public int channelId = 0;
    public static final ChannelGroup group = new DefaultChannelGroup(
            GlobalEventExecutor.INSTANCE);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, UserProto.User user) throws Exception {
        //System.out.println("Netty-Server:Receive Message,"+ user.getName());
        String respStr = "Response Message:"+new Date(System.currentTimeMillis()).toString();

        ByteBuf resp = Unpooled.copiedBuffer(respStr.getBytes());
        ctx.write(resp);
        for (Channel ch : group) {
            String s = ch.attr(CLIENT_ATTR_KEY).get();
            if("0".equals(s)){
                UserProto.User.Builder builder = UserProto.User.newBuilder();
                builder.setId(10000);
                builder.setName("Name_Boss");
                builder.setMsg("Msg_Boss");
                ch.writeAndFlush(builder.build());
            }
        }
        this.channelId=user.getId();


        *//*ChatServerHandler chatServerHandler = ChannelManager.getInstance().get("0");
        chatServerHandler.
        if(chatServerHandler!=null){
            //sendCommand(ctx);
        }*//*

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        *//*for (Channel ch : group) {
            ch.writeAndFlush(
                    "[" + channel.remoteAddress() + "] " + "is comming!");
        }*//*
        channel.attr(CLIENT_ATTR_KEY).setIfAbsent(ChannelManager.getInstance().size()+"");
        group.add(channel);
        ChannelManager.getInstance().add(this,ChannelManager.getInstance().size()+"");
        System.out.println(ChannelManager.getInstance().size());
    }

   *//* @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        for(Channel ch:group){
            ch.writeAndFlush( "[" + channel.remoteAddress() + "] " + "is leaving");
        }
        group.remove(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        for(Channel ch:group){
            ch.writeAndFlush( "[" + channel.remoteAddress() + "] " + "online");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        for(Channel ch:group){
            ch.writeAndFlush( "[" + channel.remoteAddress() + "] " + "offline");
        }
    }*//*

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        *//*System.out.println(
                "[" + ctx.channel().remoteAddress() + "]" + "exit the room");*//*
        ctx.close().sync();
    }
    private ChannelHandlerContext handlerContext() {
        if (this.localCtx == null) {
            //throw new ChannelNotActiveException("context is null");
            System.out.println("localCtx is null");
        }
        return this.localCtx;
    }*/
}
