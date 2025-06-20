-- ================================================================================= --
-- 6. 后台管理用户信息表 (tb_users)
-- ================================================================================= --
DROP TABLE IF EXISTS `tb_users`;

CREATE TABLE `tb_users`(
	`user_id` INT NOT NULL AUTO_INCREMENT,
	`user_logname` VARCHAR(50) NOT NULL,
	`user_pwd` VARCHAR(50) NOT NULL,
	`user_realname` VARCHAR(50) NOT NULL,
	`user_email` VARCHAR(50) NOT NULL,
	`user_role` INT NOT NULL COMMENT '用户角色, e.g., 3=Admin',
	`user_state` INT NOT NULL COMMENT '用户状态, e.g., 1=Active',
	PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_user_logname` (`user_logname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理用户信息表';

-- 用户信息表基础数据 (ID会自动生成)
-- 为了提高效率，将多个INSERT合并为一个
INSERT INTO `tb_users` (`user_logname`, `user_pwd`, `user_realname`, `user_email`, `user_role`, `user_state`) VALUES
('admin','123456','青软实训','admin@test.com',3,1),
('fengjj','123456','冯娟娟','fengjj@test.com',3,1),
('test','123456','test','test@test.com',3,1);