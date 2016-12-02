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
