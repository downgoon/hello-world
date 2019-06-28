# npm-install

## 快速安装

### 安装指定包

``` bash
$ npm install <package-name>
```

例如：

``` bash
$ npm install electron
```

这里我们没有指定具体的版本，则会下载对应仓库的最新版本。


#### ``--production`` 参数

>By default, ``npm install`` will install all modules listed as dependencies in ``package.json``.

>With the ``--production`` flag (or when the NODE_ENV environment variable is set to production), npm will not install modules listed in ``devDependencies``.


它类似``maven``，先下载``package-name``后，还会查看``package-name``这个包里面的``package.json``描述文件，里面有依赖关系描述节点，比如：``devDependencies``和``dependencies``，前者类似``maven``的依赖``<scope>provided</scope>``，后者类似``<scope>compile</scope>``。默认情况下，它会同时追踪``devDependencies``和``dependencies``的依赖包。如果不想追踪``devDependencies``，加个``--production``参数，例如：

``` bash
npm install electron --production
```

#### 指定具体版本

``` bash
$ npm install <package-name>@<version>
```

例如：

``` bash
$ npm install electron@1.6.11
```

#### ``--global`` 参数

>Install the dependencies in the **local** ``node_modules`` folder.

>In global mode (ie, with -g or --global appended to the command), it installs the current package context (ie, the current working directory) as a global package.

``npm``安装包的时候，分两种情况：局部安装和全局安装。

- 局部安装：安装包会写入当前子目录``node_modules``。
- 全局安装：会写入类似``/usr/local/lib/node_modules``的系统目录下。同时全局安装的，可以以命令行形式运行。比如``npm install electron``安装后，可以直接``electron -v``。

#### ``--save-dev`` 参数

如果我们写一个程序的时候，它依赖两个模块：``electron``和``electron-packager``。
在java中，我们需要在``maven``的``pom.xml``中，声明：

``` xml
<dependency>
		<groupId>org.example</groupId>
		<artifactId>electron</artifactId>
		<version>1.6.11</version>
</dependency>
<dependency>
		<groupId>org.example</groupId>
		<artifactId>electron-packager</artifactId>
		<version>8.7.1</version>
    <scope>provided</scope>
</dependency>
```

接着``mvn build``编译的的时候，会自动下载依赖包。同时``mvn package``的时候，只会把``electron``打包，``electron-packager``不会打包，因为它只是用于开发阶段（取决于``<scope>provided</scope>``设置）。

在``node``的``npm``中，大体意思是一样的。只不过不是先写``package.json``，再下载依赖；而是先下载依赖（``npm install electron@1.6.11 --save-prod`` ），然后自动加入``package.json``:

``` bash
npm install electron@1.6.11 --save-prod
npm install electron-packager@8.7.1 --save-dev
```

上述两个命令运行完后，``node_modules``子目录会多出两个模块，同时在``package.json``的依赖描述会多出它两：

``` json
"dependencies": {
    "electron": "~1.6.11",
},
"devDependencies": {
    "electron-packager": "^8.7.1"
}
```

## 版本标识符

细心的同学会发现，上述的``package.json``两种不同的版本写法：


``` json
"dependencies": {
    "electron": "~1.6.11",
},
"devDependencies": {
    "electron-packager": "^8.7.1"
}
```

版本标识符``~1.6.11``和``^8.7.1``有什么区别呢？

>版本标识符：``~1.6.11`` 表示“约等于”，并不严格要求是``1.6.11``版本，可以是``1.6.12``或者``1.6.9``等。``^8.7.1`` 表示“兼容版”，只要跟``8.7.1``兼容的都可以，至于什么表示兼容，目前尚未查到具体资料。

版本标识符还有其他，比如``>1.6.11``表示版本必须大于``1.6.11``，再比如``<=1.6.11``表示小于或等于``1.6.11``版本。



## 参考资料

- [npm install](https://docs.npmjs.com/cli/install)
- [package.json 文件说明](https://docs.npmjs.com/files/package.json)
