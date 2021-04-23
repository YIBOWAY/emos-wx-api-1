package com.example.emos.wx.common.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:  统一返回对象
 */
public class R extends HashMap<String,Object> {
    public R(){
        put("code", HttpStatus.SC_OK); //绑定数据
        put("msg","success");
    }

    @Override
    public R put(String key, Object value){//链式调用
        super.put(key, value);
        return this;
    }

    public static R ok(){
        return new R();
    }

    public static R ok(String msg){
        R r = new R();
        r.put("msg",msg);
        return r;
    }

    public static R ok(Map<String,Object> map){
        R r = new R();
        r.putAll(map);//绑定所有
        return r;
    }

    public static R error(int code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static R error(String msg){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,msg);
    }

    public static R error(){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知异常，请联系管理员");
    }
}
