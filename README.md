# hello-world for bash

## how to get the path of currently executing program

``` bash
echo 'self-name: '$0
echo "self-path: "$(cd `dirname $0`; pwd)
echo "curr-path: "$(pwd)
```
which one is the right ?

the second: ``echo "self-path: "$(cd `dirname $0`; pwd)``

![the path of currently executing program](https://cloud.githubusercontent.com/assets/23731186/20923692/b053f34e-bbe8-11e6-99fc-8544c35040dc.png)

suppose a script named ``start.sh`` placed in ``/some/path/appname/bin/start.sh``, how to get ``appname`` in shell ?

``` bash
apppath=$(cd `dirname $0`; cd ..;  pwd)
appname=`echo $apppath | awk -F "/" '{print $NF}'`
echo $appname
```
---
