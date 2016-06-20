CREATE DATABASE  IF NOT EXISTS `zhwg_space` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `zhwg_space`;
-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: jeremyliu.cn    Database: zhwg_space
-- ------------------------------------------------------
-- Server version	5.7.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `zhwg_ user_privilege`
--

DROP TABLE IF EXISTS `zhwg_ user_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_ user_privilege` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限配置id',
  `role_id` bigint(20) unsigned NOT NULL COMMENT '对应roles表中的id',
  `resource_id` bigint(20) unsigned NOT NULL COMMENT '对应resource表中的资源id',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '控制类型, 暂定0为read, 1为write',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  KEY `resource_id` (`resource_id`),
  CONSTRAINT `resource_index` FOREIGN KEY (`resource_id`) REFERENCES `zhwg_user_resource` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `role_index` FOREIGN KEY (`role_id`) REFERENCES `zhwg_user_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限控制表，每一个角色对资源的权限配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_ user_privilege`
--

LOCK TABLES `zhwg_ user_privilege` WRITE;
/*!40000 ALTER TABLE `zhwg_ user_privilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_ user_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_business`
--

DROP TABLE IF EXISTS `zhwg_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_business` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '业务名称',
  `img` varchar(255) NOT NULL DEFAULT '' COMMENT '显示图标',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务类型定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_business`
--

LOCK TABLES `zhwg_business` WRITE;
/*!40000 ALTER TABLE `zhwg_business` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_business_record`
--

DROP TABLE IF EXISTS `zhwg_business_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_business_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` bigint(20) unsigned NOT NULL COMMENT '对应business表的id',
  `calling_number` varchar(255) NOT NULL DEFAULT '' COMMENT '主叫号码',
  `calling_ip` varchar(255) NOT NULL DEFAULT '' COMMENT '主叫ip',
  `called_number` varchar(255) NOT NULL COMMENT '被叫号码',
  `called_ip` varchar(255) NOT NULL DEFAULT '' COMMENT '被叫ip',
  `period_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '经历时长',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `del_flag` int(11) NOT NULL DEFAULT '0' COMMENT '是否被删除，0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `business_id` (`business_id`),
  CONSTRAINT `business_fr_key` FOREIGN KEY (`business_id`) REFERENCES `zhwg_business` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务监控记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_business_record`
--

LOCK TABLES `zhwg_business_record` WRITE;
/*!40000 ALTER TABLE `zhwg_business_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_business_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_card_type`
--

DROP TABLE IF EXISTS `zhwg_card_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_card_type` (
  `code` int(11) NOT NULL,
  `name` varchar(45) CHARACTER SET utf8 NOT NULL,
  `port_count` int(11) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_card_type`
--

LOCK TABLES `zhwg_card_type` WRITE;
/*!40000 ALTER TABLE `zhwg_card_type` DISABLE KEYS */;
INSERT INTO `zhwg_card_type` VALUES (0,'空槽位',0),(1,'主控板',1),(2,'数字用户板',16),(3,'模拟用户板',16),(5,'以太网板',10),(6,'数字中继板',8),(8,'录音板',1),(10,'光传输板',2),(12,'综合接口板',6);
/*!40000 ALTER TABLE `zhwg_card_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_device_card`
--

DROP TABLE IF EXISTS `zhwg_device_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_device_card` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `element_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `slot_number` varchar(45) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '板卡类型',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `device_id` (`element_id`),
  CONSTRAINT `card_device_fr_key` FOREIGN KEY (`element_id`) REFERENCES `zhwg_monitor_element` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_device_card`
--

LOCK TABLES `zhwg_device_card` WRITE;
/*!40000 ALTER TABLE `zhwg_device_card` DISABLE KEYS */;
INSERT INTO `zhwg_device_card` VALUES (1,1,'0',3,'2016-05-12 20:05:45'),(2,1,'1',0,'2016-05-11 20:16:58'),(3,1,'2',0,'2016-05-11 20:16:58'),(4,1,'3',0,'2016-05-11 20:16:58'),(5,1,'4',0,'2016-05-11 20:16:58'),(6,1,'5',0,'2016-05-11 20:16:58'),(7,1,'6',0,'2016-05-11 20:16:58'),(8,1,'7',0,'2016-05-11 20:16:58'),(9,1,'8',0,'2016-05-11 20:16:58'),(10,1,'9',0,'2016-05-11 20:16:58'),(11,1,'10',0,'2016-05-11 20:16:58'),(12,1,'11',0,'2016-05-11 20:16:58'),(13,1,'12',0,'2016-05-11 20:16:58'),(14,1,'13',0,'2016-05-11 20:16:58');
/*!40000 ALTER TABLE `zhwg_device_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_log_error`
--

DROP TABLE IF EXISTS `zhwg_log_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_log_error` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(255) NOT NULL DEFAULT '' COMMENT '调用的api',
  `content` text COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_log_error`
--

LOCK TABLES `zhwg_log_error` WRITE;
/*!40000 ALTER TABLE `zhwg_log_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_log_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_log_operate`
--

DROP TABLE IF EXISTS `zhwg_log_operate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_log_operate` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `resource_id` bigint(20) unsigned NOT NULL COMMENT '对应user_resource表',
  `action` int(11) NOT NULL COMMENT '操作类型，0为新建，1为修改，2为删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作者，用户id',
  `udp_message` blob COMMENT '监听到的udp报文',
  `return_message` varchar(2048) DEFAULT '' COMMENT '返回给前台的信息，或者产生的异常信息',
  PRIMARY KEY (`id`),
  KEY `resource_id` (`resource_id`),
  KEY `operator` (`operator`),
  CONSTRAINT `log_resource_fr_key` FOREIGN KEY (`resource_id`) REFERENCES `zhwg_user_resource` (`id`),
  CONSTRAINT `log_user_fr_key` FOREIGN KEY (`operator`) REFERENCES `zhwg_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志，这里只记录正常的操作日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_log_operate`
--

LOCK TABLES `zhwg_log_operate` WRITE;
/*!40000 ALTER TABLE `zhwg_log_operate` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_log_operate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_monitor_device`
--

DROP TABLE IF EXISTS `zhwg_monitor_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_monitor_device` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '设备名称',
  `model` varchar(255) NOT NULL DEFAULT '' COMMENT '设备型号',
  `param_config` varchar(255) NOT NULL DEFAULT '' COMMENT '设备的一些配置信息存储的文件路径, 包括协议的配置信息也可以放进来，格式为xml',
  `command_config` varchar(255) NOT NULL DEFAULT '' COMMENT '发送命令的配置文件路径，该文件一般设备定了后不会作更改',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='监控网元的设备，目前应该只有一种设备，方';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_monitor_device`
--

LOCK TABLES `zhwg_monitor_device` WRITE;
/*!40000 ALTER TABLE `zhwg_monitor_device` DISABLE KEYS */;
INSERT INTO `zhwg_monitor_device` VALUES (1,'P823','p823','1','1','2016-05-11 12:03:54');
/*!40000 ALTER TABLE `zhwg_monitor_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_monitor_element`
--

DROP TABLE IF EXISTS `zhwg_monitor_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_monitor_element` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `device_id` bigint(20) unsigned NOT NULL COMMENT '对应monitor_device的id',
  `ip` varchar(15) NOT NULL DEFAULT '',
  `port` int(10) unsigned NOT NULL,
  `card_count` int(11) NOT NULL DEFAULT '14',
  `create_by` bigint(20) unsigned NOT NULL COMMENT '创建者id',
  `cardCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `device_id` (`device_id`),
  KEY `create_by` (`create_by`),
  CONSTRAINT `device_fr_key` FOREIGN KEY (`device_id`) REFERENCES `zhwg_monitor_device` (`id`),
  CONSTRAINT `user_fr_key` FOREIGN KEY (`create_by`) REFERENCES `zhwg_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='网元实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_monitor_element`
--

LOCK TABLES `zhwg_monitor_element` WRITE;
/*!40000 ALTER TABLE `zhwg_monitor_element` DISABLE KEYS */;
INSERT INTO `zhwg_monitor_element` VALUES (1,'网元1',1,'192.0.0.1',3600,14,1,14);
/*!40000 ALTER TABLE `zhwg_monitor_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_record`
--

DROP TABLE IF EXISTS `zhwg_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `calling_number` varchar(255) NOT NULL DEFAULT '' COMMENT '主叫号码',
  `called_number` varchar(255) NOT NULL DEFAULT '' COMMENT '被叫号码',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `period_time` bigint(20) unsigned NOT NULL COMMENT '录音时长',
  `file` varchar(255) DEFAULT NULL COMMENT '录音文件路径, 暂定为一个文件夹，每次录音都存放在同一个文件夹，文件夹里面包含录音元数据和多个录音数据文件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_record`
--

LOCK TABLES `zhwg_record` WRITE;
/*!40000 ALTER TABLE `zhwg_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_terminal_device`
--

DROP TABLE IF EXISTS `zhwg_terminal_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_terminal_device` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `card_id` int(10) unsigned NOT NULL,
  `card_port` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_terminal_device`
--

LOCK TABLES `zhwg_terminal_device` WRITE;
/*!40000 ALTER TABLE `zhwg_terminal_device` DISABLE KEYS */;
INSERT INTO `zhwg_terminal_device` VALUES (2,'test1',1,2),(3,'test2',1,3);
/*!40000 ALTER TABLE `zhwg_terminal_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_user`
--

DROP TABLE IF EXISTS `zhwg_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_user` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint(20) unsigned NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_login_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_user`
--

LOCK TABLES `zhwg_user` WRITE;
/*!40000 ALTER TABLE `zhwg_user` DISABLE KEYS */;
INSERT INTO `zhwg_user` VALUES (1,'admin','123',1,'2016-05-08 15:08:12','2016-05-08 15:08:12');
/*!40000 ALTER TABLE `zhwg_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_user_resource`
--

DROP TABLE IF EXISTS `zhwg_user_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_user_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `name` varchar(255) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限控制的资源表，每个角色可以对多个资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_user_resource`
--

LOCK TABLES `zhwg_user_resource` WRITE;
/*!40000 ALTER TABLE `zhwg_user_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_user_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zhwg_user_role`
--

DROP TABLE IF EXISTS `zhwg_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zhwg_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(2048) NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zhwg_user_role`
--

LOCK TABLES `zhwg_user_role` WRITE;
/*!40000 ALTER TABLE `zhwg_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `zhwg_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `zhwg_view_card`
--

DROP TABLE IF EXISTS `zhwg_view_card`;
/*!50001 DROP VIEW IF EXISTS `zhwg_view_card`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `zhwg_view_card` AS SELECT 
 1 AS `id`,
 1 AS `element_id`,
 1 AS `slot_number`,
 1 AS `code`,
 1 AS `name`,
 1 AS `port_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `zhwg_view_terminal_device`
--

DROP TABLE IF EXISTS `zhwg_view_terminal_device`;
/*!50001 DROP VIEW IF EXISTS `zhwg_view_terminal_device`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `zhwg_view_terminal_device` AS SELECT 
 1 AS `id`,
 1 AS `name`,
 1 AS `card_port`,
 1 AS `slot_number`,
 1 AS `element_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `zhwg_view_card`
--

/*!50001 DROP VIEW IF EXISTS `zhwg_view_card`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`zhwg`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `zhwg_view_card` AS select `zhwg_device_card`.`id` AS `id`,`zhwg_device_card`.`element_id` AS `element_id`,`zhwg_device_card`.`slot_number` AS `slot_number`,`zhwg_card_type`.`code` AS `code`,`zhwg_card_type`.`name` AS `name`,`zhwg_card_type`.`port_count` AS `port_count` from (`zhwg_device_card` left join `zhwg_card_type` on((`zhwg_device_card`.`type` = `zhwg_card_type`.`code`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `zhwg_view_terminal_device`
--

/*!50001 DROP VIEW IF EXISTS `zhwg_view_terminal_device`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`zhwg`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `zhwg_view_terminal_device` AS select `zhwg_terminal_device`.`id` AS `id`,`zhwg_terminal_device`.`name` AS `name`,`zhwg_terminal_device`.`card_port` AS `card_port`,`zhwg_device_card`.`slot_number` AS `slot_number`,`zhwg_device_card`.`element_id` AS `element_id` from (`zhwg_terminal_device` left join `zhwg_device_card` on((`zhwg_terminal_device`.`card_id` = `zhwg_device_card`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-15 23:03:16
