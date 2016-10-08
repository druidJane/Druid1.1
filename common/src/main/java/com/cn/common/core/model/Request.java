package com.cn.common.core.model;

/**
 * Created by 1115 on 2016/9/23.
 */
public class Request {

    /**
     * 模块号
     */
    private short module;

    /**
     * 命令号
     */
    private short cmd;

    /**
     * 数据
     */
    private byte[] data;

    public static Request valueOf(short module,short cmd,byte[] data){
        Request req = new Request();
        req.setCmd(cmd);
        req.setModule(module);
        req.setData(data);
        return req;
    }

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
