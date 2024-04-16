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

 Date: 11/04/2024 23:02:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for openai_product
-- ----------------------------
DROP TABLE IF EXISTS `openai_product`;
CREATE TABLE `openai_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品描述',
  `product_model_types` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3',
  `quota` int NOT NULL COMMENT '额度次数',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `sort` int NULL DEFAULT 1 COMMENT '商品排序',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效：0-无效 1-有效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `product_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品描述',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id_enabled`(`product_id` ASC, `is_enabled` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of openai_product
-- ----------------------------
INSERT INTO `openai_product` VALUES (1, 1, 'glm-3-turbo', 'glm-3-turbo', 10, 1.00, 1, 1, '2024-03-24 15:50:38', '2024-03-24 15:50:41', '智谱AI大模型');
INSERT INTO `openai_product` VALUES (2, 2, 'glm-4', 'glm-4', 10, 1.00, 1, 1, '2024-03-29 14:17:46', '2024-03-29 14:17:46', '智谱AI大模型');
INSERT INTO `openai_product` VALUES (3, 3, 'general', 'general', 10, 1.00, 1, 1, '2024-03-29 14:25:48', '2024-03-29 14:26:52', '星火AI大模型');
INSERT INTO `openai_product` VALUES (4, 4, 'glm-4v', 'glm-4v', 10, 1.00, 1, 1, '2024-03-29 14:26:30', '2024-03-29 14:26:30', '智谱AI大模型');

SET FOREIGN_KEY_CHECKS = 1;
