# hello-world for mysql

## connect to mysqld

``` mysql
➜  ~ mysql -h localhost -P 3306 -u root -p123456
Warning: Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 4
Server version: 5.6.25 Homebrew

Copyright (c) 2000, 2015, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>

```

## grant

``` mysql
grant all privileges on kafka_capture.* to dev@'%' identified by '123456';
flush privileges;
mysql -h localhost -P 3306 -u dev -p123456 kafka_capture;
```

## create table

``` mysql
drop database if exists passport;
create database passport;
use passport;

DROP TABLE IF EXISTS `user`;
create table `user`
(
	id bigint not null auto_increment,
	type tinyint not null,
	parent bigint null,
	ip varchar(128) null,
	createtime bigint not null,
	lastmodifytime bigint null,
	status tinyint not null,
	primary key(id)
) Engine=InnoDb;

DROP TABLE IF EXISTS `partner`;
create table `partner`
(
	id bigint not null auto_increment,
	userid bigint not null,
	psrc tinyint not null,
	puserid varchar(32) not null,
	nick varchar(64) null,
	headface varchar(255) null,
	gender tinyint null,
	token varchar(128) null,
	tokensecret varchar(128) null,
	primary key(id)
) Engine=InnoDb;

DROP TABLE IF EXISTS `account`;
create table `account`
(
	id bigint not null auto_increment,
	userid bigint not null,
	username varchar(32) null,
	email varchar(64) null,
	mobile varchar(16) null,
	password varchar(128) null,
	primary key(id)
) Engine=InnoDb;

```
