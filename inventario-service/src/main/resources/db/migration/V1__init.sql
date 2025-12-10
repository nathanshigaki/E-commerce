CREATE TABLE `t_inventario`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `skucode` varchar(255) DEFAULT NULL,
    `quantidade` int(11) DEFAULT NULL,
    PRIMARY KEY(`id`)
);