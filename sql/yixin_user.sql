CREATE TABLE `user` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `user_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
    `password` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
    `nick_name` varchar(30) DEFAULT NULL COMMENT '昵称',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
    `avatar` varchar(100) DEFAULT NULL COMMENT '头像地址',
    `gmt_created` datetime NOT NULL,
    `gmt_modified` datetime NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;