package com.example.emos.wx.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 描述:  JWT工具类
 */
@Component
@Slf4j
public class JwtUtil {
    @Value("${emos.jwt.secret}")
    private String secret;

    @Value("${emos.jwt.expire}")
    private int expire;

    public String createToken(int userId){
        Date date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR,5);//找到偏移5天的日期
        Algorithm algorithm = Algorithm.HMAC256(secret); //加密算法生成密钥,封装成加密对象
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId",userId).withExpiresAt(date).sign(algorithm);//由用户ID，过期日期，加密密钥生成token
        return token;
    }

    public int getUserId(String token){
        DecodedJWT jwt = JWT.decode(token);
        int userId = jwt.getClaim("userId").asInt();
        return userId;
    }

    public void verifierToken(String token){//验证令牌有效性
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();//生成验证对象
        verifier.verify(token);
    }
}
