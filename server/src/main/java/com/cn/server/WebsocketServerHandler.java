package com.cn.server;

import com.cn.common.core.model.proto.RequestProto;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by 1115 on 2016/9/23.
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = Logger.getLogger(WebsocketServerHandler.class.getName());
    private WebSocketServerHandshaker handshaker;
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统的http接入
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }
        //WebSocket接入
        if (msg instanceof WebSocketFrame) {
            handldWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //如果Http解码失败，返回http异常
        if(!req.getDecoderResult().isSuccess()||!"websocket".equals((req.headers().get("Upgrade")))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8989/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if(handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
    }
    private void handldWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的指令
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain());
        }
        //判断是否是Ping消息
        if(frame instanceof  PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        }
        if(frame instanceof BinaryWebSocketFrame){
            byte[] bytes = new byte[frame.content().readableBytes()];
            RequestProto.Req.Builder req = RequestProto.Req.newBuilder();
            frame.content().readBytes(bytes);
            try {
                req.mergeFrom(bytes);
                RequestProto.Head.Builder reqSon = RequestProto.Head.newBuilder();
                reqSon.mergeFrom(req.getBody().getData());
                System.out.println(req.getHead().getHEADERFLAG());
                System.out.println("reqSon="+reqSon.getCmd().getNumber());
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            RequestProto.Head.Builder head = RequestProto.Head.newBuilder();
            head.setCmd(RequestProto.CmdType.PUSHCHAT);
            head.setModule(RequestProto.ModuleType.PLAYER);
            RequestProto.Body.Builder body = RequestProto.Body.newBuilder();
            body.setData(head.build().toByteString());
            RequestProto.Req.Builder res = RequestProto.Req.newBuilder();
            head.setHEADERFLAG(555);
            res.setHead(head.build());
            res.setBody(body.build());
            ctx.channel().write(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(res.build().toByteArray())));
        }
        /*if(frame instanceof BinaryWebSocketFrame){
            byte[] bytes = new byte[frame.content().readableBytes()];
            MessageJs.UserMessage.Builder builder = MessageJs.UserMessage.newBuilder();
            frame.content().readBytes(bytes);
            MessageJs.UserMessage.Builder msg;
            try {
                msg = builder.mergeFrom(bytes);
                System.out.println(msg.getMsg());
                msg.setMsg(msg.getMsg()+
                        ",欢迎使用Netty WebSocket服务，现在时刻："+new Date().toString());
                MessageJs.UserMessage user = msg.build();
                ByteBuf response = Unpooled.buffer();
                response.writeInt(100);
                response.writeBytes(user.toByteArray());
                ctx.channel().write(new BinaryWebSocketFrame(response));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        //本历程仅支持文本消息，不支持二进制消息
        /*if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported",frame.getClass().getName()));
        }*/
        //返回应答消息
        //String request = ((TextWebSocketFrame)frame).text();
       // System.out.println(String.format("%s receive %s",ctx.channel(),request));


    }
    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
        //返回应答给客户端
        if(res.getStatus().code()!=200){
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res,res.content().readableBytes());
        }
        //如果是非Keep-Alive,关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if(!isKeepAlive(req)||res.getStatus().code()!=200){
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
