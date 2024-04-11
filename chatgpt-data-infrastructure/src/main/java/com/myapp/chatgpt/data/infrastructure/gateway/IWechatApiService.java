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
     * 本方法用于通过提供的接口凭证和请求参数，在微信平台上创建二维码。
     *
     * @param accessToken   调用接口凭证，用于验证请求的合法性。
     * @param weChatQrCodeRequestDTO  请求参数，包含了创建二维码所需的全部信息。
     * @return              响应结果，包含了创建二维码后的相关信息。
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeChatQrCodeResponseDTO> createQrCode(
            @Query("access_token") String accessToken,
            @Body WeChatQrCodeRequestDTO weChatQrCodeRequestDTO
    );







}
