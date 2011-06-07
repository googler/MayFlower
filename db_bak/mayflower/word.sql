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

# Dumping structure for table mayflower.word
DROP TABLE IF EXISTS `word`;
CREATE TABLE IF NOT EXISTS `word` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `spelling` varchar(45) DEFAULT NULL,
  `phonetic_symbol` varchar(45) DEFAULT NULL COMMENT '音标',
  `meaning` text,
  `hits` int(10) unsigned DEFAULT '0' COMMENT '查询次数',
  `remembered` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='单词表';

# Dumping data for table mayflower.word: ~51 rows (approximately)
DELETE FROM `word`;
/*!40000 ALTER TABLE `word` DISABLE KEYS */;
INSERT INTO `word` (`id`, `spelling`, `phonetic_symbol`, `meaning`, `hits`, `remembered`) VALUES
	(4, 'acknowledgement', '', 'n.\r\n承认, 确认, 感谢', 0, 0),
	(5, 'generous', '', 'adj.\r\n慷慨的, 大方的, 有雅量的', 0, 0),
	(6, 'phenomenal', '', 'adj.\r\n显著的, 现象的, 能知觉的', 0, 0),
	(7, 'stylish', '', 'adj.\r\n时髦的, 漂亮的, 流行的', 0, 0),
	(8, 'versatile', '', 'adj.\r\n通用的, 万能的, 多才多艺的, 多面手的', 0, 0),
	(9, 'accelerometer', '', 'n.\r\n加速计', 0, 0),
	(10, 'enticing', '', 'adj.\r\n引诱的, 迷人的', 0, 0),
	(11, 'proprietary', '', 'adj.\r\n所有的, 私人拥有的\r\nn.\r\n所有者, 所有权', 0, 0),
	(12, 'restrict', '', 'vt.\r\n限制, 约束, 限定', 0, 0),
	(13, 'alternative', '', 'n.\r\n二中择一, 可供选择的办法, 事物\r\nadj.\r\n选择性的, 二中择一的', 0, 0),
	(14, 'barrier', '', 'n.\r\n(阻碍通道的)障碍物, 栅栏, 屏障', 0, 0),
	(15, 'artificial', '', 'adj.\r\n人造的, 假的, 非原产地的', 0, 0),
	(16, 'philosophy', '', 'n.\r\n哲学, 哲学体系, 达观, 冷静', 0, 0),
	(17, 'leverage', '', 'n.\r\n杠杆作用\r\n杠杆机构\r\n杠杆(效)率, 杠杆臂长比; 扭转力矩\r\n达到目的的手段; 势力; 影响', 0, 0),
	(18, 'admitted', '', 'adj.\r\n被承认的, 被确认无疑的', 0, 0),
	(19, 'mnemonic', '', 'adj.\r\n记忆的, 记忆术的', 0, 0),
	(20, 'achieve', '', 'vt.\r\n完成, 达到', 0, 0),
	(21, 'versatility', '', 'n.\r\n多功能性', 0, 0),
	(22, 'sensitive', '', 'adj.\r\n有感觉的, 敏感[锐]的\r\n易受伤害的, 容易生气的\r\n神经质的, 过敏的\r\n(软片等)易感光的\r\n易受影响的\r\n极机密的, 极微妙的\r\n[罕]感觉的, 感官的\r\n易受感动的, 仁慈的\r\n能记录微小变化的', 0, 0),
	(23, 'scratch', '', 'n.\r\n乱写, 刮擦声, 抓痕, 擦伤\r\nvt.\r\n乱涂, 勾抹掉, 擦, 刮, 搔, 抓, 挖出\r\nvi.\r\n发刮擦声, 搔, 抓\r\nadj.\r\n打草稿用的, 凑合的', 0, 0),
	(24, 'punctuation', '', 'n.\r\n标点, 标点符号', 0, 0),
	(25, 'relevant', '', 'adj.\r\n有关的; 恰当的, 贴切的, 切题的, 中肯的(to)\r\n成比例的; 相应的\r\n有重大意义[作用]的; 实质性的', 0, 0),
	(26, 'asterisk', '', 'n.\r\n星号', 0, 0),
	(27, 'abbreviate', '', 'v.\r\n缩写, 缩短, 简化, 简写成, 缩写为', 0, 0),
	(28, 'encounter', '', 'v.\r\n遭遇, 遇到, 相遇\r\nn.\r\n遭遇, 遭遇战', 0, 0),
	(29, 'variety', '', 'n.\r\n变化, 多样性, 种种, 品种, 种类', 0, 0),
	(30, 'keystroke', '', 'n.\r\n[计]键击，按键', 0, 0),
	(31, 'gravity', '', 'n.\r\n地心引力, 重力', 0, 0),
	(32, 'spinner', '', 'n.\r\n[计]微调控制项', 0, 0),
	(33, 'maneuver', '', 'n.\r\n调运\r\n演习\r\n机动运用\r\n运用, 操作\r\n(工作中的)动作, 程序, 方法\r\n处理事情', 0, 0),
	(34, 'permanent', '', 'adj.\r\n永久的, 持久的', 0, 0),
	(35, 'neat', '', 'adj.\r\n整洁的, 灵巧的, 优雅的, (酒)纯的, 未搀水的', 0, 0),
	(36, 'caveat', '', 'n.\r\n[律]中止诉讼手续的申请, 警告, 告诫', 0, 0),
	(37, 'punctuation', '', 'n.\r\n标点, 标点符号', 0, 0),
	(38, 'trick', '', 'n.\r\n诡计, 骗局, 恶作剧, 窍门, 诀窍\r\nvt.\r\n欺骗, 哄骗', 0, 0),
	(39, 'sake', '', 'n.\r\n为了...之好处, 出于对...的兴趣, 缘故, 理由, 日本米酒', 0, 0),
	(40, 'debate', '', 'v.\r\n争论, 辩论\r\nn.\r\n争论, 辩论', 0, 0),
	(41, 'optimism', '', 'n.\r\n乐观, 乐观主义', 0, 0),
	(42, 'pessimism', '', 'n.\r\n悲观, 悲观主义', 0, 0),
	(43, 'edification', '', 'n.（名词）\r\nIntellectual, moral, or spiritual improvement; enlightenment.\r\n启迪：智力、伦理或精神上的提高；启蒙', 0, 0),
	(44, 'laurel', '', 'n.\r\n[植]月桂树, 桂冠, 殊荣\r\nvt.\r\n使戴桂冠, 授予荣誉', 0, 0),
	(45, 'horizon', '', 'n.\r\n地平(线; 圈), 水平(线)\r\n地[水]平仪, 水平反射镜, (经纬仪的)水平度盘\r\n等高线\r\n视野, 眼界, 视距, 见识, 范围, 前景\r\n【地质】层位\r\n距天顶90度的大圆圈, 构成座标地平系统的赤道\r\n与重力方向相垂直的平面, 与天体相交的圆圈\r\n特定时期的地质沉积物', 0, 0),
	(46, 'embrace', '', 'vt.\r\n拥抱, 互相拥抱, 包含, 收买, 信奉\r\nvi.\r\n拥抱\r\nn.\r\n拥抱', 0, 0),
	(47, 'relevant', '', 'adj.\r\n有关的, 相应的', 0, 0),
	(48, 'encounter', '', 'v.\r\n遭遇, 遇到, 相遇\r\nn.\r\n遭遇, 遭遇战', 0, 0),
	(49, 'insane', '', 'adj.\r\n患精神病的, 精神病患者的, 极愚蠢的', 0, 0),
	(50, 'crack', '', 'n.\r\n裂缝, 噼啪声\r\nv.\r\n(使)破裂, 裂纹, (使)爆裂\r\nadj.\r\n最好的, 高明的', 0, 0),
	(51, 'instinctive', '', 'adj.\r\n本能的', 0, 0),
	(52, 'discipline', '', 'n.\r\n纪律, 学科\r\nv.\r\n训练', 0, 0),
	(53, 'synergistic', '', '增效的\r\n 协作的\r\n 互相作用[促进]的', 0, 0),
	(54, 'prolific', '', 'adj.\r\n多产的, 丰富的, 大量繁殖的', 0, 0);
/*!40000 ALTER TABLE `word` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
