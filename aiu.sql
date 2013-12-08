/*
MySQL Data Transfer
Source Host: localhost
Source Database: aiu
Target Host: localhost
Target Database: aiu
Date: 2013/12/8 23:10:28
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for adlist
-- ----------------------------
CREATE TABLE `adlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mtype` int(11) NOT NULL,
  `ad_id` bigint(20) DEFAULT NULL,
  `ct_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAB39F341FE6AC987` (`ct_id`),
  CONSTRAINT `FKAB39F341FE6AC987` FOREIGN KEY (`ct_id`) REFERENCES `carouseltype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for appadmin
-- ----------------------------
CREATE TABLE `appadmin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `psd` varchar(255) DEFAULT NULL,
  `role` tinyint(4) DEFAULT NULL,
  `admingroup` int(11) DEFAULT NULL,
  `game_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45B0B5CE4429C99E` (`game_id`),
  CONSTRAINT `FK45B0B5CE4429C99E` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for appsession
-- ----------------------------
CREATE TABLE `appsession` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `sessionID` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`),
  UNIQUE KEY `customer_id_2` (`customer_id`),
  KEY `FK5698915555FD001E` (`customer_id`),
  KEY `idx_sessionID` (`sessionID`),
  CONSTRAINT `FK5698915555FD001E` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for carouseltype
-- ----------------------------
CREATE TABLE `carouseltype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for clientversion
-- ----------------------------
CREATE TABLE `clientversion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `mobiletype` int(11) NOT NULL,
  `update_aiu` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_version` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cpkey
-- ----------------------------
CREATE TABLE `cpkey` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updatetime` bigint(20) NOT NULL,
  `c_id` bigint(20) DEFAULT NULL,
  `p_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5A79ED2BFCAF579` (`c_id`),
  KEY `FK5A79ED2500AF287` (`p_id`),
  CONSTRAINT `FK5A79ED2500AF287` FOREIGN KEY (`p_id`) REFERENCES `packs` (`id`),
  CONSTRAINT `FK5A79ED2BFCAF579` FOREIGN KEY (`c_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cid` varchar(255) DEFAULT NULL,
  `data` bigint(20) DEFAULT NULL,
  `exp` bigint(20) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `m_number` varchar(255) DEFAULT NULL,
  `mac` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `os` int(11) NOT NULL,
  `portrait` varchar(255) DEFAULT NULL,
  `psd` varchar(255) DEFAULT NULL,
  `serialNumber` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `lv_id` bigint(20) DEFAULT NULL,
  `vid_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK24217FDE77569E2A` (`lv_id`),
  KEY `FK24217FDEB2B1ADF2` (`vid_id`),
  KEY `idx_m_number` (`m_number`),
  CONSTRAINT `FK24217FDE77569E2A` FOREIGN KEY (`lv_id`) REFERENCES `leveltype` (`id`),
  CONSTRAINT `FK24217FDEB2B1ADF2` FOREIGN KEY (`vid_id`) REFERENCES `clientversion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_games
-- ----------------------------
CREATE TABLE `customer_games` (
  `customer_id` bigint(20) NOT NULL,
  `addgame_id` bigint(20) NOT NULL,
  UNIQUE KEY `addgame_id` (`addgame_id`),
  KEY `FK8663372052AE89DD` (`addgame_id`),
  KEY `FK8663372055FD001E` (`customer_id`),
  CONSTRAINT `FK8663372052AE89DD` FOREIGN KEY (`addgame_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FK8663372055FD001E` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_packs
-- ----------------------------
CREATE TABLE `customer_packs` (
  `customer_id` bigint(20) NOT NULL,
  `addpack_id` bigint(20) NOT NULL,
  UNIQUE KEY `addpack_id` (`addpack_id`),
  KEY `FK86E1E5D92E40847D` (`addpack_id`),
  KEY `FK86E1E5D955FD001E` (`customer_id`),
  CONSTRAINT `FK86E1E5D955FD001E` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK86E1E5D92E40847D` FOREIGN KEY (`addpack_id`) REFERENCES `packs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for everygame
-- ----------------------------
CREATE TABLE `everygame` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `mtype` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `hit` bigint(20) DEFAULT NULL,
  `res` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `txt` text,
  `type` int(11) DEFAULT NULL,
  `everygametype_id` bigint(20) DEFAULT NULL,
  `game_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK901B98D4429C99E` (`game_id`),
  KEY `FK901B98D5218A3F6` (`everygametype_id`),
  KEY `idx_data` (`data`),
  KEY `idx_everygame_star` (`star`),
  KEY `idx_title` (`title`),
  CONSTRAINT `FK901B98D4429C99E` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FK901B98D5218A3F6` FOREIGN KEY (`everygametype_id`) REFERENCES `everygametype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for everygametype
-- ----------------------------
CREATE TABLE `everygametype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `everygametype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for game_message
-- ----------------------------
CREATE TABLE `game_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) NOT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `game_id` bigint(20) DEFAULT NULL,
  `news_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCAE0E0BA4429C99E` (`game_id`),
  KEY `FKCAE0E0BAEC7BD203` (`news_id`),
  CONSTRAINT `FKCAE0E0BA4429C99E` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FKCAE0E0BAEC7BD203` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for games
-- ----------------------------
CREATE TABLE `games` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `mtype` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `hit` bigint(20) DEFAULT NULL,
  `res` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `bbsurl` varchar(255) DEFAULT NULL,
  `exp` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `picture1` varchar(255) NOT NULL,
  `picture10` varchar(255) DEFAULT NULL,
  `picture2` varchar(255) DEFAULT NULL,
  `picture3` varchar(255) DEFAULT NULL,
  `picture4` varchar(255) DEFAULT NULL,
  `picture5` varchar(255) DEFAULT NULL,
  `picture6` varchar(255) DEFAULT NULL,
  `picture7` varchar(255) DEFAULT NULL,
  `picture8` varchar(255) DEFAULT NULL,
  `picture9` varchar(255) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `txt` text,
  `type` int(11) DEFAULT NULL,
  `gtype_id` bigint(20) DEFAULT NULL,
  `downloadurl` varchar(255) DEFAULT NULL,
  `ranking` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5D932C1CAC7F769` (`gtype_id`),
  KEY `idx_games_star` (`star`),
  KEY `idx_data` (`data`),
  KEY `idx_title` (`title`),
  CONSTRAINT `FK5D932C1CAC7F769` FOREIGN KEY (`gtype_id`) REFERENCES `gametype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gametype
-- ----------------------------
CREATE TABLE `gametype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gametype_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for indexpage
-- ----------------------------
CREATE TABLE `indexpage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pic` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for leveltype
-- ----------------------------
CREATE TABLE `leveltype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logs
-- ----------------------------
CREATE TABLE `logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL,
  `data` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news
-- ----------------------------
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `mtype` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `hit` bigint(20) DEFAULT NULL,
  `res` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `describe_aiu` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `picture1` varchar(255) DEFAULT NULL,
  `picture10` varchar(255) DEFAULT NULL,
  `picture2` varchar(255) DEFAULT NULL,
  `picture3` varchar(255) DEFAULT NULL,
  `picture4` varchar(255) DEFAULT NULL,
  `picture5` varchar(255) DEFAULT NULL,
  `picture6` varchar(255) DEFAULT NULL,
  `picture7` varchar(255) DEFAULT NULL,
  `picture8` varchar(255) DEFAULT NULL,
  `picture9` varchar(255) DEFAULT NULL,
  `txt1` text,
  `txt10` text,
  `txt2` text,
  `txt3` text,
  `txt4` text,
  `txt5` text,
  `txt6` text,
  `txt7` text,
  `txt8` text,
  `txt9` text,
  `game_id` bigint(20) DEFAULT NULL,
  `newtype_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK338AD34429C99E` (`game_id`),
  KEY `FK338AD38A307F6` (`newtype_id`),
  KEY `idx_data` (`data`),
  KEY `idx_title` (`title`),
  CONSTRAINT `FK338AD34429C99E` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FK338AD38A307F6` FOREIGN KEY (`newtype_id`) REFERENCES `newtype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for newtype
-- ----------------------------
CREATE TABLE `newtype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `newtype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for packs
-- ----------------------------
CREATE TABLE `packs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `mtype` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `describe_aiu` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `game_id` bigint(20) DEFAULT NULL,
  `ranking` int(11) NOT NULL,
  `remaining` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK657E17A4429C99E` (`game_id`),
  KEY `idx_data` (`data`),
  KEY `idx_packs_star` (`star`),
  KEY `idx_title` (`title`),
  CONSTRAINT `FK657E17A4429C99E` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pubchannel
-- ----------------------------
CREATE TABLE `pubchannel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` bigint(20) DEFAULT NULL,
  `exp` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `pubchannelname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for searchkey
-- ----------------------------
CREATE TABLE `searchkey` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `num` bigint(20) NOT NULL,
  `searchkey` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_pkey
-- ----------------------------
CREATE TABLE `t_pkey` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pkey` varchar(255) DEFAULT NULL,
  `pack_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCB6168DA1FBBC43E` (`pack_id`),
  CONSTRAINT `FKCB6168DA1FBBC43E` FOREIGN KEY (`pack_id`) REFERENCES `packs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ttt
-- ----------------------------
CREATE TABLE `ttt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ttt1` tinyblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `adlist` VALUES ('8', '1', '1', '2');
INSERT INTO `adlist` VALUES ('9', '1', '3', '4');
INSERT INTO `adlist` VALUES ('13', '1', '3', '2');
INSERT INTO `adlist` VALUES ('14', '1', '1', '3');
INSERT INTO `appsession` VALUES ('9', '1385912529376', 'f2e8d93c-c205-468a-8c6d-4f0df01302c3', '9');
INSERT INTO `carouseltype` VALUES ('2', '每日一游&玩嗨周五');
INSERT INTO `carouseltype` VALUES ('3', '游戏');
INSERT INTO `carouseltype` VALUES ('4', '新闻');
INSERT INTO `carouseltype` VALUES ('5', '礼包');
INSERT INTO `clientversion` VALUES ('1', '1385912558590', '1', '这是一个更新测试', 'http://www.sohu.com', '1.0.0');
INSERT INTO `cpkey` VALUES ('1', '1386490357528', '9', '1');
INSERT INTO `cpkey` VALUES ('2', '1386490460373', '9', '4');
INSERT INTO `customer` VALUES ('9', '12345', '1385912529359', '0', '0', null, '13838389439', '12,14,87,jj,ff', 'ss', '1', 'null|null', '123456', null, '土豪金5S', '1', null);
INSERT INTO `customer_games` VALUES ('9', '1');
INSERT INTO `customer_games` VALUES ('9', '2');
INSERT INTO `everygame` VALUES ('1', '1385913920244', '1', '每日一游测试1', '500', '百度', 'http://baidu.com', '我', '50255be3-6ba9-4347-a672-dd2b84f187af|image/png', '4', '是非得失飞个个 打撒', '0', '1', '1');
INSERT INTO `everygame` VALUES ('2', '1385913960042', '1', '每日一游测试2', null, '', '', '你', '65b610a5-b7f3-4452-8b8d-870110b5b6f9|image/png', '5', '测试2', '1', '1', '2');
INSERT INTO `everygame` VALUES ('3', '1385913995444', '1', '玩嗨周五1', null, '百度', '', '他', 'b1888773-0f65-422c-ae6f-667d696a9a2b|image/jpeg', '3', '非师范飞东方飞', '1', '2', '1');
INSERT INTO `everygame` VALUES ('4', '1385914034437', '1', '玩嗨周五测试2', null, '', '', '', '40e36996-35f4-4f18-b64d-1f580414e641|image/png', '5', '规范过放电风格规范', '0', '2', '2');
INSERT INTO `everygametype` VALUES ('1', '每日一游');
INSERT INTO `everygametype` VALUES ('2', '玩嗨周五');
INSERT INTO `game_message` VALUES ('2', '1386510941491', '我知道我了', '4', null);
INSERT INTO `game_message` VALUES ('3', '1386510943169', '我道我了', '4', null);
INSERT INTO `game_message` VALUES ('4', '1386513115557', '我道我了', '2', null);
INSERT INTO `game_message` VALUES ('5', '1386513120616', '我道eee我了', '2', null);
INSERT INTO `game_message` VALUES ('6', '1386513124324', '我道eee我了', '3', null);
INSERT INTO `game_message` VALUES ('7', '1386513127573', '我道999ee我了', '3', null);
INSERT INTO `game_message` VALUES ('8', '1386513131553', '我道999ee我了', null, '3');
INSERT INTO `game_message` VALUES ('9', '1386513135257', '我道999ee我了', null, '2');
INSERT INTO `game_message` VALUES ('10', '1386513138576', '我道[p99ee我了', null, '2');
INSERT INTO `game_message` VALUES ('11', '1386513142632', '我道规范的负担9ee我了', null, '2');
INSERT INTO `game_message` VALUES ('12', '1386513145945', '我道规范4的负担9ee我了', null, '2');
INSERT INTO `games` VALUES ('1', '1385913433940', '1', '游戏测试1', '401', '', '', 'hzh', 'http://www.hao123.com', '20', '3d66f13a-6d52-4143-b66d-2345dbd530e8|image/x-icon', '1a65cf4c-bdbc-4fd1-802a-9706613cd5fd|image/jpeg', '1a455559-a712-4210-bc0a-4cd760efa0a1|image/png', '9b908617-e0a7-41d9-b4ac-8b2c5127999e|image/jpeg', 'ebd32760-e0e3-4648-b876-59401c040c4d|image/jpeg', '99fa05e0-85cc-4465-812a-a9de349ebaa3|image/jpeg', '913e4bff-a6ef-4211-b518-8e32b9dc2459|image/png', 'e04f2152-84a3-4e83-8db4-92b7e8f48831|image/png', 'be3e49e9-43a2-47dd-ac13-b4826ab2b147|image/png', '742a55fe-c991-47a5-b2cd-fb0c6930d8d6|image/png', 'b432f37d-a355-4a00-9e01-15f8fedfeced|image/png', '4', ' 游戏1测试', '1', '1', 'www.hao123.com', '20');
INSERT INTO `games` VALUES ('2', '1385913644187', '1', '游戏测试22', '301', '百度', 'http://baidu.com', '我', 'http://www.hao123.com4', '30', '5fb1c525-bf2a-42ed-b502-da5d1d39160c|image/x-icon', '061ecddf-54f1-4af9-85c6-2ee170822aaa|image/png', 'bbb3316f-a4fe-41d2-ac05-0217c2ecfaed|image/jpeg', 'a9275030-f5d5-4041-afc6-c7802b8410fa|image/png', 'null|null', 'null|null', 'eb22fb86-f17f-4e56-aea9-0ed4f2e096c5|image/png', 'null|null', '5624937b-a943-4f59-97ad-7e5af520301e|image/png', 'null|null', 'null|null', '5', '游戏测试2', '1', '2', 'www.hao123.com', '19');
INSERT INTO `games` VALUES ('3', '1386510098010', '1', '火影忍者', '400', '百度', '', 'hzh', 'http://www.hao123.com', '20', 'b0a410fa-e0c3-4438-a802-fd18534c2760|image/x-icon', '02686cc2-bb74-4d24-afa8-2b8b5e2461f6|image/png', null, null, null, null, null, null, null, null, null, '4', 'fdsdf 游戏简介', '1', '1', 'www.hao123.com', '18');
INSERT INTO `games` VALUES ('4', '1386510175857', '1', '23 飞', '500', '', 'http://baidu.com', 'hzh', '', '30', '9a6d200a-801b-46a8-b80e-6deec73b1150|image/x-icon', '51b61d7e-44ae-4e3d-94ef-3c94f3f5df59|image/png', null, null, null, null, null, null, null, null, null, '4', '游戏测试55', '1', '2', 'www.hao123.com', '0');
INSERT INTO `games` VALUES ('5', '1386510221931', '1', '犬夜叉', '500', '百度', 'http://baidu.com', '韩朝', 'http://www.hao123.com', '30', '40060477-7d48-4224-809a-b235fee9b85a|image/x-icon', '7d031e20-54f8-447a-90cd-22d0cc375691|image/png', null, null, null, null, null, null, null, null, null, '4', '测试', '0', '2', 'www.hao123.com', '0');
INSERT INTO `gametype` VALUES ('1', '动作冒险');
INSERT INTO `gametype` VALUES ('2', '休闲娱乐');
INSERT INTO `indexpage` VALUES ('1', 'bb3d5989-24f9-4fdb-8191-f3322e5985c4|image/jpeg');
INSERT INTO `leveltype` VALUES ('1', '小学生');
INSERT INTO `leveltype` VALUES ('2', '中学生');
INSERT INTO `leveltype` VALUES ('3', '高中生');
INSERT INTO `leveltype` VALUES ('4', '大学生');
INSERT INTO `news` VALUES ('1', '1385914285993', '1', '新闻测试1', '401', '百度', 'http://baidu.com', '我', '简介1', 'df051cdd-595d-44b9-a20a-547fef40721e|image/png', 'd47c2a93-6b1f-490c-88c0-2032d786e921|image/jpeg', '08474e31-251a-4d8b-93b9-2b5651d93e58|image/png', '1135cb6d-e477-43cc-b971-856957b91e5f|image/jpeg', 'f26dc25a-87fd-4249-987a-cf60bec5b3b4|image/jpeg', '8a5c0e77-39e2-46a5-8091-db3fe71d0904|image/jpeg', 'ee4e0d1d-06fb-42cb-85fc-22968d19adfa|image/png', '25ffea6a-bed3-4586-b777-c95979568c65|image/png', '36595ad1-a610-48f2-8f1b-04338120cbff|image/png', '0d71b1e8-4d29-4310-afbc-ca1830a79af5|image/png', 'd6b10861-5c38-4de2-9fc8-554b8dbafe98|image/png', '图片1', '图片10', '图片2', '图片3', '图片4', '图片5', '图片6', '图片7', '图片8', '图片9', '1', '1');
INSERT INTO `news` VALUES ('2', '1385914407447', '1', '新闻测试2', '401', '', '', '', '简介2', '90a3414a-c745-4302-80d7-6a8d10eb758d|image/x-icon', 'face542b-0b43-4f3a-8963-d5c0e12d358d|image/png', 'null|null', 'null|null', '64a32316-dc03-4ec2-97aa-94c02f01b898|image/png', 'null|null', '91471805-c13d-4450-97ba-d71342da5e73|image/png', '4ee0e713-8233-4ae4-bbe4-a7a1e9a99e72|image/png', 'null|null', 'null|null', 'null|null', '打撒', '', '', '打撒', '', '', '的飞飞', '', '', '', null, '1');
INSERT INTO `news` VALUES ('3', '1385914458181', '1', '攻略1', '501', '百度', 'http://baidu.com', '哈哈', '简介3', 'b0a50104-fc36-492d-988d-82edbe04f22f|image/x-icon', '3d59b21c-cfbe-4ca0-b15a-20e77d94e766|image/jpeg', 'd3a24502-35c8-4ddd-9bb4-f208e98a7c7e|image/png', '5c7a0bd0-ef6a-4d33-92bd-bca24003d841|image/jpeg', '9f4553f1-ccf5-40dc-aa5f-f12e691d93d2|image/jpeg', '0076db39-1861-47a9-988a-e848b8334266|image/jpeg', 'c00eec59-3be3-43b5-bf8f-b94dde5f638d|image/png', '0f5ce058-b275-4c6b-9c1f-e968b7ee5c23|image/png', '963fdc50-2771-425f-89b3-d41b4f75b076|image/png', '10ef7fc3-1bb7-41cf-ae72-3f1528a7f629|image/png', '57312d33-66c8-4dd0-a8a5-61365621b572|image/png', '图片1', '图片10', '图片2', '图片3', '图片4', '图片5', '图片6', '图片7', '图片8', '图片9', '2', '2');
INSERT INTO `news` VALUES ('4', '1385914556244', '1', '评测1', '1', '', '', '', '简介5', '6c71dd5e-d7a0-4ee5-b82b-5b7275a8064c|image/x-icon', '36c126ca-4f95-450d-8f9a-bab2d9ae6da2|image/png', 'null|null', 'fe1c26bb-74a2-46a1-9a0d-0fdced1a4509|image/png', 'null|null', 'null|null', 'null|null', 'null|null', 'null|null', 'null|null', 'null|null', '饿位', '', '额外 饿', '', '', '', '', '', '', '', '1', '3');
INSERT INTO `news` VALUES ('5', '1385914600642', '1', '评测2', '400', '', '', '辅导书', '简介11', 'a23c1884-5256-40dc-894b-e5f6716b0b22|image/x-icon', 'd473824c-c0f8-4a08-8daf-399e829b9b61|image/jpeg', '6615fcd8-c81a-4047-8eda-89a7aaf043f9|image/png', 'b8a17b0a-005c-44c4-a722-55c6eee3bd78|image/jpeg', '7ee23e04-3f06-4192-a63e-948d0a4f8846|image/jpeg', '61856e72-1318-476e-a1d2-99e2f6a00880|image/jpeg', 'c5505072-bfa5-4e40-a5e7-667d6ee54689|image/png', '233258dc-b27a-46f7-9bc8-518d4d1160bf|image/png', 'cd397c6c-60fe-489e-99b2-489b369321da|image/png', '90d7b879-faca-4b66-9df8-2fb219adf4c6|image/png', 'ba77812e-4461-47de-9bd8-72a2e4e6ef17|image/png', '图片1', '图片10', '图片2', '图片3', '图片4', '图片5', '图片6', '图片7', '图片8', '图片9', '2', '3');
INSERT INTO `newtype` VALUES ('1', '游戏新闻');
INSERT INTO `newtype` VALUES ('2', '攻略');
INSERT INTO `newtype` VALUES ('3', '评测');
INSERT INTO `packs` VALUES ('1', '1386470056206', '1', '火影忍者', '出席这次选择', 'd2e6a2d9-389e-4099-9fb1-6d091a23e285|image/x-icon', '4', '0', '1', '0', '2013-12-26 00:00:00');
INSERT INTO `packs` VALUES ('2', '1386482048893', '1', '路飞', '礼包测试1', 'fa85a564-a4bb-4898-8765-0968b3cecefd|image/x-icon', '1', '0', '1', '1', '2013-12-25 00:00:00');
INSERT INTO `packs` VALUES ('3', '1386482095036', '1', '左诺', '礼包测试2', 'a2790279-881c-4e2e-a404-a19f78494c48|image/x-icon', '4', '1', '1', '3', '2013-12-25 00:00:00');
INSERT INTO `packs` VALUES ('4', '1386482126507', '1', '山治', '礼包测试3', '046afad0-4725-4271-b84f-54a5813e7d13|image/x-icon', '4', '2', '2', '2', '2013-12-26 00:00:00');
INSERT INTO `packs` VALUES ('5', '1386482165335', '1', '娜美', '礼包测试4', '7c794e39-e7f5-44f9-9bd4-7738a36eefbd|image/png', '4', '1', '1', '4', '2013-12-19 00:00:00');
INSERT INTO `packs` VALUES ('6', '1386482970478', '1', '骗人布', '礼包测试6', 'db464d80-1fc4-4a77-9d2f-aaf804a6e442|image/png', '4', '0', '1', '0', '2013-12-31 00:00:00');
INSERT INTO `pubchannel` VALUES ('1', '1385912996253', '10', 'b8ab4901-6cde-4dc6-a4f0-3cace40c4c2a|image/png', '玩嗨周五');
INSERT INTO `pubchannel` VALUES ('2', '1385913034688', '20', 'df259b95-9cce-4b68-9a6f-347ade3dca39|image/png', '新闻');
INSERT INTO `pubchannel` VALUES ('3', '1385913048386', '30', 'add17f12-27f9-4ab2-b31e-735507da47c7|image/png', '每日一游');
INSERT INTO `pubchannel` VALUES ('4', '1385913057388', '40', 'fa4e371c-740d-455e-9812-348d3ff8cc51|image/png', '游戏礼包');
INSERT INTO `pubchannel` VALUES ('5', '1385913080012', '50', '4207e504-7e82-46cb-9462-89f19d614d91|image/png', '火爆活动');
INSERT INTO `pubchannel` VALUES ('6', '1385913100638', '60', '01b76d3a-76d4-4310-9a07-aef44b9b1380|image/png', '精品游戏');
INSERT INTO `t_pkey` VALUES ('6', '6565465465', '3');
INSERT INTO `t_pkey` VALUES ('9', '32645474', '1');
INSERT INTO `t_pkey` VALUES ('10', '5665765', '1');
INSERT INTO `t_pkey` VALUES ('11', '534543', '1');
INSERT INTO `t_pkey` VALUES ('12', '467867878', '4');
INSERT INTO `ttt` VALUES ('1', 0x34626333396362372D363561352D343065612D613337362D6530646465633836623163307C6170706C69636174696F6E2F6F637465742D73747265616D);
