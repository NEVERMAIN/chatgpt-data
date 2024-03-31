package com.myapp.chatgpt.data.infrastructure.dao;

import com.myapp.chatgpt.data.infrastructure.po.OpenAiProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 操作 openai_product 表的 dao 对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Mapper
public interface IOpenAiProductDao {

    /**
     * 查询产品的详细信息
     */
    OpenAiProductPO queryProduct(Integer productId);

    /**
     * 查询商品列表
     * @return
     */
    List<OpenAiProductPO> queryProductList();


}
