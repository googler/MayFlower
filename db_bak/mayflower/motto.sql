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

# Dumping structure for table mayflower.motto
DROP TABLE IF EXISTS `motto`;
CREATE TABLE IF NOT EXISTS `motto` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(140) NOT NULL,
  `author` varchar(45) DEFAULT NULL,
  `tag` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='醒世恒言';

# Dumping data for table mayflower.motto: ~26 rows (approximately)
DELETE FROM `motto`;
/*!40000 ALTER TABLE `motto` DISABLE KEYS */;
INSERT INTO `motto` (`id`, `content`, `author`, `tag`) VALUES
	(1, '用错误的方式解决正确的问题，总比用正确的方法解决错误的问题好。', 'UNIX编程艺术', '编程箴言'),
	(2, '最终用户永远比操作系统设计人员更清楚他们究竟需要什么。', 'UNIX编程艺术', '编程箴言'),
	(3, '策略相对短寿，而机制才会长存。', 'UNIX编程艺术', '编程箴言'),
	(4, '对程序员来说，如果完成某项任务所付出的努力对他们是个挑战却又恰好还在力所能及的范围内，他们就会觉得很有乐趣。', 'UNIX编程艺术', '编程箴言'),
	(5, '毫无动力，松松垮垮且薪水微薄的程序员们，能在短短期限内，如同灵魂附体般造出稳定而新颖的软件，只不过是经理人的一个梦呓罢了。', 'UNIX编程艺术', '编程箴言'),
	(6, '让每个程序做好一件事情，并做好它。', 'UNIX编程艺术', '编程箴言'),
	(7, '尽可能早地将软件投入使用，对拙劣的代码别犹豫，扔掉重写。', 'UNIX编程艺术', '编程箴言'),
	(8, '程序要能处理文本流，这是最通用的接口。', 'UNIX编程艺术', '编程箴言'),
	(9, '优化代码，在你找到瓶颈以后。', 'UNIX编程艺术', '编程箴言'),
	(10, '花哨的代码在N很小时通常很慢，而N通常很小。', 'UNIX编程艺术', '编程箴言'),
	(11, '花哨的代码易出BUG，且更难实现，尽量使用简单的算法，配合简单的数据结构。', 'UNIX编程艺术', '编程箴言'),
	(12, '编程的核心是数据结构，而不是算法。如果你把数据结构组织得井井有条，正确的算法便不言自明。', 'UNIX编程艺术', '编程箴言'),
	(13, '拿不准就穷举', 'UNIX编程艺术', '编程箴言'),
	(14, '模拟原则：使用简单的接口拼合简单的部件。', 'UNIX编程艺术', '编程箴言'),
	(15, '计算机编程的本质是控制复杂度；要编制复杂软件而又不至于一败涂地的唯 一方法就是降低整体复杂度。', 'UNIX编程艺术', '编程箴言'),
	(16, '清楚原则：清晰胜于机巧；', 'UNIX编程艺术', '编程箴言'),
	(17, '组合原则：设计是考虑拼接组合；提倡采用简单、文本化、面向流、设备无关的格式；以一点点格式解析的代价，换得可以使用通用工具来构造或者解读的数据流的代价是值得的。', 'UNIX编程艺术', '编程'),
	(18, '#DEBUG#本身是一个敏感、感性的话题，你可能会遇到抵赖、推诿、蹩足的借口或者无动于衷，这或许是某些工作环境的文化的一部分，但首要的是接受事实，并据此发动进攻，解决问题！Fire, Fix the Problem, Not the Blame!', 'The Pragmatic Programmer', '软件调试'),
	(19, 'I can accept failure but I can\'t accept not trying.', 'Michael Jordan.', '励志'),
	(20, '在长春上了三年学，在大连要写了四年代码。当七年之痒到的时候，我该走了，真的该回家了！', '二胡', '随感'),
	(21, '扶摇千里路，孤云百尺楼。危阑拍遍，醉踏鹰飞下南州。关山万里纵横，连营犀角吹彻，天下掌中收。长剑断旌羽，一骑斩王侯。烽烟尽，英雄老，水空流。 叹息兴亡事，聚散两悠悠。萋萋弥河芳草，匹马斜阳古道，西风敝貂裘。 回首梦魂远，杯酒过春秋。', 't.cn', '读史咏怀'),
	(22, '#记事本#给搜索结果一个新选项（链接）called“只匹配标签”', '记事本', '记事本'),
	(23, '这里有个CSS3BUTTON项目，拿过来用吧！！\r\nhttp://www.oschina.net/p/css3buttons', 'OSCHINA', 'BUTTON'),
	(24, 'If you think you can win, you can win. Faith is necessary to victory.', 't.cn', 'faith'),
	(25, 'The most important thing in life is to learn how to give out love, and to let it come in.', 't.cn', 'life'),
	(26, '设计模式里的委托是什么意思？', '疑问', '提问');
/*!40000 ALTER TABLE `motto` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
