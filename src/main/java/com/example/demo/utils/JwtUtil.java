package com.example.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {

    private static final String SIGNATURE = "nickName";

    /**
     * 生产 token
     * @param map
     * @return
     */
    public static String produceToken(Map<String,String> map) {

//        Map<String, Object> map = new HashMap<>();
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.SECOND, 20);
//        return JWT.create()
//                .withHeader(map) //可以不设定，就是使用默认的
////                .withClaim("userId",20)//payload  //自定义用户名
//                .withClaim("username", user.getUserName())
//                .withExpiresAt(instance.getTime()) //指定令牌过期时间
//                .sign(Algorithm.HMAC256(SIGNATURE));

        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,6);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token
     * @param token
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }


    /**
     * 解析 token
     * @param token
     * @return
     */

    public static DecodedJWT parseToken(String token) {
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SIGNATURE)).build();
//        DecodedJWT decodedJWT = jwtVerifier.verify(token);
//        decodedJWT.getClaim("userId").asInt();//获取负载里面对应的内容
//        decodedJWT.getClaim("userName").asString();
        // 有效负载不可以放密码等敏感信息
//        decodedJWT.getClaim("password").asString();
//        decodedJWT.getExpiresAt();//获取过期时间
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }


}
