package com.myapp.chatgpt.data.infrastructure.dao;

import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 操作 user_account 表的dao
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Mapper
public interface UserAccountDao {

    /**
     * 查询用户账户
     * @param openid
     * @return
     */
    UserAccountQuotaPo query(String openid);

    /**
     * 扣减库存
     */
    Integer subAccountQuota(String openid);


}
