package com.example.emos.wx.config.shiro;

import org.springframework.stereotype.Component;

/**
 * 描述:  存储令牌的媒介类
 */
@Component
public class ThreadLocalToken {
    private ThreadLocal<String> local = new ThreadLocal<>();

    public void setToken(String token){
        local.set(token);
    }

    public String getToken(){
        return local.get();
    }

    public void clear(){
        local.remove();
    }
}
//在ThreadLocal中，只要是同一个线程，往ThreadLocal里面写入数据和读取数据是完全相同的
//而在Web项目中，从OAuth2Filter到AOP切面类，都是由同一个线程来执行的，中途不会更换线程，因此存储的数据都相同。