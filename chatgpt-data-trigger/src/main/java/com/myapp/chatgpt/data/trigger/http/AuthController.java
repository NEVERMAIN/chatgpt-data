package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.atuth.model.entity.AuthStateEntity;
import com.myapp.chatgpt.data.domain.atuth.model.vo.AuthTypeVO;
import com.myapp.chatgpt.data.domain.atuth.repository.IAuthRepository;
import com.myapp.chatgpt.data.domain.atuth.service.IAuthService;
import com.myapp.chatgpt.data.types.annotation.AccessInterceptor;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 处理登录权限
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/auth")
@CrossOrigin("*")
public class AuthController {

    @Resource
    private IAuthService authService;

    @Resource
    private IAuthRepository authRepository;


    @AccessInterceptor(key = "fingerprint",fallbackMethod = "doLoginErr",permitsPerSecond = 1.0d,blacklistCount = 10)
    @RequestMapping("/login")
    public Response<String> doLogin(@RequestParam String code,@RequestParam String fingerprint) {

        log.info("鉴权登录校验开始，验证码: {} 浏览器指纹:{}", code, fingerprint);
        try {
            // 验证用户
            AuthStateEntity authStateEntity = authService.doLogin(code);
            // 验证不通过
            if (!AuthTypeVO.SUCCESS.getCode().equals(authStateEntity.getCode())) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }

            // 查询用户是否存在
            String openid = authStateEntity.getOpenid();
            Integer count = authRepository.queryUserAccountExist(openid);
            if(count == 0){
                // 如果不存在就创建默认账户
                UserAccountQuotaEntity userAccountQuota = new UserAccountQuotaEntity();
                userAccountQuota.setOpenid(openid);
                authRepository.createUserAccount(userAccountQuota);
            }

            // 验证通过
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCEESS.getCode())
                    .info(Constants.ResponseCode.SUCEESS.getInfo())
                    .data(authStateEntity.getToken())
                    .build();

        } catch (Exception e) {
            log.info("用户鉴权出现异常:【验证码:{},异常信息:{}】", code, e.getMessage());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    public Response<String> doLoginErr(@RequestParam String code,@RequestParam String fingerprint){
        return Response.<String>builder()
                .code(Constants.ResponseCode.UN_ERROR.getCode())
                .info("频次限制，请勿恶意访问")
                .build();
    }


}
