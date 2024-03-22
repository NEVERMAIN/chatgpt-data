package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.*;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.model.vo.ProductModelTypeVO;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.infrastructure.dao.IOpenAiOrderDao;
import com.myapp.chatgpt.data.infrastructure.dao.IOpenAiProductDao;
import com.myapp.chatgpt.data.infrastructure.po.OpenAiOrderPO;
import com.myapp.chatgpt.data.infrastructure.po.OpenAiProductPO;
import com.myapp.chatgpt.data.types.enums.OpenAIProductEnableModel;

import javax.annotation.Resource;

/**
 * @description: 订单服务的仓储仓储实现
 * @author: 云奇迹
 * @date: 2024/3/22
 */
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAiOrderDao openAiOrderDao;
    @Resource
    private IOpenAiProductDao openAiProductDao;


    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCarEntity shopCarEntity) {

        OpenAiOrderPO openAiOrderPOReq = new OpenAiOrderPO();
        openAiOrderPOReq.setOrderId(shopCarEntity.getOpenid());
        openAiOrderPOReq.setProductId(shopCarEntity.getProductId());
        OpenAiOrderPO openAiOrderPO = openAiOrderDao.queryUnpaidOrder(openAiOrderPOReq);
        if (openAiOrderPO == null) return null;
        // 构建未支付的订单对象
        return UnpaidOrderEntity.builder()
                .orderId(openAiOrderPO.getOrderId())
                .openid(openAiOrderPO.getOpenid())
                .totalAmount(openAiOrderPO.getTotalAmount())
                .payStatus(PayStatusVo.get(openAiOrderPO.getPayStatus()))
                .productName(openAiOrderPO.getProductName())
                .payUrl(openAiOrderPO.getPayUrl())
                .build();
    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        // 从数据库查询产品持久化对象
        OpenAiProductPO openAiProductPO = openAiProductDao.queryProduct(productId);
        // 创建产品对象
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(openAiProductPO.getProductId());
        productEntity.setProductName(openAiProductPO.getProductName());
        productEntity.setProductDesc(openAiProductPO.getProductDesc());
        productEntity.setQuota(openAiProductPO.getQuota());
        productEntity.setPrice(openAiProductPO.getPrice());
        productEntity.setEnable(OpenAIProductEnableModel.get(openAiProductPO.getIsEnabled()));
        return productEntity;
    }


    @Override
    public void saveOrder(CreateOrderAggregate createOrderAggregate) {
        String openid = createOrderAggregate.getOpenid();
        OrderEntity order = createOrderAggregate.getOrder();
        ProductEntity product = createOrderAggregate.getProduct();

        OpenAiOrderPO openAiOrderPOReq = OpenAiOrderPO.builder()
                .openid(openid)
                .orderId(order.getOrderId())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus().getCode())
                .totalAmount(order.getTotalAmount())
                .payStatus(PayStatusVo.WAIT.getCode())
                .payType(PayTypeVO.WEIXIN_NATIVE.getCode())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productQuota(product.getQuota())
                .productModelTypes(ProductModelTypeVO.DEFAULT_MODEL.getCode())
                .build();

        openAiOrderDao.insert(openAiOrderPOReq);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        OpenAiOrderPO openAiOrderPOReq = new OpenAiOrderPO();
        openAiOrderPOReq.setOpenid(payOrderEntity.getOpenid());
        openAiOrderPOReq.setOrderId(payOrderEntity.getOrderId());
        openAiOrderPOReq.setPayUrl(payOrderEntity.getPayUrl());
        openAiOrderPOReq.setPayStatus(payOrderEntity.getPayStatus().getCode());
        openAiOrderDao.updateOrderPayInfo(openAiOrderPOReq);
    }

}
