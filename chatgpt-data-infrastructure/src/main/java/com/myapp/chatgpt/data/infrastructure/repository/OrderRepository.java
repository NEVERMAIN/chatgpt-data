package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.*;
import com.myapp.chatgpt.data.domain.order.model.vo.OrderStatusVO;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.model.vo.ProductModelTypeVO;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.infrastructure.dao.IOpenAiOrderDao;
import com.myapp.chatgpt.data.infrastructure.dao.IOpenAiProductDao;
import com.myapp.chatgpt.data.infrastructure.dao.IUserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.OpenAiOrderPO;
import com.myapp.chatgpt.data.infrastructure.po.OpenAiProductPO;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import com.myapp.chatgpt.data.types.enums.OpenAIProductEnableModel;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.xml.soap.SAAJResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单服务的仓储仓储实现
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAiOrderDao openAiOrderDao;
    @Resource
    private IOpenAiProductDao openAiProductDao;

    @Resource
    private IUserAccountDao userAccountDao;


    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCarEntity shopCarEntity) {

        OpenAiOrderPO openAiOrderPOReq = new OpenAiOrderPO();
        openAiOrderPOReq.setOrderId(shopCarEntity.getOpenid());
        openAiOrderPOReq.setProductId(shopCarEntity.getProductId());
        // 1. 查询未支付的订单
        OpenAiOrderPO openAiOrderPO = openAiOrderDao.queryUnpaidOrder(openAiOrderPOReq);
        if (openAiOrderPO == null) return null;
        // 2. 构建未支付的订单对象
        return UnpaidOrderEntity.builder()
                .orderId(openAiOrderPO.getOrderId())
                .openid(openAiOrderPO.getOpenid())
                .totalAmount(openAiOrderPO.getTotalAmount())
                .payStatus(PayStatusVo.get(openAiOrderPO.getPayStatus()))
                .productName(openAiOrderPO.getProductName())
                .payUrl(openAiOrderPO.getPayUrl())
                .payType(PayTypeVO.get(openAiOrderPO.getPayType()))
                .build();
    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        // 1. 从数据库查询产品持久化对象
        OpenAiProductPO productPo = openAiProductDao.queryProduct(productId);
        // 2. 创建产品对象
        ProductEntity productEntity = ProductEntity.builder()
                .productId(productPo.getProductId())
                .productName(productPo.getProductName())
                .productDesc(productPo.getProductDesc())
                .productModelTypes(productPo.getProductModelTypes())
                .quota(productPo.getQuota())
                .price(productPo.getPrice())
                .enable(OpenAIProductEnableModel.get(productPo.getIsEnabled()))
                .build();
        // 3.返回产品对象
        return productEntity;
    }

    @Override
    public void saveOrder(CreateOrderAggregate createOrderAggregate) {
        // 1.取出 openid
        String openid = createOrderAggregate.getOpenid();
        OrderEntity order = createOrderAggregate.getOrder();
        ProductEntity product = createOrderAggregate.getProduct();

        // 2.创建 order 持久化对象
        OpenAiOrderPO openAiOrderPOReq = OpenAiOrderPO.builder()
                .openid(openid)
                .orderId(order.getOrderId())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus().getCode())
                .totalAmount(order.getTotalAmount())
                .payStatus(PayStatusVo.WAIT.getCode())
                .payType(order.getPayType().getCode())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productQuota(product.getQuota())
                .productModelTypes(product.getProductModelTypes())
                .build();
        // 3.保存订单
        openAiOrderDao.insert(openAiOrderPOReq);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {

        OpenAiOrderPO openAiOrderPOReq = new OpenAiOrderPO();
        openAiOrderPOReq.setOpenid(payOrderEntity.getOpenid());
        openAiOrderPOReq.setOrderId(payOrderEntity.getOrderId());
        openAiOrderPOReq.setPayUrl(payOrderEntity.getPayUrl());
        openAiOrderPOReq.setPayStatus(payOrderEntity.getPayStatus().getCode());
        // 更新订单状态-创建支付
        openAiOrderDao.updateOrderPayInfo(openAiOrderPOReq);
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime) {
        OpenAiOrderPO req = new OpenAiOrderPO();
        req.setOrderId(orderId);
        req.setTransactionId(transactionId);
        req.setPayAmount(payAmount);
        req.setPayTime(payTime);
        Integer count = openAiOrderDao.changeOrderPaySuccess(req);
        return 1 == count;
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        // 1. 查询订单
        OpenAiOrderPO order = openAiOrderDao.queryOrder(orderId);
        // 2.创建产品对象
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(order.getProductId());
        productEntity.setPrice(order.getTotalAmount());
        // 3.创建订单对象
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.getOrderId());
        orderEntity.setOrderTime(order.getOrderTime());
        orderEntity.setOrderStatus(OrderStatusVO.get(order.getOrderStatus()));
        orderEntity.setTotalAmount(order.getTotalAmount());
        // 4.创建聚合对象
        return CreateOrderAggregate.builder()
                .openid(order.getOpenid())
                .order(orderEntity)
                .product(productEntity)
                .build();

    }

    @Override
    public void deliverGoods(String orderId) {

        // 0.查出订单的信息
        OpenAiOrderPO order = openAiOrderDao.queryOrder(orderId);

        // 1. 变更发货状态-修改为发货成功
        // todo 这里如果出现异常,修改发货状态失败，必须要有补偿
        Integer count = openAiOrderDao.updateOrderStatusDeliverGoods(orderId);
        if (1 != count) {
            throw new RuntimeException("updateOrderStatusDeliverGoods update count is not equals 1");
        }

        // 2.增加用户额度
        // 2.1. 查询数据库中的用户信息
        UserAccountQuotaPo userAccountPo = userAccountDao.query(order.getOpenid());
        // 2.2. 创建用户账户额度对象
        UserAccountQuotaPo req = new UserAccountQuotaPo();
        req.setTotalQuota(order.getProductQuota());
        req.setSurplusQuota(order.getProductQuota());
        req.setOpenid(order.getOpenid());

        // 3. 判断用户是否已开户
        if (null != userAccountPo) {
            // 3.1. 修改用户可用的模型
            String modelTypes = userAccountPo.getModelTypes();
            if (!modelTypes.contains(order.getProductModelTypes())) {
                // 如果不存在,添加新的模型
                if (modelTypes.isEmpty()) {
                    modelTypes = order.getProductModelTypes();
                } else {
                    modelTypes += "," + order.getProductModelTypes();
                }
            }
            req.setModelTypes(modelTypes);

            // 增加用户额度
            Integer addAccountQuotaCount = userAccountDao.addAccountQuota(req);
            if (1 != addAccountQuotaCount) {
                throw new RuntimeException("addAccountQuota update count is not equals 1");
            }

        } else {
            // 创建用户账户
            req.setModelTypes(order.getProductModelTypes());
            userAccountDao.createAccount(req);
        }
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return openAiOrderDao.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return openAiOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeOutCloseOrderList() {
        return openAiOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return openAiOrderDao.changeOrderClose(orderId);
    }

    @Override
    public List<ProductEntity> queryProductList() {
        // 1.查询所有的商品
        List<OpenAiProductPO> productList = openAiProductDao.queryProductList();
        ArrayList<ProductEntity> productEntityList = new ArrayList<>();
        for (OpenAiProductPO openAiProductPO : productList) {
            ProductEntity productEntity = ProductEntity.builder()
                    .productId(openAiProductPO.getProductId())
                    .productName(openAiProductPO.getProductName())
                    .productDesc(openAiProductPO.getProductDesc())
                    .price(openAiProductPO.getPrice())
                    .quota(openAiProductPO.getQuota())
                    .build();

            productEntityList.add(productEntity);
        }
        return productEntityList;
    }

}
