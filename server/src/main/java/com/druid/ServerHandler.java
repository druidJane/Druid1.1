package com.druid;

import com.cn.common.core.model.Request;
import com.cn.common.core.model.Response;
import com.cn.common.core.session.SessionImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 1115 on 2016/9/23.
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Request request) throws Exception {
        handlerMessage(new SessionImpl(ctx.channel()), request);
    }

    private void handlerMessage(SessionImpl session, Request request) {
        Response response = new Response(request);

        System.out.println("module:" + request.getModule() + "   " + "cmd：" + request.getCmd());
        response.setStateCode(111);
        response.setData("server response".getBytes());
        session.write(response);

    }
}
