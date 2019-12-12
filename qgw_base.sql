
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for envent_type
-- ----------------------------
DROP TABLE IF EXISTS `envent_type`;
CREATE TABLE `envent_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `event_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '事件类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for event_results
-- ----------------------------
DROP TABLE IF EXISTS `event_results`;
CREATE TABLE `event_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `strength` float NULL DEFAULT NULL COMMENT '强度',
  `event_id` int(11) NULL DEFAULT NULL,
  `event_number` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '舞动及雨事件数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ice_warn_value
-- ----------------------------
DROP TABLE IF EXISTS `ice_warn_value`;
CREATE TABLE `ice_warn_value`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `line_span_id` int(11) NULL DEFAULT NULL COMMENT '档距id',
  `line_span_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档距名称',
  `line_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '线路名称',
  `unit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `ice_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '覆冰阈值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预警阈值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for line_span_infos
-- ----------------------------
DROP TABLE IF EXISTS `line_span_infos`;
CREATE TABLE `line_span_infos`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `line_span_index` int(11) NOT NULL COMMENT '档距名称(编号)',
  `start_tower_id` int(11) NOT NULL COMMENT '起始杆塔ID',
  `end_tower_id` int(11) NOT NULL COMMENT '结束杆塔ID',
  `design_wind_speed` float NOT NULL COMMENT '设计风速',
  `design_ice_thickness` float NOT NULL COMMENT '设计冰厚',
  `cw_design_ice_thickness` float NOT NULL COMMENT '导线设计冰厚',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '档距基本信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for phi_line_span_results
-- ----------------------------
DROP TABLE IF EXISTS `phi_line_span_results`;
CREATE TABLE `phi_line_span_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `m_ice_thickness` float NULL DEFAULT NULL COMMENT 'OPGW覆冰厚度',
  `cw_ice_thickness` float NULL DEFAULT NULL COMMENT '导线覆冰厚度',
  PRIMARY KEY (`id`, `calc_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '档距覆冰数据计算结果' ROW_FORMAT = Dynamic PARTITION BY RANGE COLUMNS (`calc_time`)
 PARTITIONS 29
(PARTITION iCalcTime_201503 VALUES LESS THAN (1427817600) ENGINE = InnoDB,
 PARTITION iCalcTime_201506 VALUES LESS THAN (1435680000) ENGINE = InnoDB,
 PARTITION iCalcTime_201509 VALUES LESS THAN (1443628800) ENGINE = InnoDB,
 PARTITION iCalcTime_201512 VALUES LESS THAN (1451577600) ENGINE = InnoDB,
 PARTITION iCalcTime_201603 VALUES LESS THAN (1459440000) ENGINE = InnoDB,
 PARTITION iCalcTime_201606 VALUES LESS THAN (1467302400) ENGINE = InnoDB,
 PARTITION iCalcTime_201609 VALUES LESS THAN (1475251200) ENGINE = InnoDB,
 PARTITION iCalcTime_201612 VALUES LESS THAN (1483200000) ENGINE = InnoDB,
 PARTITION iCalcTime_201703 VALUES LESS THAN (1490976000) ENGINE = InnoDB,
 PARTITION iCalcTime_201706 VALUES LESS THAN (1498838400) ENGINE = InnoDB,
 PARTITION iCalcTime_201709 VALUES LESS THAN (1506787200) ENGINE = InnoDB,
 PARTITION iCalcTime_201712 VALUES LESS THAN (1514736000) ENGINE = InnoDB,
 PARTITION iCalcTime_201803 VALUES LESS THAN (1522512000) ENGINE = InnoDB,
 PARTITION iCalcTime_201806 VALUES LESS THAN (1530374400) ENGINE = InnoDB,
 PARTITION iCalcTime_201809 VALUES LESS THAN (1538323200) ENGINE = InnoDB,
 PARTITION iCalcTime_201812 VALUES LESS THAN (1546272000) ENGINE = InnoDB,
 PARTITION iCalcTime_201903 VALUES LESS THAN (1554048000) ENGINE = InnoDB,
 PARTITION iCalcTime_201906 VALUES LESS THAN (1561910400) ENGINE = InnoDB,
 PARTITION iCalcTime_201909 VALUES LESS THAN (1569859200) ENGINE = InnoDB,
 PARTITION iCalcTime_201912 VALUES LESS THAN (1577808000) ENGINE = InnoDB,
 PARTITION iCalcTime_202003 VALUES LESS THAN (1585670400) ENGINE = InnoDB,
 PARTITION iCalcTime_202006 VALUES LESS THAN (1593532800) ENGINE = InnoDB,
 PARTITION iCalcTime_202009 VALUES LESS THAN (1601481600) ENGINE = InnoDB,
 PARTITION iCalcTime_202012 VALUES LESS THAN (1609430400) ENGINE = InnoDB,
 PARTITION iCalcTime_202103 VALUES LESS THAN (1617206400) ENGINE = InnoDB,
 PARTITION iCalcTime_202106 VALUES LESS THAN (1625068800) ENGINE = InnoDB,
 PARTITION iCalcTime_202109 VALUES LESS THAN (1633017600) ENGINE = InnoDB,
 PARTITION iCalcTime_202112 VALUES LESS THAN (1640966400) ENGINE = InnoDB,
 PARTITION iCalcTime_max VALUES LESS THAN (MAXVALUE) ENGINE = InnoDB);



-- ----------------------------
-- Table structure for rain_results
-- ----------------------------
DROP TABLE IF EXISTS `rain_results`;
CREATE TABLE `rain_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `event_id` int(11) NULL DEFAULT NULL COMMENT '事件类型',
  `strength` float NULL DEFAULT NULL COMMENT '降雨强度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '降雨数据计算结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temperature_results
-- ----------------------------
DROP TABLE IF EXISTS `temperature_results`;
CREATE TABLE `temperature_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `temperature` float NULL DEFAULT NULL COMMENT '温度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '档距温度数据计算结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tower_infos
-- ----------------------------
DROP TABLE IF EXISTS `tower_infos`;
CREATE TABLE `tower_infos`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tower_id` int(11) NOT NULL COMMENT '杆塔ID',
  `tower_index` int(11) NOT NULL COMMENT '杆塔名称(编号)',
  `location_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '塔位编号',
  `location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '塔位点',
  `tower_model` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '杆塔型号',
  `tower_type` int(11) NOT NULL COMMENT '杆塔类型',
  `tower_elvation` float NOT NULL COMMENT '塔位桩顶高程(地形高度)',
  `gps_longitude` float(11, 7) NULL DEFAULT NULL COMMENT '经度',
  `gps_latitude` float(11, 7) NULL DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '杆塔基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transient_event
-- ----------------------------
DROP TABLE IF EXISTS `transient_event`;
CREATE TABLE `transient_event`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `event_id` int(11) NULL DEFAULT NULL,
  `position` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '瞬态数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wind_results
-- ----------------------------
DROP TABLE IF EXISTS `wind_results`;
CREATE TABLE `wind_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `avg_speed` float NULL DEFAULT NULL COMMENT '平均风速',
  `max_speed` float NULL DEFAULT NULL COMMENT '最大风速',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '档距风速数据计算结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for windshake_results
-- ----------------------------
DROP TABLE IF EXISTS `windshake_results`;
CREATE TABLE `windshake_results`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `line_span_id` int(11) NOT NULL COMMENT '档距ID',
  `calc_time` int(11) NOT NULL COMMENT '数据计算时间',
  `event_id` int(11) NULL DEFAULT NULL COMMENT '事件类型',
  `strength` float NULL DEFAULT NULL COMMENT '振动强度',
  `frequency` float NULL DEFAULT NULL COMMENT '振动评率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微风振动数据计算结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for span_view
-- ----------------------------
DROP VIEW IF EXISTS `span_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `span_view` AS select `line_span_infos`.`line_span_id` AS `line_span_id`,`line_span_infos`.`design_wind_speed` AS `design_wind_speed`,`line_span_infos`.`design_ice_thickness` AS `design_ice_thickness`,`line_span_infos`.`cw_design_ice_thickness` AS `cw_design_ice_thickness`,concat(concat((select `tower_infos`.`location_number` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`start_tower_id`)),'-'),(select `tower_infos`.`location_number` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`end_tower_id`))) AS `spannum`,(select `tower_infos`.`gps_latitude` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`start_tower_id`)) AS `s_gps_lat`,(select `tower_infos`.`gps_longitude` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`start_tower_id`)) AS `s_gps_lon`,(select `tower_infos`.`gps_latitude` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`end_tower_id`)) AS `e_gps_lat`,(select `tower_infos`.`gps_longitude` from `tower_infos` where (`tower_infos`.`tower_id` = `line_span_infos`.`end_tower_id`)) AS `e_gps_lon` from `line_span_infos` where ((`line_span_infos`.`line_span_index` >= 0) and (`line_span_infos`.`line_span_index` <> 99999)) order by `line_span_infos`.`line_span_index`;

SET FOREIGN_KEY_CHECKS = 1;
