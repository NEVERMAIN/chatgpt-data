package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
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

    /**
     * 微信公众号鉴权服务
     */
    @Resource
    private IWeiXinValidateService weiXinAuthService;
    /**
     * 微信公众号用户行为服务
     */
    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;


    /**
     * 验证微信公众号请求的真实性
     *
     * @param appid     公众号的APPID，用于标识不同的公众号
     * @param signature 微信加密签名，用于验证请求的真实性
     * @param timestamp 签名生成的时间戳
     * @param nonce     随机数，用于防止网络重放攻击
     * @param echostr   随机字符串，用于验证请求的真实性
     * @return 如果验证成功，返回 echostr 参数的值；验证失败或参数非法，返回null
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {

        // 记录开始验签的日志
        log.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
        try {
            // 检查参数是否为空
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法,请核实");
            }
            // 执行验签逻辑
            boolean success = weiXinAuthService.checkValid(signature, timestamp, nonce);
            // 记录验签完成的日志
            log.info("微信公众号验签信息{}完成 check：{}", appid, success);
            if (!success) {
                return null;
            }
            return echostr;
        } catch (RuntimeException e) {
            // 记录验签失败的日志
            log.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }


    /**
     * 处理用户发送的信息，用于微信公众号交互。
     *
     * @param appid        公众号的唯一标识。
     * @param requestBody  用户发送的消息的XML字符串。
     * @param signature    微信加密签名，用于验证消息的真实性。
     * @param timestamp    时间戳，用于验证消息的真实性。
     * @param nonce        随机数，用于验证消息的真实性。
     * @param openid       用户的唯一标识。
     * @param encType      加密类型，可选参数，表示消息是否加密。
     * @param msgSignature 消息签名，用于验证消息的真实性。
     * @return 返回处理后的响应信息，格式可以是XML。
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
            // 记录接收到微信公众号信息的开始日志
            log.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);

            // 将接收到的XML格式消息转换为Bean对象
            MessageTextEntity messageText = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);

            // 将消息Bean对象转换为用户行为信息对象
            BehaviorMatter behaviorMessageEntity = BehaviorMatter.builder()
                    .event(messageText.getEvent())
                    .msgType(messageText.getMsgType())
                    .openId(openid)
                    .createTime(new Date(System.currentTimeMillis() / 1000L))
                    .fromUserName(messageText.getFromUserName())
                    .content(messageText.getContent())
                    .ticket(messageText.getTicket())
                    .build();

            // 处理用户的行为信息，返回处理结果
            String result = weiXinBehaviorService.acceptUserBehavior(behaviorMessageEntity);
            // 记录处理微信公众号信息请求完成的日志
            log.info("接收微信公众号信息请求完成:【openId:{},result:{}】", openid, result);
            return result;
        } catch (Exception e) {
            // 记录处理微信公众号信息请求出现异常的日志
            log.info("微信公众号接收用户请求出现异常:【openId:{} , 异常信息:{}】", openid, e.getMessage());
            return "";
        }
    }


}
