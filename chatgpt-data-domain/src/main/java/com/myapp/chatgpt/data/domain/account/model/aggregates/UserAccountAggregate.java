package com.myapp.chatgpt.data.domain.account.model.aggregates;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户账户聚合对象
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountAggregate {

    private UserAccountQuotaEntity userAccountQuotaEntity;


}
