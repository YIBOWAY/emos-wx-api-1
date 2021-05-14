package com.example.emos.wx.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.wx.db.dao.TbUserDao;
import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * 描述:  service实现类
 */
@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private TbUserDao tbUserDao;

    private String getOpenId(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url,map);
        JSONObject json = JSONUtil.parseObj(response);//转换成json格式类型
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0){
            throw new RuntimeException("临时登录凭证错误");
        }
        return openId;
    }

    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        //首先判断激活码是否要绑定超级管理员
        if (registerCode.equals("000000")){
            boolean bool = tbUserDao.haveRootUser();//查询是否已有超级管理员
            if (!bool){//如果没有超级管理员，则可进行绑定
                String openId = getOpenId(code);
                HashMap param = new HashMap();
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");//用户角色，0代表超级管理员，一个用户可能有多个角色，因此是json的格式类型
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                tbUserDao.insert(param);
                int id = tbUserDao.searchIdByOpenId(openId);
                return id;
            }else {
                throw new EmosException("无法绑定超级管理员账号");
            }
        }else {

        }
        return 0;
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions = tbUserDao.searchUserPermissions(userId);
        return permissions;
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = tbUserDao.searchIdByOpenId(openId);
        if (id == null){
            throw new EmosException("账户不存在，请先注册");
        }
        //TODO 从消息队列中接收消息，转移到消息表
        return id;
    }

    @Override
    public TbUser searchById(int userId) { //得到用户信息
        TbUser user = tbUserDao.searchById(userId);
        return user;
    }
}
