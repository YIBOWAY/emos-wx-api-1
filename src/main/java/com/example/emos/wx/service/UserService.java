package com.example.emos.wx.service;

import java.util.Set;

/**
 * 描述:  用户服务接口
 */
public interface UserService {
    public int registerUser(String registerCode,String code,String nickname,String photo);

    public Set<String> searchUserPermissions(int userId);
}
