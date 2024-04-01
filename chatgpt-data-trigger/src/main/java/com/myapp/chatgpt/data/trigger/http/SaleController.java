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
     * 创建订单的 http 方法
     *
     * @param token
     * @param productId
     * @return
     */
    @PostMapping("create_pay_order")
    public Response<String> createPayOrder(@RequestHeader("Authorization") String token, @RequestParam Integer productId) {

        try {
            // 1.解析 token
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
            log.info("用户商品下单,根据商品Id创建支付单开始 openid:{},productId:{}", openid, productId);

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
     * 查询商品列表
     *
     * @param token
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
                        .info(Constants.ResponseCode.UN_ERROR.getInfo())
                        .build();
            }
            // 2.查询商品
            List<ProductEntity> productList = orderService.queryProductList();
            log.info("商品查询:{}", JSON.toJSONString(productList));

            ArrayList<SaleProductDTO> mallProductDTOS = new ArrayList<>();
            for (ProductEntity productEntity : productList) {

                SaleProductDTO saleProductDTO = SaleProductDTO.builder()
                        .productId(productEntity.getProductId())
                        .productName(productEntity.getProductName())
                        .productDesc(productEntity.getProductDesc())
                        .productModelTypes(productEntity.getProductModelTypes())
                        .quota(productEntity.getQuota())
                        .price(productEntity.getPrice())
                        .build();

                mallProductDTOS.add(saleProductDTO);
            }

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
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }


    /**
     * 微信支付回调 方法
     *
     * @param requestBody
     * @param request
     * @param response
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
                log.info("支付成功 orderId:{} total:{} successTime:{}", orderId, total, successTime);
                // 更新订单
                boolean success = orderService.changeOrderPaySuccess(orderId, transactionId, new BigDecimal(total).divide(new BigDecimal(100), RoundingMode.HALF_UP), dateFormat.parse(successTime));
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
     * 支付宝支付的回调方法 - 沙箱
     */
    @PostMapping("/alipay/pay_notify")
    public String payNotify(HttpServletRequest request) {
        try {

            log.info("支付回调,消息接收 {}", request.getParameter("trade_status"));

            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {

                // 1.取出请求体中的参数
                HashMap<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }

                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8");

                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    String orderId = params.get("out_trade_no");
                    String tradeNo = params.get("trade_no");
                    String payAmount = params.get("buyer_pay_amount");
                    String payTime = params.get("gmt_payment");

                    boolean success = orderService.changeOrderPaySuccess(orderId, tradeNo, new BigDecimal(payAmount), sdf.parse(payTime));
                    if (success) {
                        // 发布消息
//                        eventBus.post(orderId);
                        payOrderSuccessTopic.publish(orderId);
                    }
                }
            }

            return "success";
        } catch (Exception e) {
            log.error("支付回调,处理失败", e);
            return "false";
        }

    }


}
