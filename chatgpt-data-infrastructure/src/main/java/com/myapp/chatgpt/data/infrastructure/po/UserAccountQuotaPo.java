package com.myapp.chatgpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 用户账单对象
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountQuotaPo {
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
    private String modelTypes;
    /**
     * 账户状态:0-可用;1-冻结
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
