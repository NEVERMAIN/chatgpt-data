package com.myapp.chatgpt.data.trigger.http;

import com.myapp.chatgpt.data.domain.account.model.aggregates.UserAccountAggregate;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountEntity;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.account.model.vo.AccountStatusVO;
import com.myapp.chatgpt.data.domain.account.service.IUserAccountService;
import com.myapp.chatgpt.data.domain.atuth.service.IAuthService;
import com.myapp.chatgpt.data.trigger.http.dto.UserAccountQuotaDTO;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@RestController
@RequestMapping("/api/${app.config.api-version}/account")
public class UserAccountController {

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IAuthService authService;

    @PostMapping("/add")
    public Response createUserAccount(@RequestHeader("Authorization") String token) {

        String openid = authService.getOpenId(token);
        UserAccountAggregate userAccountAggregate = UserAccountAggregate.builder()
                .userAccountQuotaEntity(UserAccountQuotaEntity.builder()
                        .openid(openid)
                        .totalQuota(10)
                        .surplusQuota(10)
                        .availModes(Arrays.asList("glm-3-turbo")).build())
                .build();

        UserAccountEntity accountEntity = userAccountService.createUserAccount(userAccountAggregate);
        if (!AccountStatusVO.SUCCESS.getCode().equals(accountEntity.getCode())) {
            return Response.builder()
                    .code(accountEntity.getCode())
                    .info(accountEntity.getInfo())
                    .build();
        }

        return Response.builder()
                .code(Constants.ResponseCode.SUCEESS.getCode())
                .info(Constants.ResponseCode.SUCEESS.getInfo())
                .build();
    }
}
