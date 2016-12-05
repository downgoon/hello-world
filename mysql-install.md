# mysqld Linux 安装

mysqld的安装，有三种安装方式：
* 绿色安装：预编译，.tar.gz形式。
* RPM包安装：预编译，RPM包形式。
* 编译安装：下载源代码，并编译。

版本目前推荐 5.5，用的最广泛。

## 绿色安装（.tar.gz 形式）

如果您不想了解过程，请直接看后面的小结。

### 下载

官方下载太慢(安装包180M左右)，而且需要登陆，现提供国内sohu镜像下载：
[mysql-5.5.52-linux2.6-x86_64.tar.gz](http://mirrors.sohu.com/mysql/MySQL-5.5/mysql-5.5.52-linux2.6-x86_64.tar.gz)

其他版本参考： [http://mirrors.sohu.com/mysql/MySQL-5.5/](http://mirrors.sohu.com/mysql/MySQL-5.5/)

安装平台说明：linux2.6 内核； x86机器，64位机； mysqld 5.5 版本。

### 解压
``tar zxvf mysql-5.5.52-linux2.6-x86_64.tar.gz``
目录结构：

| 目录 | 含义 |
|------|-----------------------|
|bin | Client programs and the mysqld server |
|data| Log files, databases |
| scripts | mysql_install_db **初始化脚本** |
|sql-bench| 性能测试工具 |
| lib | 依赖的 lib 库 |
| 其他 | 其他目录 |

### 安装

* **添加mysql 账户**

```
groupadd mysql
useradd -r -g mysql mysql
```

* **赋权mysql 账户** 

假设解压后的目录为``/var/wd/mysql-5.5.52-linux2.6-x86_64/``，默认情况下 子目录 ``data`` 将来就是存放数据库数据和日志的。

mysql服务器运行需要使用mysql账户，并且期间需要**读写**``data``目录及其文件，因此把``data``目录及其子目录赋权给mysql账户。

```
chown -R mysql /var/wd/mysql-5.5.52-linux2.6-x86_64/
chgrp -R mysql /var/wd/mysql-5.5.52-linux2.6-x86_64/
```

**注意**
- useradd -r 参数表示mysql用户是系统用户，不可用于登录系统。
- 如果想把``data``存放在其他目录，那么``chown``时，就得指定其他目录。

**附言**
- **chown** change owner 改变文件或目录的所属“用户”；
- **chgrp** change group 改变文件或目录的所属“用户组”；
- **合并** 同时改变“用户和用户组”。
```
chown -R mysql /var/wd/mysql-5.5.52-linux2.6-x86_64/
chgrp -R mysql /var/wd/mysql-5.5.52-linux2.6-x86_64/
```
等效于：
```
chown -R mysql:mysql /var/wd/mysql-5.5.52-linux2.6-x86_64/
```
- 参数 -R 表示“递归”，修改递归到子目录。

```
[root@CDCS-213057166 demo]# ll  // 三个文件属于root:root
-rw-r--r-- 1 root root    0 Dec  5 15:38 g.txt
-rw-r--r-- 1 root root    0 Dec  5 15:38 ug.txt
-rw-r--r-- 1 root root    0 Dec  5 15:38 u.txt
[root@CDCS-213057166 demo]# chown mysql u.txt // 只变“用户”
[root@CDCS-213057166 demo]# chgrp mysql g.txt // 只变“用户组”
[root@CDCS-213057166 demo]# chown mysql:mysql ug.txt // 同时改变“用户”和“用户组”
[root@CDCS-213057166 demo]# ll // 验证如预期
-rw-r--r-- 1 root  mysql    0 Dec  5 15:38 g.txt
-rw-r--r-- 1 mysql mysql    0 Dec  5 15:38 ug.txt
-rw-r--r-- 1 mysql root     0 Dec  5 15:38 u.txt
```

-R 递归改变用户和用户组

```
# tree sub   // 构建子目录
sub
├── d
│   ├── sub_sub_dir.txt
│   └── sub_sub_file.txt
└── f.txt

demo] # ll    // sub目录 本身属于 root:root
drwxr-xr-x 3 root root 4096 Dec  5 15:39 sub
demo]# ll sub  // sub目录下的文件和子目录属于 root:root
drwxr-xr-x 3 root root 4096 Dec  5 15:39 d
-rw-r--r-- 1 root root    0 Dec  5 15:39 f.txt

demo]# chown mysql:mysql sub   // 非递归模式
demo]# ll   // sub 目录本身 变为  mysql:mysql
drwxr-xr-x 3 mysql mysql 4096 Dec  5 15:39 sub
[root@CDCS-213057166 demo]# ll sub // sub 目录下文件和子目录依然是 root:root
drwxr-xr-x 3 root root 4096 Dec  5 15:39 d
-rw-r--r-- 1 root root    0 Dec  5 15:39 f.txt
[root@CDCS-213057166 demo]# chown -R mysql:mysql sub // 递归模式
demo]# ll sub/d    // 全部（包括子目录的子目录）变为 mysql:mysql
drwxr-xr-x 2 mysql mysql 4096 Dec  5 15:39 sub_sub_dir.txt
-rw-r--r-- 1 mysql mysql    0 Dec  5 15:39 sub_sub_file.txt
```

### 初始化

``` bash
cd /var/wd/mysql-5.5.52-linux2.6-x86_64/
scripts/mysql_install_db --user=mysql --basedir=/var/wd/mysql-5.5.52-linux2.6-x86_64 --datadir=/var/wd/mysql-5.5.52-linux2.6-x86_64/data
```

执行初始化后，在``$datadir``会多出 *mysql* 和 *performance_schema* 子目录。

```
[root@CDCS-213057166 data]# pwd
/var/wd/mysql-5.5.52-linux2.6-x86_64/data
[root@CDCS-213057166 data]# ll
total 12
drwx------ 2 mysql root  4096 Dec  5 13:46 mysql
drwx------ 2 mysql mysql 4096 Dec  5 13:46 performance_schema
drwxr-xr-x 2 mysql mysql 4096 Dec  5 11:09 test
```

### 启动

```
cd /var/wd/mysql-5.5.52-linux2.6-x86_64
cp support-files/my-medium.cnf ./my.cnf
bin/mysqld_safe --defaults-file=my.cnf &
```

这里启动用的是*mysqld_safe* 脚本，官方解释：
>A server startup script. mysqld_safe attempts to start mysqld (The SQL daemon , that is, the MySQL server ). 

它是一个bash脚本，最终会调用 *mysqld* 。

参数 ``--defaults-file=my.cnf`` 是指定配置文件。在 ``support-files`` 子目录下，默认提供了3个配置文件（ ``my-small.cnf``,  ``my-medium.cnf`` 和 ``my-large.cnf``），供不同应用场景（欲知详情，文件里都有注释）。

检查 **mysqld** 是否启动：

```
# ps aux | grep mysqld
root     29628  0.0  0.0 106232  1408 pts/1    S    13:56   0:00 /bin/sh bin/mysqld_safe --defaults-file=my.cnf
mysql    29871  0.1  0.5 485256 43980 pts/1    Sl   13:56   0:00 /var/wd/mysql-5.5.52-linux2.6-x86_64/bin/mysqld --defaults-file=my.cnf --basedir=/var/wd/mysql-5.5.52-linux2.6-x86_64 --datadir=/var/wd/mysql-5.5.52-linux2.6-x86_64/data --plugin-dir=/var/wd/mysql-5.5.52-linux2.6-x86_64/lib/plugin --user=mysql --log-error=/var/wd/mysql-5.5.52-linux2.6-x86_64/data/CDCS-213057166.err --pid-file=/var/wd/mysql-5.5.52-linux2.6-x86_64/data/CDCS-213057166.pid --socket=/tmp/mysql.sock --port=3306

# ss -lpn | grep 3306
LISTEN     0      50                        *:3306                     *:*      users:(("mysqld",29871,11))
```

默认情况下，**mysqld** 监听 3306 端口。还可以查看下启动日志：

```
# tail -7f data/*.err
161205 13:56:44 InnoDB: 5.5.52 started; log sequence number 0
161205 13:56:44 [Note] Server hostname (bind-address): '0.0.0.0'; port: 3306
161205 13:56:44 [Note]   - '0.0.0.0' resolves to '0.0.0.0';
161205 13:56:44 [Note] Server socket created on IP: '0.0.0.0'.
161205 13:56:44 [Note] Event Scheduler: Loaded 0 events
161205 13:56:44 [Note] /var/wd/mysql-5.5.52-linux2.6-x86_64/bin/mysqld: ready for connections.
Version: '5.5.52-log'  socket: '/tmp/mysql.sock'  port: 3306  MySQL Community Server (GPL)
```

顺便说一下，关于启动的4个程序：

| 程序 | 介绍 |
|--------|--------|
| bin/mysqld | server 二进制文件 |
| bin/mysqld_safe | 启动server的脚本，bash脚本 |
| bin/mysqld_multi | 同一个机器启动多个server实例的脚本，perl脚本 |
| support-files/mysql.server | 也是启动脚本，bash脚本 |


```
mysql-5.5.52-linux2.6-x86_64] # file bin/mysqld
bin/mysqld: ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.4.0, not stripped
mysql-5.5.52-linux2.6-x86_64] # file bin/mysqld_safe
bin/mysqld_safe: POSIX shell script text executable
mysql-5.5.52-linux2.6-x86_64] # file bin/mysqld_multi
bin/mysqld_multi: a /usr/bin/perl script text executable
mysql-5.5.52-linux2.6-x86_64] # file support-files/mysql.server
support-files/mysql.server: POSIX shell script text executable
```

既然有4个启动程序，为什么用 ``mysqld_safe``，而不直接用``mysqld``呢？顾名思义，前者更 *safe* ，安全靠谱。官方这么解释的：
>mysqld_safe is the **recommended** way to start a mysqld server on Unix and NetWare. mysqld_safe adds some safety features such as **restarting the server when an error occurs** and **logging runtime information to an error log file**.

>概括起来两点：
- 当系统出现错误时，mysqld_safe 会自动重启（自带watchdog机制）。
- 运行出错，会记录到error log里面（文件在哪? --log-error 指定的）。


更加详细情况，请参考 [官方文档 5.5](http://dev.mysql.com/doc/refman/5.5/en/mysqld-safe.html)。

### 访问

在 mysqld 本地机器上执行：
```
# cd /var/wd/mysql-5.5.52-linux2.6-x86_64
# bin/mysql
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 1
Server version: 5.5.52-log MySQL Community Server (GPL)

Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| test               |
+--------------------+
4 rows in set (0.00 sec)
```


在 *mysqld* 所在的机器，执行 *mysql*  无需密码，即可登录。 ``mysql -h localhost -P 3306 -u root -p``, 提示输入密码时候，直接回车（无需密码）就可以进入。但是为了保护数据库信息安全，root@localhost只能从本机访问，无法从远程客户端访问的。

为了外网访问，新建一个账号``devacc``，密码为``swl123``，允许从任何源主机访问。

```
# mysql -h localhost -u root -p
Enter password:  （直接回车，无需密码）
Welcome to the MySQL monitor.  Commands end with ; or \g.

mysql> grant all privileges on *.* to devacc@'%' identified by 'swl123';
Query OK, 0 rows affected (0.00 sec)

mysql> show grants for devacc;
+----------------------------------------------------------------------------------------------------------------+
| Grants for devacc@%                                                                                            |
+----------------------------------------------------------------------------------------------------------------+
| GRANT ALL PRIVILEGES ON *.* TO 'devacc'@'%' IDENTIFIED BY PASSWORD '*2F2E4079A9226AEF10B1C665B3DE101EB380D03F' |
+----------------------------------------------------------------------------------------------------------------+
1 row in set (0.00 sec)
```

文中授权命令 ``grant all privileges on *.* to devacc@'%' identified by 'swl123';`` 表示授予账户来自``%``（符号%表示任意源IP）的``devacc``用密码``swl123``，的所有权限（all privileges）。``grant``命令，**对于不存在的用户，会自动创建**。

另外，通过``grant``命令，创建用户并授权的，并不需要执行``flush privileges;``就可以生效。只有``insert into mysql.user(Host,User,Password) values("%","devacc",password("swl123"));``方式添加用户并授权的才需要``flush privileges;``。

从远程登录：
```
~ mysql -h 10.213.57.166 -P 10006 -u devacc -pswl123

mysql>
```

**附言**

- 重置密码
```
mysql> SET PASSWORD FOR 'devacc'@'%' = PASSWORD("123swl");
Query OK, 0 rows affected (0.00 sec)
```

当然本地还可以用 *sock* 方式访问：

```
# mysql --socket=/tmp/mysql.sock
-bash: mysql: command not found
[root@CDCS-213057166 mysql-5.5.52-linux2.6-x86_64]# bin/mysql --socket=/tmp/mysql.sock
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 2
Server version: 5.5.52-log MySQL Community Server (GPL)

Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```

### 停止

```
# bin/mysqladmin  --socket=/tmp/mysql.sock shutdown
161205 15:17:43 mysqld_safe mysqld from pid file /var/wd/mysql-5.5.52-linux2.6-x86_64/data/CDCS-213057166.pid ended
[1]+  Done                    bin/mysqld_safe --defaults-file=my.cnf
```

查看停止日志：

```
# tail -10f data/*.err
161205 13:56:44 [Note] /var/wd/mysql-5.5.52-linux2.6-x86_64/bin/mysqld: ready for connections.
Version: '5.5.52-log'  socket: '/tmp/mysql.sock'  port: 3306  MySQL Community Server (GPL)
161205 15:17:42 [Note] /var/wd/mysql-5.5.52-linux2.6-x86_64/bin/mysqld: Normal shutdown

161205 15:17:42 [Note] Event Scheduler: Purging the queue. 0 events
161205 15:17:42  InnoDB: Starting shutdown...
161205 15:17:43  InnoDB: Shutdown completed; log sequence number 1595675
161205 15:17:43 [Note] /var/wd/mysql-5.5.52-linux2.6-x86_64/bin/mysqld: Shutdown complete

161205 15:17:43 mysqld_safe mysqld from pid file /var/wd/mysql-5.5.52-linux2.6-x86_64/data/CDCS-213057166.pid ended
```

### 安装总结

- 下载
 ``wget http://mirrors.sohu.com/mysql/MySQL-5.5/mysql-5.5.52-linux2.6-x86_64.tar.gz``

- 解压
``tar zxvf mysql-5.5.52-linux2.6-x86_64.tar.gz``

- 初始化
``cd mysql-5.5.52-linux2.6-x86_64``
 - 准备以mysql:mysql运行
```
groupadd mysql
useradd -r -g mysql mysql
```
 - data目录赋权mysql:mysql
```
chown -R mysql:mysql .
```
 - 生成系统表
```
scripts/mysql_install_db --user=mysql --basedir=./ --datadir=./data
```
- 启动
```
bin/mysqld_safe --defaults-file=support-files/my-medium.cnf &
```
- 授权外网访问（包含创建用户和授权，无需flush）
```
grant all privileges on *.* to devacc@'%' identified by 'swl123';
```
- 停止
```
bin/mysqladmin  --socket=/tmp/mysql.sock shutdown
```

**注意事项**

1. 如果创建一个软链指向安装目录，即便对软链也执行``chown -R mysql:mysql mysqld`` （其中mysqld是软链），通过软链进入并如下执行：

```
lrwxrwxrwx  1 mysql mysql    36 Dec  5 16:39 mysqld -> /var/wd/mysql-5.5.52-linux2.6-x86_64
$ cd mysqld
$ bin/mysqld_safe --defaults-file=support-files/my-medium.cnf &
```
居然会报生成PID文件时候，没有权限。

```
mysqld]# tail -3f data/*.err
161205 17:00:20 [ERROR] /root/mysqld/bin/mysqld: Can't create/write to file '/root/mysqld/data/CDCS-213057166.pid' (Errcode: 13)
161205 17:00:20 [ERROR] Can't start server: can't create PID file: Permission denied
161205 17:00:20 mysqld_safe mysqld from pid file /root/mysqld/data/CDCS-213057166.pid ended
```

---
