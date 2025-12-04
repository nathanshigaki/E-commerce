CREATE TABLE `t_pedidos`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `numero_pedido` varchar(255) default NULL,
    `skucode` varchar(255),
    `preco` decimal(19,2),
    `quantidade` int(11),
    PRIMARY KEY(`id`)
);