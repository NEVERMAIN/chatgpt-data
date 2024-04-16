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

 Date: 11/04/2024 23:02:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_account
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户Id:这里用的是微信ID作为唯一ID',
  `total_quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '总量余额：分配的总使用次数',
  `surplus_quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '剩余额度：剩余可使用的次数',
  `model_types` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3',
  `status` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '账户状态:0-可用;1-冻结',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_openId`(`openid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO `user_account` VALUES (2, 'o6OsG6Z10yvXD2y2PIsRTgOhBbkg', 60, 35, 'glm-3-turbo,glm-4,glm-4v,cogview-3,generalv3.5', 0, '2024-03-20 23:06:12', '2024-04-09 08:19:38');
INSERT INTO `user_account` VALUES (3, 'altman', 0, 0, '', 0, '2024-03-21 06:43:37', '2024-03-21 06:43:37');
INSERT INTO `user_account` VALUES (5, 'o6OsG6fPEeZFba6gqH9xsR9jrgnk', 10, 8, 'glm-3-turbo', 0, '2024-03-21 08:24:23', '2024-04-03 10:41:39');
INSERT INTO `user_account` VALUES (6, 'o6OsG6QnH3mH9HySmf1XMPmxo3nQ', 10, 10, 'glm-3-turbo,glm-4,glm-4v,cogview-3', 0, '2024-04-03 08:37:59', '2024-04-03 08:37:59');

SET FOREIGN_KEY_CHECKS = 1;
