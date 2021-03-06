package com.example.emos.wx.config.shiro;

import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述:  完成认证与授权的Realm类
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     *
     * 授权（登录时调用）
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //TODO 查询用户的权限列表
        //TODO 把权限列表添加到info对象中
        return info;
    }

    /**
     *
     * 认证（验证登录时调用）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //TODO 从令牌中获取userId，然后检测该账户是否被冻结
        String accessToken = (String) authenticationToken.getPrincipal();
        int userId = jwtUtil.getUserId(accessToken);
        TbUser user = userService.searchById(userId);
        //如果该账户处于离职状态，那么status字段为0，无法查找到，返回空对象
        if (user == null){
            throw new LockedAccountException("账号已被锁定，请联系管理员");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,accessToken,getName());
        //TODO 往info对象中添加用户信息，Token字符串
        return info;
    }
}
