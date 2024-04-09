package com.myapp.chatgpt.data.infrastructure.adapter;

import com.myapp.chatgpt.data.domain.atuth.login.adapter.ILoginAdapter;
import com.myapp.chatgpt.data.infrastructure.gateway.IWechatApiService;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeRequestDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeResponseDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatTokenResponseDTO;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

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
    private IRedisService redisService;

    @Resource
    private IWechatApiService wechatApiService;

    public static final String WECHAT_ACCESS_TOKEN = "wechat_access_token";

    @Override
    public String createQrCodeTicket() throws Exception {

        try {

            // 1.获取 accessToken
            String accessToken = redisService.getValue(WECHAT_ACCESS_TOKEN + "_" + appid);
            if (null == accessToken) {
                Call<WeChatTokenResponseDTO> call = wechatApiService.getToken("client_credential", appid, appSecret);
                WeChatTokenResponseDTO weChatTokenResponseDTO = call.execute().body();
                assert Objects.requireNonNull(weChatTokenResponseDTO).getAccess_token() != null;
                accessToken = weChatTokenResponseDTO.getAccess_token();
                redisService.setValue(WECHAT_ACCESS_TOKEN + "_" + appid, accessToken, 2 * 60 * 60 * 1000);
            }

            // 2.生成 ticket
            WeChatQrCodeRequestDTO weChatQrCodeRequestDTO = WeChatQrCodeRequestDTO.builder()
                    .expire_seconds(2592000)
                    .action_name(WeChatQrCodeRequestDTO.ActionNameTypeVO.QR_SCENE.getCode())
                    .action_info(WeChatQrCodeRequestDTO.ActionInfo.builder()
                            .scene(WeChatQrCodeRequestDTO.ActionInfo.Scene.builder()
                                    .scene_id(100601)
                                    .build())
                            .build())
                    .build();

            Call<WeChatQrCodeResponseDTO> qrCodeCall = wechatApiService.createQrCode(accessToken, weChatQrCodeRequestDTO);
            WeChatQrCodeResponseDTO weChatQrCodeResponseDTO = qrCodeCall.execute().body();
            if(weChatQrCodeResponseDTO != null){
                return weChatQrCodeResponseDTO.getTicket();
            }
            return "";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
