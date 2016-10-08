package com.cn.common.core.coder;

import com.cn.common.core.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号      |  命令号    |   长度     |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * Created by 1115 on 2016/9/23.
 */
public class ResponseDecoder extends ByteToMessageDecoder {
    /**
     * 数据包基本长度
     */
    public static int BASE_LENTH = 4 + 2 + 2 + 4 + 4;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("Client:"+in.readableBytes());
        while(true){
            if(in.readableBytes()>=BASE_LENTH) {
                int beginReadIndex;
                while (true) {
                    //包头开始游标点
                    beginReadIndex = in.readerIndex();
                    in.markReaderIndex();
                    if (in.readInt() == Constant.HEADER_FLAG) {
                        break;
                    }
                    //未读到包头标识略过一个字节
                    in.resetReaderIndex();
                    in.readByte();
                    if (in.readableBytes() < BASE_LENTH) {
                        return;
                    }
                }
                short module = in.readShort();
                short cmd = in.readShort();
                int stateCode = in.readInt();
                Response response = new Response();
                response.setModule(module);
                response.setCmd(cmd);
                int length = in.readInt();
                if (length < 0) {
                    ctx.channel().close();
                }
                //数据包还没到齐
                if (in.readableBytes() < length) {
                    in.readerIndex(beginReadIndex);
                    return;
                }
                byte[] data = new byte[length];
                in.readBytes(data);
                response.setData(data);
                response.setStateCode(stateCode);
                //解析出消息对象，继续往下面的handler传递
                out.add(response);
            }else{
                break;
            }
        }
        return;
    }
}
