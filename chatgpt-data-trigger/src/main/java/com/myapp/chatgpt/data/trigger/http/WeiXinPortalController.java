package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinValidateService;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import com.myapp.chatgpt.data.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private IWeiXinValidateService weiXinAuthService;

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

        log.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
        try {
            if(StringUtils.isAnyBlank(signature,timestamp,nonce,echostr)){
                throw new IllegalArgumentException("请求参数非法,请核实");
            }
            boolean success = weiXinAuthService.checkValid(signature, timestamp, nonce);
            log.info("微信公众号验签信息{}完成 check：{}", appid, success);
            if(!success){
                return null;
            }
            return echostr;
        } catch (RuntimeException e) {
            log.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
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


        try {
            log.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);
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

            String result = weiXinBehaviorService.acceptUserBehavior(behaviorMessageEntity);
            log.info("接收微信公众号信息请求完成:【openId:{},result:{}】",openid,result);
            return result;
        } catch (Exception e) {
            log.info("微信公众号接收用户请求出现异常:【openId:{} , 异常信息:{}】",openid,e.getMessage());
            return "";
        }
    }

}
