/*
 Navicat Premium Data Transfer

 Source Server         : localhost_MySQL
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : openai

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 20/03/2024 23:15:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户Id:这里用的是微信ID作为唯一ID',
  `total_quota` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '总量余额：分配的总使用次数',
  `surplus_quota` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '剩余额度：剩余可使用的次数',
  `model_types` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3',
  `status` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '账户状态:0-可用;1-冻结',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_openId`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO `user_account` VALUES (2, 'Altman', 10, 10, 'glm-3-turbo,glm-4,glm-4v,cogview-3', 0, '2024-03-20 23:06:12', '2024-03-20 23:06:14');

SET FOREIGN_KEY_CHECKS = 1;
