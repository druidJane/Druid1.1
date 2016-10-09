package com.cn.common.core.model;

import java.io.Serializable;

/**
 * Created by 1115 on 2016/10/9.
 */
public class Result<T> {
    private int resultCode;

    private T content;

    public static <T> Result<T> SUCCESS(T content){
        Result<T> result = new Result<T>();
        result.resultCode = ResultCode.SUCCESS;
        result.content = content;
        return result;
    }
    public static <T> Result<T> SUCCESS(){
        Result<T> result = new Result<T>();
        result.resultCode = ResultCode.SUCCESS;
        return result;
    }
    public static <T> Result<T> ERROR(int resultCode){
        Result<T> result = new Result<T>();
        result.setResultCode(resultCode);
        return result;
    }
    public static <T>Result<T> valueOf(int resultCode,T content){
        Result<T> result = new Result<T>();
        result.setResultCode(resultCode);
        result.setContent(content);
        return result;
    }
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getResultCode() {

        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
