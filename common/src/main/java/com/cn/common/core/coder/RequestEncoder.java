package com.cn.common.core.coder;

import com.cn.common.core.model.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号      |  命令号    |   长度     |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * Created by 1115 on 2016/9/23.
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf buffer) throws Exception {
        System.out.println("client:"+msg.getCmd());
        //包头
        buffer.writeInt(Constant.HEADER_FLAG);
        //模块号
        buffer.writeShort(msg.getModule());
        //命令号
        buffer.writeShort(msg.getCmd());
        //长度
        int length = msg.getData() == null ? 0 : msg.getData().length;
        if(length<=0){
            buffer.writeInt(length);
        }else{
            buffer.writeInt(length);
            //数据
            buffer.writeBytes(msg.getData());
        }
    }
}
