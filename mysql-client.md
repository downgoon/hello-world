# install mysql-client (official client)

download from here [MySQL-client-5.5.54-1.linux2.6.x86_64.rpm](http://mirrors.sohu.com/mysql/MySQL-5.5/MySQL-client-5.5.54-1.linux2.6.x86_64.rpm)

install   ``rpm -ivh MySQL-client-5.5.54-1.linux2.6.x86_64.rpm``

connect

```
mysql -h 10.209.44.12 -P 10043 -u memcloud -pmemcloud memcloud
```

# another nice mysql-client named **[mycli](https://github.com/dbcli/mycli)**

install

```
$ brew install mycli
```

connect

```
mycli -h 10.209.44.12 -P 10043 -u memcloud -pmemcloud memcloud
```
