# hello-world for docker


You’ll learn how to:

* how to run commons docker such as nginx, memcached

---


## run memcached

``` bash
docker search memcached
docker pull memcached
docker images

docker run --name memcached-master -d -p 11211:11211 memcached

# if you want to fix memory size, use -m; and listen on 18080
docker run --name memcached-master -d -p 18080:11211 -m 256m memcached

```
