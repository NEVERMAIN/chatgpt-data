package com.myapp.chatgpt.data.infrastructure.adapter;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.login.adapter.ILoginAdapter;
import com.myapp.chatgpt.data.infrastructure.gateway.IWechatApiService;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeRequestDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeResponseDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatTokenResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Slf4j
@Service
public class LoginAdapter implements ILoginAdapter {

    @Value("${wx.config.appid}")
    private String appid;

    @Value("${wx.config.app-secret}")
    private String appSecret;

    @Resource
    private Cache<String, String> weixinAccessToken;

    @Resource
    private IWechatApiService wechatApiService;

//    public static final String WECHAT_ACCESS_TOKEN = "wechat_access_token";


    /**
     * 创建二维码门票。
     * 该方法首先尝试从Redis获取微信访问令牌（accessToken），如果不存在，则通过调用微信API来获取。
     * 获取到accessToken后，使用它来创建二维码，并返回二维码的ticket。
     *
     * @return String 生成的二维码的 ticket，如果无法生成则返回空字符串。
     * @throws Exception 抛出异常的情况包括获取 accessToken 或创建二维码时遇到的网络错误。
     */
    @Override
    public String createQrCodeTicket() throws Exception {

        try {

            // 1. 获取 accessToken 【实际业务场景，按需处理下异常】
            String accessToken = weixinAccessToken.getIfPresent(appid);
            if (null == accessToken) {
                // 从微信API获取accessToken
                Call<WeChatTokenResponseDTO> call = wechatApiService.getToken("client_credential", appid, appSecret);
                WeChatTokenResponseDTO weChatTokenResponseDTO = call.execute().body();
                accessToken = weChatTokenResponseDTO.getAccess_token();
                if(accessToken == null){
                    log.error("微信公众号登录-获取 access_token 失败:{}",weChatTokenResponseDTO.getErrmsg());
                    throw new Exception(weChatTokenResponseDTO.getErrmsg());
                }
                weixinAccessToken.put(appid, accessToken);
            }

            // 2. 生成 ticket
            // 构建二维码请求参数
            WeChatQrCodeRequestDTO weChatQrCodeRequestDTO = WeChatQrCodeRequestDTO.builder()
                    .expire_seconds(604800)
                    .action_name(WeChatQrCodeRequestDTO.ActionNameTypeVO.QR_SCENE.getCode())
                    .action_info(WeChatQrCodeRequestDTO.ActionInfo.builder()
                            .scene(WeChatQrCodeRequestDTO.ActionInfo.Scene.builder()
                                    .scene_id(1234)
                                    .build())
                            .build())
                    .build();

            // 调用微信 API 创建二维码
            Call<WeChatQrCodeResponseDTO> qrCodeCall = wechatApiService.createQrCode(accessToken, weChatQrCodeRequestDTO);
            WeChatQrCodeResponseDTO weChatQrCodeResponseDTO = qrCodeCall.execute().body();
            // 返回二维码的ticket
            if (weChatQrCodeResponseDTO != null) {
                String ticket = weChatQrCodeResponseDTO.getTicket();

                if (ticket == null) {
                    log.error("微信公众号登录-获取 ticket 失败:{}",weChatQrCodeResponseDTO.getErrmsg());
                    throw new RuntimeException(weChatQrCodeResponseDTO.getErrmsg());
                }
                return ticket;
            }
            return "";

        } catch (IOException e) {
            // 将IO异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }

    }

}
