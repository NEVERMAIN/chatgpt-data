/*
 Navicat Premium Data Transfer

 Source Server         : hw_mysql
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 139.9.212.188:3307
 Source Schema         : openai

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/04/2024 23:02:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for openai_order
-- ----------------------------
DROP TABLE IF EXISTS `openai_order`;
CREATE TABLE `openai_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `openid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID:微信分配的唯一ID\r\n',
  `product_id` int NOT NULL COMMENT '商品ID',
  `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `product_quota` int NOT NULL COMMENT '商品额度',
  `product_model_types` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `order_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态:0-创建完成 1-等待发货 2-发货完成 3-系统关单',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `pay_type` tinyint NOT NULL COMMENT '支付方式:0-微信支付',
  `pay_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付地址:创建支付后，获取 URL 地址',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额:支付成功后,以回调信息更新金额',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易单号:支付成功后，回调信息的交易单号',
  `pay_status` tinyint(1) NULL DEFAULT NULL COMMENT '支付状态：0-等待支付、1-支付成功、2-支付失败、3-放弃支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_openid`(`openid` ASC) USING BTREE,
  INDEX `idx_order_status_pay_status_order_time`(`order_time` ASC, `order_status` ASC, `pay_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of openai_order
-- ----------------------------
INSERT INTO `openai_order` VALUES (1, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '646364947334', '2024-03-25 03:19:49', 2, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=D5zpDuO0IHCHfRXPfvutTCY%2B3%2FdW5hJHSRtXzuKKE56c4byqQa0jSLzDBOWPvyvhLjiFKur1kxoYQmscEVgIdqJYvO6%2BtvVcvACCzFwZqnxwwSnWysDS4EuDUIQWGRwjtLQbSg1QIysrumTD1WNvRmj9WKhyJPcrAXM%2BpaC6FVrLf5868AXcLfC%2BMk%2B9wm%2BaWjb5K1n%2FcYqHkoEpQTNo2kZSzPEqnaSjxLIQYWvTwm2w9OCu1xKpS%2FKBD%2BNPobbSp%2FcyZ5pCTKYE9ECgJcK1%2FPik03YucV0jvqXjOmqnxFRE2NEpBLaNoKJMrDZuHyb2XH9bKTUdUKVwIWJfhKQNUQ%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-03-25+11%3A19%3A49&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;646364947334&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', 1.00, '2024032522001455780503265785', 1, '2024-03-25 03:20:07', '2024-03-25 11:19:49', '2024-03-25 11:20:09');
INSERT INTO `openai_order` VALUES (2, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '027310913099', '2024-03-29 06:30:16', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=ECp19gNUkMXA%2FuIqFqFv0tl3udK26AnVAh58giozlbcY52WeRvka%2BdhRHplL9RkXw%2FIrZOL0qzGa%2FEVv0AjgvS5GACE628UcSVRlhO4c0uXE9dy6YAxfknHwnLWfuF0gKHjB8VQhFJvDmdjJ0t8XN1oGI7KxDH4dMOt0cBKyYMxJ2sHngK5z3GwIyXGgimgoRWKpVc79K4VkPoVlWVhXuvryphL6ohWLXfMjdc5DcI2Zq0l%2B%2B2eDhqC4Jfhv3IYEVz%2BZmyIVtlyklD1ZZq%2FAGVd%2Bz1fvGaVLjbqJepYgBccT5WaUNp%2B20u7JlsfvxviDuww3BGtQC1RngbaM%2Fpvd2A%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-03-29+14%3A30%3A16&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;027310913099&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-03-29 14:30:16', '2024-03-29 14:30:16');
INSERT INTO `openai_order` VALUES (3, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '500902939032', '2024-04-01 03:58:45', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=OmnXh5ZeLO8mWIjgtM4DTb%2B9jCucf%2Fi40BPh5z%2F%2BMkaUuE4g9slONlPogwXnATW94rZxg33wDk8YqpljMLtwPHUtB628hzvlhX8EazA%2FA7ghB%2F59HxaUGtr7jp2nHx3Ox5UAWkONW%2BpPqfaXhsr5xkSFUIy7R8n3JYySJ4L5PC7t4Z2Az7%2B%2F2qgAvLZuh8rS0jizr5FOi6kFVq%2FnExS9ujZcR6pDu6EVxRmZifAV92374zPC%2Bajz834xHOT%2B5n0gunl2BGY4VzWjQ4uAQQR0nienzmvNdiA8b17Nu%2Fou5MXtHp7rrRkoc%2F%2BzC2ssJlcimuAkmcrurnRS6r1gjAjzDQ%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-01+11%3A58%3A45&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;500902939032&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-04-01 11:58:45', '2024-04-01 11:58:45');
INSERT INTO `openai_order` VALUES (4, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '126594748531', '2024-04-01 04:41:28', 2, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=MX9GMYelvSS1LrTus81qaVgJBHCFlfi5TCGQDCHFUX19nSoHUAsc3ZPWjnm%2B2ZIG18myRM2t%2BnctmvePFHatVF0vpHithx6fRxt9w4Wce4dRKTC%2BYqZMd1tWdP%2BpWwIzA5JPvDHDoSux4NnBzf619Ao7QhXicHOhWNj3Aj%2B3TZvT2R9LQ%2FtM%2FdzPQcqhSI6JrOEuFUYCVt6%2FDpE2Yblah7Bf%2F74z9PKhb3a07%2FXeQEHLxunpSnmJ%2BAr1%2F0ympFJrR9YkPBS9%2BV%2Bj9tXvV1QVlZPQCDmCjqUvbz7PHzSZ9sMWK0wkyFKQbwOCqL%2BY5hcRuUBUockzWN3NAYbr%2FOOndA%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-01+12%3A41%3A28&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;126594748531&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', 1.00, '2024040122001455780503357015', 1, '2024-04-01 04:41:48', '2024-04-01 12:41:28', '2024-04-01 12:41:51');
INSERT INTO `openai_order` VALUES (5, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '110459259370', '2024-04-01 07:21:32', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=NsTi23Q17P7CqAmGM58wW6oLPi08CYW91O85mricaBk5Rdvhe3Q%2FXz8%2BwEEh2xo%2Bb%2FS%2BYDtk%2FGMbofmwWzix1TEUrm2bBeZ209JIugR1L46R5gf6f1dUhmfLgqMEEogHtYh%2F3oxUoDDVgPPclP3%2Bkbsm8IKqQudj1rljElU1azEwOrzqWUJy3rzkcAnIQMzMoB8KD5AfPxPrj8PDLeudQNDjkyjSQsV3XBevuRxbiBq6zPReIlkDiD4YCA0hp7fIAT3Wdj1jpnrXUfTL7laFXHWcrKeJTywY2GNGVgitlp2LTWfB2gY4ycsSRBhdRwbO2ez6NszqynsZzNy6%2Bkp1kQ%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-01+15%3A21%3A32&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;110459259370&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-04-01 07:21:32', '2024-04-01 07:21:32');
INSERT INTO `openai_order` VALUES (6, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 1, 'glm-3-turbo', 10, 'glm-3-turbo', '705313078441', '2024-04-01 07:21:58', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=Hft5Fu6Nizoafh12EQ%2FNiILIFHS%2FoHnKghTSR%2Fvuf7nc0LOyHKD95p4NnKYKPzu31EOQGIYVyhfU7ZD7NC9cbNmD4kZmQxc6MumtarafQ%2BdwjlIRgoj3IwROYO45A7AV4SSC98CBTREp%2BMnZNi49S7%2BDB6P5ERwxALhyD54seIw77oTPI5BsY78BgmIzAXYvGbWXvt4p%2BESoaLaJLb0V3R5jYFkb4M2ytlFcMgMSx4btEHVtkyAN3fbPK1eHbJu4M72cvkxVSWcTztvt9LTUPBkbbdZ0xP%2FWdX2qdCJi27SvsOZPHlIm6cYC4L40hgf80gZx0QloI2xof8nbYxZAow%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-01+15%3A21%3A58&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;705313078441&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-3-turbo&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-04-01 07:21:58', '2024-04-01 07:21:58');
INSERT INTO `openai_order` VALUES (7, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 2, 'glm-4', 10, 'glm-4', '788536295079', '2024-04-01 07:22:33', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=ZiU3Ufh3X84DLXMeOivUgfNxZ3MSrewCqjEGwiSFrmYIK7wikq2mC5v4yXWYID9x8FN1Aqc67J%2FzutV57eFSLayaBpBEd6YRFGuVti3UCmFZpkQrsCjRgyj1Z4Bhq2rY3oUmCKz0XycjuOdr%2FGgQnFziGn%2FEaF8%2BrXh1eo4dOht%2Fz6AY83jQ0KohcFQ3jwbuDLmSP7EH65w24wQfbDgJYh0mDi8Hps5mgZxq8yX7xdsRIQRi%2FOM5aTZpQbsM%2F39snO7zTqs6e1H9%2B30hPIT%2BAjTONh%2BkZb3IgH0yW%2BjNy83BYaaNWc5L0szaFg%2FhHanEYBoMNhrwFPykJ3P6G1MI9g%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-01+15%3A22%3A33&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;788536295079&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-4&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-04-01 07:22:33', '2024-04-01 07:22:33');
INSERT INTO `openai_order` VALUES (8, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 2, 'glm-4', 10, 'glm-4', '799679054312', '2024-04-02 14:43:12', 2, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=HGaiFqtKfGJmAJzzO8MS6LXGPgs2XAhhdtncL%2FYf9nAP%2B%2FDmrBZgLAwwuUX2JpoqLLOP4GQQKmBIwNki9boyHrmF9wZyjbGOgXldqjIhCFiLbu4hfVWpNQsvUC0YYACytrIy8gH5xgFpsnn8yjSPAqUSSclHNDxuJCyKVD72x76MVgkLlitnkjBcfCDLJ20hIuPVxo9iCzBwp0lp9w7m7uO3Xs4cinv9eWrqialukOZoahyzRNNsilIHh0inCDf%2FOqlErFgS%2BzDCPq7rarRKbR2gwTXu%2B9DTBgxIlTPuupODmKIS0ME9U8wobkZv544kDlUe90vQWkjT9KyUgOdE%2Bg%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-02+22%3A43%3A12&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;799679054312&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-4&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', 1.00, '2024040222001455780503370482', 1, '2024-04-02 22:43:33', '2024-04-02 14:43:12', '2024-04-02 14:43:35');
INSERT INTO `openai_order` VALUES (9, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 2, 'glm-4', 10, 'glm-4', '063283440937', '2024-04-02 14:51:41', 0, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=kgDreMyxs%2BpwATLiZgii8H4DTQz7nYjynK2zTNUUsI9RRHPBDHWGTOz35OpWam8BcDx921Ps5q8QSY4lIwlVf9h2l0KyUAojK8TWvkcsePPfck%2FJg%2BG7yCmxxgScJcVRWUwTBy6DunLtMhGxHwnjeAM6waDC24FO4pON2iKzPQLLVAu8l40HEUEn5hGF%2B9zAeEGlXmE9D00xPSFgdDzDTE4c2eGbdSrlAS0BoJvF%2FoaUBQix3fItMH%2BZJ%2FlmIn0ODvSNZe4Qgdn1ngp5Q55%2Bs8zZyCRC1XxSjweLFAfmWop7JbLv5%2FFhauUFRTJc3q7ELJ8SbYzmcHic1bGczIPlXg%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Flotterycore.nat300.top%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-02+22%3A51%3A41&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;063283440937&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-4&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', NULL, NULL, 0, NULL, '2024-04-02 14:51:41', '2024-04-02 14:51:41');
INSERT INTO `openai_order` VALUES (10, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 2, 'glm-4', 10, 'glm-4', '847370302074', '2024-04-02 14:59:19', 2, 1.00, 1, '<form name=\"punchout_form\" method=\"post\" action=\"https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=SXCITu30eanrtJqkNYgRrTdh7E2Y7TDhJ%2F%2BHh4rTKrJ5%2BQzB2iQxdI3TdmgOFgV3i%2FdU0FOBC1RnT%2FizNDsoPVtIiqO4G65lLYWbyefR%2BoiQT6EdVjP6Bg3s9AvhgKnT0FjDGUwX9%2BQrd9GS609Iwv0DCO6jsdgr3V5M0qD9TyLlLntMivYCOjAyzCfTerUUh%2FS9%2FcmOh78nK%2BPHc3mhFVA5A0ZkfAtwwLv3xBYuSrv6DVpU6u7sOaliALzi4xJA2lP1O7MYt72c5k8LuKPfE%2FYfChDNcSZxAnfDKp2zepxNxBV0MfBTMFFYGPU5QpUZf%2FHYgHNsk%2BsJ6ehszfmS0w%3D%3D&return_url=https%3A%2F%2Fgaga.plus%2F&notify_url=http%3A%2F%2Fwww.opendicu.top%3A80%2Fapi%2Fv1%2Fsale%2Falipay%2Fpay_notify&version=1.0&app_id=9021000135657223&sign_type=RSA2&timestamp=2024-04-02+22%3A59%3A19&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json\">\n<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;847370302074&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;glm-4&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}\">\n<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n</form>\n<script>document.forms[0].submit();</script>', 1.00, '2024040222001455780503379383', 1, '2024-04-02 22:59:39', '2024-04-02 14:59:18', '2024-04-02 14:59:43');

SET FOREIGN_KEY_CHECKS = 1;
