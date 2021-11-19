/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.26 : Database - video-os-and-server
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = ''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS */`video-os-and-server` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `video-os-and-server`;

/*Table structure for table `video` */

DROP TABLE IF EXISTS `video`;

CREATE TABLE `video`
(
    `id`                   int NOT NULL AUTO_INCREMENT,
    `create_time`          datetime                                                 DEFAULT NULL,
    `type`                 varchar(255)                                             DEFAULT NULL,
    `m3u8_file_url`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL,
    `play_file_url`        varchar(1023) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `ts_amount`            int                                                      DEFAULT NULL,
    `video_file_base_name` varchar(255)                                             DEFAULT NULL,
    `video_file_extension` varchar(255)                                             DEFAULT NULL,
    `video_file_full_name` varchar(255)                                             DEFAULT NULL,
    `video_id`             varchar(255)                                             DEFAULT NULL,
    `view_count`           int NOT NULL,
    `watch_url`            varchar(255)                                             DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8mb3;

/*Data for the table `video` */

/*Table structure for table `view_log` */

DROP TABLE IF EXISTS `view_log`;

CREATE TABLE `view_log`
(
    `id`                   int NOT NULL AUTO_INCREMENT,
    `ip`                   varchar(255)  DEFAULT NULL,
    `number_of_this_video` int NOT NULL,
    `user_agent`           varchar(1000) DEFAULT NULL,
    `video_id`             varchar(255)  DEFAULT NULL,
    `view_time`            datetime      DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;

/*Data for the table `view_log` */

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
