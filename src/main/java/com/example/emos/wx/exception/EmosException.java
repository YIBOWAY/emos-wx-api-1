package com.example.emos.wx.exception;

import lombok.Data;

/**
 * 描述:  自定义异常类
 */
@Data
public class EmosException extends RuntimeException{
    private String msg;
    private int code = 500; //set get 方法由lombok生成

    public EmosException(String msg){
        super(msg);
        this.msg = msg;
    }

    public EmosException(String msg,Throwable e){
        super(msg,e);
        this.msg = msg;
    }

    public EmosException(String msg,int code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public EmosException(String msg, int code, Throwable e){
        super(msg,e);
        this.msg = msg;
        this.code = code;
    }
}
