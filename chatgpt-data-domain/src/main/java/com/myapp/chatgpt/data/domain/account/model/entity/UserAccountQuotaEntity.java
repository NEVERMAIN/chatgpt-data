package com.myapp.chatgpt.data.domain.account.model.entity;

import com.myapp.chatgpt.data.domain.openai.model.vo.UserAccountStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 用户账户对象
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountQuotaEntity {
    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 用户Id:这里用的是微信ID作为唯一ID
     */
    private String openid;
    /**
     * 总量余额：分配的总使用次数
     */
    private Integer totalQuota;
    /**
     * 剩余额度：剩余可使用的次数
     */
    private Integer surplusQuota;
    /**
     * 可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3
     */
    private List<String> availModes;
    /**
     * 账户状态:0-可用;1-冻结
     */
    private UserAccountStatusVO status;

    public void genModelTypes(String modelTypes){
        String[] values = modelTypes.split(",");
        this.availModes = Arrays.asList(values);
    }


}
