DROP TABLE IF EXISTS `huge`.`code`;
CREATE TABLE  `huge`.`code` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(45) NOT NULL COMMENT '代码标题',
  `AUTHOR` varchar(45) NOT NULL COMMENT '发布人',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '发布时间',
  `LASTMODIFY_DATE` datetime DEFAULT NULL COMMENT '最后修改时间',
  `CONTENT` longtext COMMENT '代码段',
  `TAG` varchar(45) DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='代码';


