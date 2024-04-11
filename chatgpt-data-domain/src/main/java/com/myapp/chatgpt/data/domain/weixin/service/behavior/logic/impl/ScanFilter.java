package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.repository.IWeiXinRepository;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 扫码登录
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Service("scan")
@Slf4j
public class ScanFilter implements LogicFilter {

    private final Logger logger = LoggerFactory.getLogger(SubscribeFilter.class);

//    @Resource
//    private IWeiXinRepository weiXinRepository;

    @Resource
    private Cache<String, String> openidToken;


    /**
     * SecretKey 要替换为你自己的，并且最好是通过配置的方式使用
     */
    private static final String DEFAULT_BASE64_ENCODED_SECRET_KEY = "B*B^D%fe";
    private final String base64EncodedSecretKey = Base64.encodeBase64String(DEFAULT_BASE64_ENCODED_SECRET_KEY.getBytes());
    private final Algorithm algorithm = Algorithm.HMAC256(Base64.decodeBase64(Base64.encodeBase64String(DEFAULT_BASE64_ENCODED_SECRET_KEY.getBytes())));


    @Override
    public String filter(BehaviorMatter behaviorMessage) {
        // 生成 jwt 的 token 让前端存储
        String ticket = behaviorMessage.getTicket();
        String openid = behaviorMessage.getOpenId();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("openId", openid);
        String token = jwtEncode(openid, 7 * 24 * 60 * 60 * 1000L, claims);
        openidToken.put(ticket, token);

        return "";
    }

    /**
     * 生成 jwt Token
     * @param issuer
     * @param ttlMillis
     * @param claims
     * @return
     */
    protected String jwtEncode(String issuer, long ttlMillis, Map<String, Object> claims) {
        // iss签发人，ttlMillis生存时间，claims是指还想要在jwt中存储的一些非隐私信息
        if (claims == null) {
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                // 荷载部分
                .setClaims(claims)
                // 这个是JWT的唯一标识，一般设置成唯一的，这个方法可以生成唯一标识
                .setId(UUID.randomUUID().toString())
                // 签发时间
                .setIssuedAt(new Date(nowMillis))
                // 签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .setSubject(issuer)
                //这个地方是生成jwt使用的算法和秘钥
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            // 4. 过期时间，这个也是使用毫秒生成的，使用当前时间+前面传入的持续时间生成
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


}
