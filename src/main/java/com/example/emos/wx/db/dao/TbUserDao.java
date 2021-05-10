package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Set;

@Mapper
public interface TbUserDao {
    public boolean haveRootUser();

    public int insert(HashMap param);

    public Integer searchIdByOpenId(String openId);//一些相关查询要利用主键来进行，主键作为数据库的索引，速度更快

    public Set<String> searchUserPermissions(int userId);
}