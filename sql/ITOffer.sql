-- ================================================================================= --
-- 1. 求职者表 (tb_applicant)
-- ================================================================================= --
DROP TABLE IF EXISTS `tb_jobapply`;
DROP TABLE IF EXISTS `tb_resume_basicinfo`;
DROP TABLE IF EXISTS `tb_job`;
DROP TABLE IF EXISTS `tb_company`;
DROP TABLE IF EXISTS `tb_applicant`;

CREATE TABLE `tb_applicant`(
	`applicant_id` INT NOT NULL AUTO_INCREMENT,
	`applicant_email` VARCHAR(50) NOT NULL,
	`applicant_pwd` VARCHAR(50) NOT NULL,
	`applicant_registdate` DATETIME,
	PRIMARY KEY (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='求职者信息表';

-- 求职者信息表基础数据 (ID会自动生成)
INSERT INTO `tb_applicant`(`applicant_email`, `applicant_pwd`, `applicant_registdate`) VALUES('qst@itoffer.cn','123456','2011-01-01 13:14:20');

-- ================================================================================= --
-- 2. 简历基本信息表 (tb_resume_basicinfo)
-- ================================================================================= --
CREATE TABLE `tb_resume_basicinfo`(
	`basicinfo_id` INT NOT NULL AUTO_INCREMENT,
	`applicant_id` INT NOT NULL,
	`realname` VARCHAR(50) NOT NULL,
	-- 注意：MySQL 5.x 不真正支持CHECK约束, 使用 ENUM 是更好的选择
	`gender` ENUM('男','女') NOT NULL,
	`birthday` DATETIME,
	`current_loc` VARCHAR(255),
	`resident_loc` VARCHAR(255),
	`telephone` VARCHAR(50),
	`email` VARCHAR(50),
	`job_intension` VARCHAR(50),
	`job_experience` VARCHAR(255),
	`head_shot` VARCHAR(255),
	PRIMARY KEY (`basicinfo_id`),
	CONSTRAINT `fk_resume_basicinfo_applicant` FOREIGN KEY(`applicant_id`) REFERENCES `tb_applicant`(`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='简历基本信息表';

-- 简历基本信息测试数据 (ID会自动生成, 假设上面插入的求职者ID为1)
INSERT INTO `tb_resume_basicinfo`(`applicant_id`, `realname`, `gender`, `birthday`, `current_loc`, `resident_loc`, `telephone`, `email`, `job_intension`, `job_experience`, `head_shot`) VALUES(1,'张三','男','1993-11-05 00:00:00','山东省青岛市高新区','山东省青岛市','13166666666','qst@itoffer.cn','Java软件开发','刚刚参加工作','QST.jpg');

-- ================================================================================= --
-- 3. 公司表 (tb_company)
-- ================================================================================= --
CREATE TABLE `tb_company`(
	`company_id` INT NOT NULL AUTO_INCREMENT,
	`company_name` VARCHAR(50),
	`company_area` VARCHAR(50),
	`company_size` VARCHAR(50),
	`company_type` VARCHAR(50),
	-- VARCHAR2(2000) 转换为 TEXT 类型更合适
	`company_brief` TEXT,
	-- 注意：MySQL 5.x 不真正支持CHECK约束
	`company_state` INT COMMENT '状态: 1=招聘中, 2=已暂停, 3=已结束',
	`company_sort` INT,
	-- LONG 类型转换为 INT 更合适
	`company_viewnum` INT,
	`company_pic` VARCHAR(255),
	PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业信息表';

-- 企业信息表基础数据 (ID会自动生成)
INSERT INTO `tb_company`(`company_name`,`company_area`,`company_size`,`company_type`,`company_brief`,`company_state`,`company_sort`,`company_viewnum`,`company_pic`) VALUES('凌志软件股份有限公司','苏州市','1000人以上','股份制企业','凌志软件股份有限公司成立于2003年1月，是一家致力于为各行业提供软件产品研发，软件外包开发及IT系统集成服务。业务范围包括证券，银行，保险，电子商务，物流等。金融行业产品研发及软件外包服务的专业性及稳定性，已成为企业的核心竞争力。公司在日本东京、上海、北京、深圳等地均设有分支机构。凌志软件经过10年多的发展，得到现有海内外客户高度认可，并在中国和日本形成了一定的品牌知名度，海外业务以日本市场为核心，已成为国际知名企业的核心供应商，在2011年软件出口企业排行榜中名列第10名，并获得2011-2012年国家规划布局内重点软件企业称号。凌志软件在稳步扩大高端软件外包业务的同时，自主研发国内高端金融产品，现已申请多项发明专利并开发了多款拥有自主知识产权的金融软件产品，投入商业应用并得到客户的高度评价，逐步在国内市场上崭露头角。',1,1,1389,'635170123249913750.jpg');
INSERT INTO `tb_company`(`company_name`,`company_area`,`company_size`,`company_type`,`company_brief`,`company_state`,`company_sort`,`company_viewnum`,`company_pic`) VALUES('苏州大宇宙信息创造有限公司','苏州市','100-200人','外资企业','苏州大宇宙信息创造有限公司成立于2008年10月，是大宇宙信息创造（中国）有限公司全资子公司，注册资金为1600万元。公司位于风景优美的中国新加坡合作苏州工业园区独墅湖高教区，拥有自己的办公及研发大楼，是园区重点引进的软件服务外包企业。公司是一家专业从事国际和国内企事业信息化解决方案、软件外包的高科技企业，为国内外企业提供一流的软件开发、系统集成及维护、客户支持等综合的信息服务。公司拥有一支高素质的管理与开发团队，具有良好的外语能力和丰富的软件设计开发经验，同时具备与国内外客户的良好商务沟通能力。公司成立至今，保持着稳健发展的势头，事业日益发展和壮大，目前已与国内外多家企业建立了长期稳定的客户关系。公司于2009年7月份顺利通过ISO27001信息安全管理的国际认证，2010年6月顺利通过CMMI3级认证。公司具备完善的管理、教育培训和薪酬福利体系以及健全的规章制度，为员工的工作和学习提供了广阔、自由的发展空间。',1,2,577,'635508802169230812.jpg');
INSERT INTO `tb_company`(`company_name`,`company_area`,`company_size`,`company_type`,`company_brief`,`company_state`,`company_sort`,`company_viewnum`,`company_pic`) VALUES('北京日立华胜信息系统有限公司','东城区','200-500人','合资企业','北京日立华胜信息系统有限公司（简称BHH）是世界五百强之一的HITACHI日立集团和信息产业部电子六所共同投资设立的高新技术企业。公司主要从事对日软件开发,自成立以来，我们承接了日本各大银行?证券交易所相关系统、日本新干线铁路座位预约系统、面向日本政府机关的财务会计系统/税金管理系统/居民信息管理系统、纳税系统、生产管理系统、销售管理系统、日本各大汽车厂商的ECU软件、信息终端设备软件等各种大型软件开发项目。业务领域涵盖：金融、产业/流通、公共政府、ATM以及嵌入式五大领域。从1996年起，公司便已经开始从事汽车引擎控制、变速器控制、自动巡航控制等领域的嵌入式软件开发，积累了丰富的嵌入式软件的开发经验。公司十分注重对员工的外语及业务技能培训，提供多次出国工作机会和充分的发展空间；公司员工均享有良好的薪资和完备的福利保险待遇（“五险一金”和补充医疗/意外伤害保险，以及多项补贴）。诚挚邀请有志于从事对日软件开发、德才兼备的毕业生加盟，开辟属于自己的崭新生活。欢迎各位有识之士的加盟。',1,3,1183,'635086129655240312.jpg');

-- ================================================================================= --
-- 4. 招聘职位信息表 (tb_job)
-- ================================================================================= --
CREATE TABLE `tb_job`(
	`job_id` INT NOT NULL AUTO_INCREMENT,
	`company_id` INT NOT NULL,
	`job_name` VARCHAR(100),
	`job_hiringnum` INT,
	`job_salary` VARCHAR(20),
	`job_area` VARCHAR(255),
	`job_desc` VARCHAR(255),
	`job_endtime` DATETIME,
	-- 注意：MySQL 5.x 不真正支持CHECK约束
	`job_state` INT COMMENT '状态: 1=招聘中, 2=已暂停, 3=已结束',
	PRIMARY KEY (`job_id`),
	CONSTRAINT `fk_job_company` FOREIGN KEY (`company_id`) REFERENCES `tb_company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='招聘职位信息表';

-- 招聘职位信息表基础数据 (ID会自动生成, company_id 对应上面插入的公司)
INSERT INTO `tb_job`(`company_id`, `job_name`, `job_hiringnum`, `job_salary`, `job_area`, `job_desc`, `job_endtime`, `job_state`) VALUES (1,'对日软件开发工程师（提供岗前培训）',100,'2500~4000元/月','吴中区','担任设计书的制作、程序开发、测试实施等工作。', '2016-03-05 00:00:00',1);
INSERT INTO `tb_job`(`company_id`, `job_name`, `job_hiringnum`, `job_salary`, `job_area`, `job_desc`, `job_endtime`, `job_state`) VALUES (2,'Java软件开发工程师（提供岗前培训）',20,'2500~4000元/月','工业园区','针对客户现场软件问题提供技术支持，包括在研发实验室重现客户软件问题，分析代码问题原因，提供解决方案，并测试更新的代码符合客户要求', '2016-03-05 00:00:00',1);
INSERT INTO `tb_job`(`company_id`, `job_name`, `job_hiringnum`, `job_salary`, `job_area`, `job_desc`, `job_endtime`, `job_state`) VALUES (3,'对日软件开发（提供岗前培训）',40,'4万-4.5万/年','历城区','J2EE开发 TOMCAT/JBOSS等主流应用服务器 Webservice、SOCKET、SNMP等标准接口和协议,Struts2、Spring、Hibernate等常用框架 Linux操作系统及oracle。', '2016-03-05 00:00:00',1);

-- ================================================================================= --
-- 5. 职位申请信息表 (tb_jobapply)
-- ================================================================================= --
CREATE TABLE `tb_jobapply`(
	`apply_id` INT NOT NULL AUTO_INCREMENT,
	`job_id` INT NOT NULL,
	`applicant_id` INT NOT NULL,
	`apply_date` DATETIME,
	`apply_state` INT,
	PRIMARY KEY (`apply_id`),
	CONSTRAINT `fk_jobapply_job` FOREIGN KEY (`job_id`) REFERENCES `tb_job` (`job_id`),
  CONSTRAINT `fk_jobapply_applicant` FOREIGN KEY (`applicant_id`) REFERENCES `tb_applicant` (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='职位申请信息表';

-- 职位申请信息表基础数据 (ID会自动生成, job_id 和 applicant_id 对应上面插入的数据)
INSERT INTO `tb_jobapply`(`job_id`, `applicant_id`, `apply_date`, `apply_state`) VALUES (1,1,'2015-03-05 00:00:00',1);