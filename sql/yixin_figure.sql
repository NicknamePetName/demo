CREATE TABLE `figure_number` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `figure_number` varchar(255) NOT NULL COMMENT '图号名',
  `figure_page_id` int DEFAULT NULL COMMENT '图纸id',
  `ply` VARCHAR(20) DEFAULT NULL COMMENT '厚度(mm)',
  `width` VARCHAR(20) DEFAULT NULL COMMENT '宽度(mm)',
  `length` VARCHAR(20) DEFAULT NULL COMMENT '长度(mm)',
  `gmt_created` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='图号表';


CREATE TABLE `figure_page` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL COMMENT '图纸文件名',
  `file_path` varchar(255) NOT NULL COMMENT '图纸Minio路径',
  `gmt_created` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='图纸表';