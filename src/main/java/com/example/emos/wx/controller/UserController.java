package com.example.emos.wx.controller;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.RegisterForm;
import com.example.emos.wx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 描述:  用户controller
 */
@RestController
@RequestMapping("/user")
@Api("用户模块Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public R register(@Valid @RequestBody RegisterForm registerForm){
        int id = userService.registerUser(registerForm.getRegisterCode(),registerForm.getCode(),registerForm.getNickname(),registerForm.getPhoto());
        String token = jwtUtil.createToken(id);
        Set<String> permsSet = userService.searchUserPermissions(id);
        saveCacheToken(token,id);
        return R.ok("用户注册成功").put("token",token).put("permission",permsSet);

    }

    private void saveCacheToken(String token,int userId){
        redisTemplate.opsForValue().set(token,userId+"",cacheExpire, TimeUnit.DAYS);
    }
}
