package com.cn.common.core.coder;

import com.cn.common.core.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.logging.Logger;

/**
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 * |  包头	|  模块号      |  命令号    |  结果码    |  长度       |   数据     |
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 * Created by 1115 on 2016/9/23.
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {
    private static final Logger log = Logger.getLogger(ResponseEncoder.class.getName());
    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, ByteBuf out) throws Exception {
        log.info("返回请求:" + "module:" +msg.getModule() +" cmd:" + msg.getCmd() + " resultCode:" + msg.getStateCode());

        //包头
        out.writeInt(Constant.HEADER_FLAG);
        //module和cmd
        out.writeShort(msg.getModule());
        out.writeShort(msg.getCmd());
        //结果码
        out.writeInt(msg.getStateCode());
        //长度
        int length = msg.getData() == null ? 0 : msg.getData().length;
        if(length<=0){
            out.writeInt(length);
        }else{
            out.writeInt(length);
            out.writeBytes(msg.getData());
        }
    }
}
