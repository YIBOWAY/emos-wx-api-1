package com.example.emos.wx.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 描述:  token字符串对象封装类
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
