# --------------------------------------------------------
# Host:                         localhost
# Server version:               5.5.8
# Server OS:                    Win32
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2011-05-19 09:38:55
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping structure for table mayflower.user
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `NICKNAME` varchar(45) DEFAULT NULL,
  `REG_DATE` datetime DEFAULT NULL,
  `LASTlOGIN_DATE` datetime DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FIRST_IP` varchar(45) DEFAULT NULL,
  `ROLE` varchar(45) DEFAULT NULL,
  `profile` text COMMENT '个人简介',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户表';

# Dumping data for table mayflower.user: ~11 rows (approximately)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`ID`, `USERNAME`, `PASSWORD`, `NICKNAME`, `REG_DATE`, `LASTlOGIN_DATE`, `EMAIL`, `FIRST_IP`, `ROLE`, `profile`) VALUES
	(1, 'erhu', 'admin', '二胡', '2010-11-15 00:00:00', NULL, 'erhu.cn@qq.com', '172.16.1.12', 'admin', '牺牲自由而换取暂时自我保全的人，即得不到自由也得不到保全！--华盛顿'),
	(2, '127.0.0.1', '12345', 'localhost', '2011-03-10 10:24:47', '2011-03-10 10:24:47', '127.0.0.1', '127.0.0.1', 'admin', NULL),
	(3, '172.16.1.238', '12345', '238', '2011-03-10 10:32:17', '2011-03-10 10:32:17', '172.16.1.238', '172.16.1.238', 'visitor', NULL),
	(4, '192.168.1.101', '12345', '192.168.1.101', '2011-03-10 21:24:44', '2011-03-10 21:24:44', NULL, '192.168.1.101', 'visitor', NULL),
	(5, 'admin', 'admin', 'admin', NULL, NULL, 'erhu.com@gmail.com', '172.16.1.12', 'admin', NULL),
	(6, '我是100', '12345', '192.168.1.100', '2011-03-17 22:43:32', '2011-03-17 22:43:32', '100@gmail.com', '192.168.1.100', 'visitor', ''),
	(7, '192.168.1.102', '12345', '192.168.1.102', '2011-03-17 22:51:35', '2011-03-17 22:51:35', '192.168.1.102', '192.168.1.102', 'visitor', NULL),
	(8, '172.16.1.12', '12345', '172.16.1.12', '2011-03-18 09:59:03', '2011-03-18 09:59:03', '172.16.1.12', '172.16.1.12', 'visitor', NULL),
	(9, '172.16.1.204', '12345', '172.16.1.204', '2011-03-18 10:37:45', '2011-03-18 10:37:45', '172.16.1.204', '172.16.1.204', 'visitor', NULL),
	(10, '172.16.1.254', '12345', '172.16.1.254', '2011-03-18 12:15:25', '2011-03-18 12:15:25', '172.16.1.254', '172.16.1.254', 'visitor', NULL),
	(11, '172.16.1.53', '12345', '172.16.1.53', '2011-03-18 12:21:47', '2011-03-18 12:21:47', '172.16.1.53', '172.16.1.53', 'visitor', NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
