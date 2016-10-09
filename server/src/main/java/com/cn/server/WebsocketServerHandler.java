package com.cn.server;

import com.cn.common.core.coder.Constant;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.proto.RequestProto;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.SessionImpl;
import com.cn.server.scanner.Invoker;
import com.cn.server.scanner.InvokerHoler;
import com.google.protobuf.ByteString;
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
            ResponseProto.Rsp.Builder res = ResponseProto.Rsp.newBuilder();
            try {
                //解析数据
                byte[] bytes = new byte[frame.content().readableBytes()];
                RequestProto.Req.Builder req = RequestProto.Req.newBuilder();
                frame.content().readBytes(bytes);
                req.mergeFrom(bytes);
                //数据校验 TODO 检验包头
                //根据模块，命令获取相关方法
                Integer cmd = req.getCmd().getNumber();
                Integer module = req.getModule().getNumber();
                Invoker invoker = InvokerHoler.getInvoker(module,cmd);
                if(invoker!=null){
                    Result<?> result = null;
                    SessionImpl session = new SessionImpl(ctx.channel());
                    //执行相关业务逻辑
                    result = (Result<?>)invoker.invoke(session,req.getData().toByteArray());
                    //封装响应数据
                    //构建包头
                    res.setHEADERFLAG(Constant.HEADER_FLAG);
                    res.setCmd(ResponseProto.CmdType.valueOf(cmd));
                    res.setModule(ResponseProto.ModuleType.valueOf(module));
                    if (ResultCode.SUCCESS==result.getResultCode()) {
                        //构建包数据
                        byte[] content = (byte[])result.getContent();
                        res.setData(ByteString.copyFrom(content));
                    }
                    res.setResultCode(result.getResultCode());
                }else{
                    res.setResultCode(ResultCode.NO_INVOKER);
                }
            } catch (InvalidProtocolBufferException e) {
                res.setResultCode(ResultCode.AGRUMENT_ERROR);
            } catch (Exception e){
                e.printStackTrace();
                res.setResultCode(ResultCode.UNKOWN_EXCEPTION);
            } finally{
                //输出数据到客户端
                ctx.channel().write(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(res.build().toByteArray())));
            }
        }
        //本历程仅支持文本消息，不支持二进制消息
        /*if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported",frame.getClass().getName()));
        }*/
        //返回应答消息
        //String request = ((TextWebSocketFrame)frame).text();
       // System.out.println(String.format("%s receive %s",ctx.channel(),request));


    }
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
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
