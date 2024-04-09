package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.atuth.login.service.ILoginService;
import com.myapp.chatgpt.data.domain.atuth.service.IAuthService;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description: 登录鉴权
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/login")
@CrossOrigin("*")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @Resource
    private IAuthService authService;

    /**
     * 获取微信凭证
     *
     * @return
     */
    @RequestMapping(value = "weixin_qrcode_ticket", method = RequestMethod.GET)
    public Response<String> wechatQrCodeTicket() {
        try {
            String qrCodeTicket = loginService.createQrCodeTicket();
            log.info("生成微信扫码登录 ticket {}", qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCEESS.getCode())
                    .info(Constants.ResponseCode.SUCEESS.getInfo())
                    .data(qrCodeTicket)
                    .build();

        } catch (Exception e) {
            log.info("生成微信扫码登录 ticket 失败", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 轮训登录
     *
     * @param ticket 登录凭证
     * @return
     */
    @RequestMapping(value = "check_login", method = RequestMethod.GET)
    public Response<String> checkLogin(@RequestParam String ticket) {
        try {
            // 1.判断是否登录
            String openidToken = loginService.checkLogin(ticket);
            log.info("扫描检测登录结果 ticket:{} openidToken:{}", ticket, openidToken);
            if (StringUtils.isNotBlank(openidToken)) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCEESS.getCode())
                        .info(Constants.ResponseCode.SUCEESS.getInfo())
                        .data(openidToken).build();
            } else {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.NO_LOGIN.getCode())
                        .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                        .data("")
                        .build();
            }
        } catch (Exception e) {
            log.info("扫描检测登录结果失败,出现异常 ticket:{}", ticket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

}
