package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbUser;

import java.util.Set;

/**
 * 描述:  用户服务接口
 */
public interface UserService {
    public int registerUser(String registerCode,String code,String nickname,String photo);

    public Set<String> searchUserPermissions(int userId);

    public Integer login(String code);

    public TbUser searchById(int userId);
}
