package com.myapp.chatgpt.data.domain.atuth.repository;


import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;

/**
 * @description: 认证仓储服务接口
 * @author: 云奇迹
 * @date: 2024/4/1
 */
public interface IAuthRepository {

    /**
     * 从缓存中获取验证码
     * @param code 验证码标识，用于在缓存中定位验证码
     * @return 返回对应于验证码标识的验证码字符串。如果找不到，则返回null或空字符串。
     */
    String getCodeByOpenId(String code);

    /**
     * 删除缓存中的验证码
     * @param code 验证码标识，指定要删除的验证码。
     * @param openId 与验证码关联的用户开放标识，用于精确删除。
     */
    void removeCodeByOpenId(String code,String openId);


    /**
     * 查询用户账户
     *
     * @param openid 用户的唯一标识符
     * @return 如果账户存在返回账户ID，否则返回null
     */
    Integer queryUserAccountExist(String openid);

    /**
     * 创建用户账户
     *
     * @param entity 包含用户账户信息的数据实体
     * 该方法不返回任何内容，但会将提供的用户账户信息创建或更新到数据库中
     */
    void createUserAccount(UserAccountQuotaEntity entity);


}
