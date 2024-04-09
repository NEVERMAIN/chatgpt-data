package com.myapp.chatgpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.eventbus.EventBus;
import com.myapp.chatgpt.data.domain.atuth.service.IAuthService;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ProductEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ShopCarEntity;
import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import com.myapp.chatgpt.data.trigger.http.dto.SaleProductDTO;
import com.myapp.chatgpt.data.trigger.mq.RedisTopicListener;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.model.Response;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 销售服务
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@RestController
@Slf4j
@RequestMapping("/api/${app.config.api-version}/sale/")
@CrossOrigin("*")
public class SaleController {

    @Autowired(required = false)
    private NotificationParser notificationParser;

    @Resource
    private IOrderService orderService;

    @Resource
    private IAuthService authService;

    @Resource
    private EventBus eventBus;

    @Resource
    private RTopic payOrderSuccessTopic;


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建订单
     *
     * @param token     鉴权 token
     * @param productId 商品ID
     * @return
     */
    @PostMapping("create_pay_order")
    public Response<String> createPayOrder(@RequestHeader("Authorization") String token, @RequestParam Integer productId) {

        try {
            // 1.解析 token,鉴权
            boolean success = authService.checkToken(token);
            if (!success) {
                return Response.<String>builder()
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .build();
            }

            // 2. 获取 openid
            String openid = authService.getOpenId(token);
            assert null != openid;
            log.info("用户商品下单,根据商品Id 创建支付单开始 openid:{},productId:{}", openid, productId);

            // 3.创建购物车
            ShopCarEntity shopCarEntity = ShopCarEntity.builder()
                    .openid(openid)
                    .productId(productId)
                    .build();

            // 4.创建订单
            PayOrderEntity payOrderEntity = orderService.createOrder(shopCarEntity);
            log.info("用户商品下单,根据商品ID创建支付单完成 openid:{},productId:{},orderPay:{}", openid, productId, payOrderEntity.toString());

            // 5.返回对象
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCEESS.getCode())
                    .info(Constants.ResponseCode.SUCEESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();

        } catch (Exception e) {
            log.error("用户商品下单，根据商品ID创建支付单失败", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }


    /**
     * 微信支付回调 方法
     *
     * @param requestBody 请求体
     * @param request     请求对象
     * @param response    返回对象
     */
    @PostMapping("pay_notify")
    public void payNotify(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            // 随机串
            String nonceStr = request.getHeader("Wechatpay-Nonce");
            // 微信传递过来的签名
            String signature = request.getHeader("Wechatpay-Signature");
            // 微信支付平台证书的序列号，验签必须使用序列号对应的微信支付平台证书。
            String serialNo = request.getHeader("Wechatpay-Serial");
            // 时间戳
            String timestamp = request.getHeader("Wechatpay-Timestamp");

            // 1. 构造 RequestParam
            com.wechat.pay.java.core.notification.RequestParam requestParam = new com.wechat.pay.java.core.notification.RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonceStr)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 以支付通知回调为例,验签,解密,并转成 Transaction
            Transaction transaction = notificationParser.parse(requestParam, Transaction.class);
            // 1.获取支付的状态
            Transaction.TradeStateEnum tradeState = transaction.getTradeState();
            if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
                // 订单单号
                String orderId = transaction.getOutTradeNo();
                // 交易单号
                String transactionId = transaction.getTransactionId();
                Integer total = transaction.getAmount().getTotal();
                String successTime = transaction.getSuccessTime();
                Date payTime = null;
                if (StringUtils.isBlank(successTime)) {
                    payTime = new Date();
                } else {
                    payTime = dateFormat.parse(successTime);
                }

                log.info("支付成功 orderId:{} total:{} payTime:{}", orderId, total, payTime);
                // 更新订单
                boolean success = orderService.changeOrderPaySuccess(orderId, transactionId, new BigDecimal(total).divide(new BigDecimal(100), RoundingMode.HALF_UP), payTime);
                if (success) {
                    // 发布消息
                    eventBus.post(orderId);
                }
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            } else {
                response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
            }
        } catch (Exception e) {
            log.error("支付失败", e);
            response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");

        }

    }


    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    /**
     * 处理支付宝支付的回调通知（沙箱环境）。
     * <p>
     * 该方法用于接收支付宝支付完成后的通知回调，对支付结果进行验证，并根据验证结果更新订单状态。
     * <p>
     *
     * @param request 请求对象，包含支付宝回调的通知信息。
     * @return 返回字符串"success"表示处理成功，其他字符串表示处理失败。
     */
    @PostMapping("/alipay/pay_notify")
    public String payNotify(HttpServletRequest request) {
        try {
            // 记录接收到的支付状态
            log.info("支付回调,消息接收 {}", request.getParameter("trade_status"));

            // 验证支付状态是否为成功
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {

                // 1. 提取请求体中的参数
                HashMap<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }

                // 2. 验签
                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8");

                // 验签逻辑
                if (checkSignature) {
                    // 验签通过，处理支付结果
                    String orderId = params.get("out_trade_no");
                    String tradeNo = params.get("trade_no");
                    String payAmount = params.get("buyer_pay_amount");
                    String successTime = params.get("gmt_payment");

                    // 解析支付时间
                    Date payTime = null;
                    if (StringUtils.isBlank(successTime)) {
                        payTime = new Date();
                    } else {
                        payTime = sdf.parse(successTime);
                    }

                    // 记录支付成功信息
                    log.info("支付成功,orderId:{},payTime:{}", orderId, payTime);

                    // 更新订单状态为支付成功，并发布消息
                    boolean success = orderService.changeOrderPaySuccess(orderId, tradeNo, new BigDecimal(payAmount), payTime);
                    if (success) {
                        // 发布订单支付成功的消息
                        payOrderSuccessTopic.publish(orderId);
                    }
                }
            }

            // 回复成功处理
            return "success";
        } catch (Exception e) {
            // 记录处理失败的日志
            log.error("支付回调,处理失败", e);
            // 返回失败标识
            return "false";
        }

    }

    /**
     * 查询商品列表
     *
     * @param token 鉴权 token
     * @return
     */
    @GetMapping("query_product_list")
    public Response<List<SaleProductDTO>> queryProductList(@RequestHeader("Authorization") String token) {
        try {
            // 1.Token 校验
            boolean success = authService.checkToken(token);
            if (!success) {
                return Response.<List<SaleProductDTO>>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .build();
            }
            // 2.查询商品
            List<ProductEntity> productList = orderService.queryProductList();
            if (productList == null) {
                // 避免 null
                productList = new ArrayList<>();
            }
            log.info("商品查询:{}", JSON.toJSONString(productList));

            List<SaleProductDTO> mallProductDTOS = convertProductEntitiesToDTOs(productList);

            // 3.返回结果
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.SUCEESS.getCode())
                    .info(Constants.ResponseCode.SUCEESS.getInfo())
                    .data(mallProductDTOS)
                    .build();


        } catch (Exception e) {
            log.error("商品查询失败", e);
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getCode())
                    .build();
        }

    }


    /**
     * 将产品实体列表转换为DTO（数据传输对象）列表。
     *
     * @param productList 产品实体列表，不应为null。
     * @return 转换后的SaleProductDTO列表，包含从产品实体中提取的所有信息。
     */
    private List<SaleProductDTO> convertProductEntitiesToDTOs(List<ProductEntity> productList) {
        // 使用流将每个产品实体映射到SaleProductDTO，然后收集到一个列表中
        return productList.stream()
                .map(productEntity -> SaleProductDTO.builder()
                        .productId(productEntity.getProductId())
                        .productName(productEntity.getProductName())
                        .productDesc(productEntity.getProductDesc())
                        .productModelTypes(productEntity.getProductModelTypes())
                        .quota(productEntity.getQuota())
                        .price(productEntity.getPrice())
                        .build())
                .collect(Collectors.toList());
    }


}
