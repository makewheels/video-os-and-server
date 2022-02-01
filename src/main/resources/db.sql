/*
 Navicat Premium Data Transfer

 Source Server         : bj-cynosdbmysql-root
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : bj-cynosdbmysql-grp-br05un86.sql.tencentcdb.com:21875
 Source Schema         : video-os-and-server

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 28/01/2022 11:23:16
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`
(
    `id`                   int(11) NOT NULL AUTO_INCREMENT,
    `create_time`          datetime(0) NULL DEFAULT NULL,
    `type`                 varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `m3u8_file_url`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `play_file_url`        varchar(1023) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `ts_amount`            int(11) NULL DEFAULT NULL,
    `video_file_base_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `video_file_extension` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `video_file_full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `video_id`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `view_count`           int(11) NOT NULL,
    `watch_url`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 188 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for view_log
-- ----------------------------
DROP TABLE IF EXISTS `view_log`;
CREATE TABLE `view_log`
(
    `id`                   int(11) NOT NULL AUTO_INCREMENT,
    `ip`                   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `number_of_this_video` int(11) NOT NULL,
    `user_agent`           varchar(1023) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `video_id`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `view_time`            datetime(0) NULL DEFAULT NULL,
    `ip_json`              varchar(1023) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `ip_province`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `ip_city`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `ip_district`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1664 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
