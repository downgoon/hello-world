# hello-world for mongodb 2.7

You’ll learn how to:

* Install mongodb-linux-x86_64-2.7.0.tgz
* Start mongodb
* Shell client


## install and start mongodb-linux-x86_64-2.7.0.tgz

it's so easy to install mongodb on linux due to `Green Software`. only 3 steps: download, uncompress tgz, and run.

* download ``mongodb-linux-x86_64-2.7.0.tgz`` from [https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.7.0.tgz](https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.7.0.tgz)

* uncompress

``tar zxvf mongodb-linux-x86_64-2.7.0.tgz``
``cd mongodb-linux-x86_64-2.7.0``

* start

a simple way to start:  ``bin/mongod``

**NOTE: **  to run in background, execute ``bin/mongod &`` 

specify datapath (used to store data):  ``bin/mongod --dbpath ~/mongod-data``

specify listening port: ``bin/mongod  --dbpath=~/mongodb-data/ --port 27017``

specify log file: ``bin/mongod  --dbpath=~/mongodb-data/ --port 27017 --logpath=~/mongodb.log``

specify some modes: ``bin/mongod  --dbpath=~/mongodb-data/ --port 27017 --logpath=~/mongodb.log --logappend --logappend``
  
**NOTE**

mongodb-3.x is quite different from mongodb-2.x

## Shell client

connect to default database named *test*:  ``bin/mongo localhost:27017``

connnect to database you specify: ``bin/mongo localhost:27017/some-database``

connect to nothing: ``bin/mongo --nodb``, why nodb? just for writing js script.


