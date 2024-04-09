package com.myapp.chatgpt.data.infrastructure.gateway;

import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeRequestDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatQrCodeResponseDTO;
import com.myapp.chatgpt.data.infrastructure.gateway.dto.WeChatTokenResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @description: 微信API服务
 * @author: 云奇迹
 * @date: 2024/4/9
 */
public interface IWechatApiService {

    /**
     * 获取微信token
     * @param grantType 获取access_token填写client_credential
     * @param appid      第三方用户唯一凭证
     * @param appSecret  第三方用户唯一凭证密钥，即 appsecret
     * @return           响应结果
     */
    @GET("cgi-bin/token")
    Call<WeChatTokenResponseDTO> getToken(
            @Query("grant_type") String  grantType,
            @Query("appid") String appid,
            @Query("secret") String appSecret
    );

    /**
     * 创建二维码
     * @param access_token   调用接口凭证
     * @param weChatQrCodeRequestDTO  请求参数
     * @return              响应结果
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeChatQrCodeResponseDTO> createQrCode(
            @Query("access_token") String access_token,
            @Body WeChatQrCodeRequestDTO weChatQrCodeRequestDTO
    );






}
