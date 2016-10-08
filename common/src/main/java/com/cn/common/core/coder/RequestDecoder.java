package com.cn.common.core.coder;

import com.cn.common.core.model.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 数据包解码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号      |  命令号    |   长度     |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节
 * 命令号2字节
 * 长度4字节(数据部分占有字节数量)
 *
 * Created by 1115 on 2016/9/23.
 */
public class RequestDecoder  extends ByteToMessageDecoder {
    /**
     * 数据包基本长度
     */
    public static int BASE_LENTH = 4 + 2 + 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("server:"+in.readableBytes());
        while(true){
            if(in.readableBytes()>=BASE_LENTH){
                //第一个可读数据包的起始位置
                int beginIndex;
                while(true){
                    //包头开始游标点
                    beginIndex = in.readerIndex();
                    //标记初始读游标位置
                    in.markReaderIndex();
                    if(in.readInt()==Constant.HEADER_FLAG){
                        break;
                    }
                    //未读到包头标识略过一个字节
                    in.resetReaderIndex();
                    //读游标移动一位
                    in.readByte();
                    if(in.readableBytes()<BASE_LENTH){
                        return;
                    }
                }
                short module = in.readShort();
                short cmd = in.readShort();
                //读取数据长度
                int length = in.readInt();
                if(length<0){
                    ctx.channel().close();
                }
                //数据包还没到齐
                if(in.readableBytes()<length){
                    in.readerIndex(beginIndex);
                    return ;
                }
                //读数据部分
                byte[] data = new byte[length];
                in.readBytes(data);

                Request msg = new Request();
                msg.setModule(module);
                msg.setCmd(cmd);
                msg.setData(data);
                //解析出消息对象，继续往下面的handler传递
                out.add(msg);
            }else{
                break;
            }
        }
        //数据不完整，等待完整的数据包
        return;
    }
}
