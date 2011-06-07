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

# Dumping structure for table mayflower.ref_article_user
DROP TABLE IF EXISTS `ref_article_user`;
CREATE TABLE IF NOT EXISTS `ref_article_user` (
  `article_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻、用户关联表';

# Dumping data for table mayflower.ref_article_user: ~0 rows (approximately)
DELETE FROM `ref_article_user`;
/*!40000 ALTER TABLE `ref_article_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `ref_article_user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
