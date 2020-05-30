CREATE TABLE `account` (
  `cardNo` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
PRIMARY KEY (`cardNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `account`(`cardNo`, `name`, `money`) VALUES ('6029621011001', '韩梅梅 ', 11000);
INSERT INTO `account`(`cardNo`, `name`, `money`) VALUES ('6029621011000', '李大雷', 11000);
