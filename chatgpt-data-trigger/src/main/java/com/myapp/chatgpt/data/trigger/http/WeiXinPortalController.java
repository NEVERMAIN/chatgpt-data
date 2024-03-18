package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.weixin.model.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.UserBehaviorMessageEntity;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinAuthService;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import com.myapp.chatgpt.data.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @description: 处理微信验签和用户调用
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/wx/portal/{appid}")
@CrossOrigin("*")
public class WeiXinPortalController {

    @Value("${wx.config.originalid}")
    private String originalid;

    @Resource
    private IWeiXinAuthService weiXinAuthService;

    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;


    /**
     * 验签方法
     * @param appid
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {

        log.info("微信公众号验签开始: 【appid{}】",appid);
        try {
            boolean success = weiXinAuthService.checkValid(signature, timestamp, nonce);
            if(!success){
                throw new RuntimeException();
            }
            return echostr;
        } catch (RuntimeException e) {
            log.info("微信公众号验签出现异常: 【appid{},异常信息:{}】",appid,e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理用户信息
     * @return
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        log.info("微信公众号接收用户信息开始:【openId:{},请求内容:{}】",openid,requestBody);

        try {
            // 1. 将 xml 格式转成 Bean 对象
            MessageTextEntity messageText = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);

            // 2.转成用户行为信息对象
            UserBehaviorMessageEntity behaviorMessageEntity = UserBehaviorMessageEntity.builder()
                    .event(messageText.getEvent())
                    .msgType(messageText.getMsgType())
                    .openId(openid)
                    .createTime(new Date(System.currentTimeMillis() / 1000L))
                    .fromUserName(messageText.getFromUserName())
                    .content(messageText.getContent())
                    .build();

            String code = weiXinBehaviorService.post(behaviorMessageEntity);

            MessageTextEntity res = MessageTextEntity.builder()
                    .toUserName(behaviorMessageEntity.getOpenId())
                    .fromUserName(originalid)
                    .createTime(String.valueOf(System.currentTimeMillis() / 1000L))
                    .msgType("text")
                    .content("你的验证码是"+code+",有效期为三分钟")
                    .build();

            String result = XmlUtil.beanToXml(res);
            log.info("微信公众号接收用户请求完成:【openId:{} , 响应结果:{}】",openid,result);
            return result;
        } catch (Exception e) {
            log.info("微信公众号接收用户请求出现异常:【openId:{} , 异常信息:{}】",openid,e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
