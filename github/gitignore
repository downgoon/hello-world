# gitignore 

##内容提要

- 忽略文件
- 忽略目录的四种不同方式
 * ``/mytmp`` 
 * ``/mytmp/*`` 
 * ``**/mytmp`` 
 * ``**/mytmp/*``
- 例外
 * 用 ``!`` 表示例外；
 * 作用于``/mytmp/*``，不能作用于``/mytmp``
- ignore 生效的前提
- 通配符语法
 * 一个星表示任意字符；
 * 两个星表示**任意路径**。
- 两种形式：共享式和独享式
- 官方模板

## 语法

当我们进行代码开发时，把代码存到远程代码库上。但是总有一些代码是不需要上传的，比如编译的中间文件，单元测试自动生成的测试报告等。这个时候我们需要告诉``git``，哪些文件应该忽略，``git``提供了这种机制，它通过``.gitignore`` 配置文件来实现。通常该文件放在项目的根目录下，我们可以手动创建它，然后编辑内容。

### 忽略文件
在 ``.gitignore``文件中编辑：
```
#.gitignore for java
*.class
```

第一行以``#``开头的是注释，``*.class`` 表示忽略“**所有**”以``.class``为后缀的文件（其中``*``号表示glob模式匹配的通配符）。这里的“所有”无论它在哪个目录下。

实验验证下，创建多级子目录，每个目录创建一个``.class``文件，结构如下：
```
➜  demo-gitignore git:(master) tree
.
├── L1.class
└── child1
    ├── L2.class
    └── child2
        ├── L3.class
        └── child3
            └── L4.class

3 directories, 4 files
```
执行``git status``，看看有没有被忽略？

```
➜  demo-gitignore git:(master) git status
On branch master
Initial commit
nothing to commit (create/copy files and use "git add" to track)
```

当然也可以不用通配符，例如
```
# project specified gitignore
Hello.xml
```
表示忽略“所有”名字叫``Hello.xml``的文件。

###忽略目录

语法上，以``/``开头的表示忽略目录。比如``/mytmp``表示忽略“**根目录下**”名叫``mytmp``的目录，并非表示“**所有**”。

在上述3个childX目录下，各自创建一个``mytmp``子目录（实验时请勿用``tmp``，以免用户目录下的``~/.gitignore``已经配置过忽略``tmp``），并在每个``mytmp``目录下创建``Hello.xml``文件（**因为如果没有文件，git不会理会空目录的**）。

形如：
```
demo-gitignore
├── child1
│   ├── child2
│   │   ├── child3
│   │   │   └── mytmp
│   │   │       └── Hello.xml
│   │   └── mytmp
│   │       └── Hello.xml
│   └── mytmp
│       └── Hello.xml
└── mytmp
    └── Hello.xml

➜  demo-gitignore git:(master) ✗ git status -s
?? child1/
?? mytmp/
```
在``.gitignore``中添加``/mytmp``忽略后，再看status：
```
➜  demo-gitignore git:(master) ✗ git status -s
 M .gitignore
?? child1/
```
首先发现``?? mytmp/``已经不见了（被忽略了）。第一行.gitignore的变化是因为刚添加``/mytmp``，尚未提交；第二行``?? child1/``为什么还在？因为我们只是忽略了``/mytmp``目录，并**没有忽略其下的文件Hello.xml**？其实是只忽略根目录下的``/mytmp``，子目录下的``/mytmp``并不被忽略。

```
➜  demo-gitignore git:(master) ✗ git add child1
➜  demo-gitignore git:(master) ✗ git status -s
 M .gitignore
A  child1/child2/child3/mytmp/Hello.xml
A  child1/child2/mytmp/Hello.xml
A  child1/mytmp/Hello.xml
```

上述唯独没有提到根目录``demo-gitignore``下的``mytmp``目录。如果要让所有目录下的``mytmp``目录都被忽略呢？ 前缀加两个``*``号（即：``**``）。

```
# project specified gitignore
**/mytmp
```
此时``mytmp``，都不再显示，无论是哪级子目录：
```
➜  demo-gitignore git:(master) ✗ git status -s
 M .gitignore
```

如果我们要“排除（不忽略）”  ``/child1/child2/mytmp`` 目录呢？
用``!/child1/child2/mytmp``排除。

```
# project specified gitignore
**/mytmp
!/child1/child2/mytmp
```

结果验证如下：
```
➜  demo-gitignore git:(master) ✗ git status -s
 M .gitignore
A  child1/child2/mytmp/Hello.xml
```

**总结备忘**
>以``/``开头忽略目录，表示当前。例如``/mytmp``表示忽略根目录下的mytmp。
>以``**/``开头，忽略所有目录。例如``**/mytmp``表示忽略所有层级下的mytmp目录。
>用``!``开头表示例外。例如``!/child1/child2/mytmp``表示单独强调“不忽略”/child1/child2/mytmp的 mytmp 目录。

### 忽略的例外

如前文所说，例外用``!``表示。这里补充下关于“文件”的例外。在上述的实验环境中，新创建文件 ``demo-gitignore/mytmp/HelloExpectional.xml``，并配置``.gitignore``如下：

```
# project specified gitignore
/mytmp/*
!/mytmp/HelloExpectional.xml
```
它表示忽略根目录/下的mytmp子目录下的**所有文件（星号表示）**，但是``/mytmp/HelloExpectional.xml``文件例外（不忽略）。

```
➜  demo-gitignore git:(master) ✗ git add .
➜  demo-gitignore git:(master) ✗ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

	modified:   .gitignore
	new file:   child1/child2/child3/mytmp/Hello.xml
	new file:   child1/child2/mytmp/Hello.xml
	new file:   child1/mytmp/Hello.xml
	new file:   mytmp/HelloExpectional.xml
```

如上结果，只有mytmp/Hello.xml被忽略。如预期。如果要所有mytmp呢？用``**/tmp``呀。

### 为什么ignore 没生效？

紧接着上面，把``.gitignore``内容修改为：
```
# project specified gitignore
**/mytmp/*
!/mytmp/HelloExpectional.xml
```
查看status，发现并没有变化？
```
➜  demo-gitignore git:(master) ✗ git status -s
MM .gitignore
A  child1/child2/child3/mytmp/Hello.xml
A  child1/child2/mytmp/Hello.xml
A  child1/mytmp/Hello.xml
A  mytmp/HelloExpectional.xml
```
预期应该是只有``mytmp/HelloExceptional.xml``不被忽略，其他均被忽略。新配置为什么没生效？因为前文``git add .``的时候，已经加入git索引了，gitignore只能对``untracked``状态的资源起作用。

先把他们从``tracked (to be committed)`` 中撤掉：

```
➜  demo-gitignore git:(master) ✗ git rm --cached -r child1
rm 'child1/child2/child3/mytmp/Hello.xml'
rm 'child1/child2/mytmp/Hello.xml'
rm 'child1/mytmp/Hello.xml'
➜  demo-gitignore git:(master) ✗ git rm --cached -r mytmp
rm 'mytmp/HelloExpectional.xml'
➜  demo-gitignore git:(master) ✗
```

命令解释如下：
> ``git rm --cached`` 表示直接删除“索引区”的内容（不是导出到Working dir，也不是提交到版本库）。后面接文件，表示操作对象；``-r ``是当操作对象为目录时，表示递归。

接着实验看看新的ignore规则：
```
➜  demo-gitignore git:(master) ✗ git add .
➜  demo-gitignore git:(master) ✗ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

	modified:   .gitignore
	new file:   mytmp/HelloExpectional.xml
```

###目录忽略，它的子目录和文件呢？

当我们忽略一个目录时，它下面的子目录和文件也一起被忽略吗？

在``demo-gitignore/mytmp``创建一级子目录``son-of-mytmp``和二级子目录``grandson-of-mytmp``，并各自放一个文件，如下结构：
```
➜  demo-gitignore git:(master) ✗ tree mytmp
mytmp
├── Hello.xml
├── HelloExpectional.xml
└── son-of-mytmp
    ├── grandson-of-mytmp
    │   └── grandson.xml
    └── son.xml

2 directories, 4 files
```

ignore配置：
```
# project specified gitignore
/mytmp
!/mytmp/HelloExpectional.xml
```

查看状态：
```
➜  demo-gitignore git:(master) ✗ git status -s
 M .gitignore
?? child1/
```

的的确确 根目录下的mytmp目录及其子目录，都被忽略了。但与此同时奇怪的是``!/mytmp/HelloExpectional.xml``“例外设置”并没有生效？

如果调整 ignore 设置：
```
# project specified gitignore
/mytmp/*
!/mytmp/HelloExpectional.xml
```
从``/mytmp``调整为``/mytmp/*``，结果例外生效了。

```
➜  demo-gitignore git:(master) ✗ git add .
➜  demo-gitignore git:(master) ✗ git status -s
M  .gitignore
A  child1/child2/child3/mytmp/Hello.xml
A  child1/child2/mytmp/Hello.xml
A  child1/mytmp/Hello.xml
A  mytmp/HelloExpectional.xml
```

**总结备忘**
>忽略目录``/mytmp``和``/mytmp/*``，都会递归影响其子目录和文件的忽略。
>只有``/mytmp/*``忽略，才能添加形如``!/mytmp/HelloExpectional.xml``的例外。

###glob模式语法

所谓“glob模式”就是我们常见的bash下简化的正则表达式。就4招：
- 星号 ``*``，通配多个字符；
- 两个星号``**``，表示**任意中间层目录**。例如``a/**/z`` 可以匹配目录``a/z``, ``a/b/z``或``a/b/c/z``等。
- 问号``?``，通配单个字符；
- 方号``[]``，枚举单个字符。例如``[abc]``表示要么``a``，要么``b``，要么``c``，但是``ab``两个字符是不能匹配的，只能是1个。
- 范围``[0-9]``或``[a-z]`` 表示任意一个数字或字母。
- 叹号``!``，表示“取反”，表示“不忽略”的语义。


## 使用习惯

### 基本概念

- ``.gitignore`` 文件是项目根目录下的一个隐藏文件，不是``.git``子目录下的。
- ``.gitignore`` 文件对其所在的目录及其全部子目录均有效。当然用户级HOME目录下``~/.gitignore``文件全局有效，项目的ignore继承覆盖用户级的。
- 配置文件``.gitignore``本身需要加入版本库，以便其他组员能共享同一套资源忽略管理规则。

```
➜  demo-gitignore git:(master) ✗ touch .gitignore
➜  demo-gitignore git:(master) ✗ git status -s
?? .gitignore
➜  demo-gitignore git:(master) ✗ git add .gitignore
➜  demo-gitignore git:(master) ✗ git commit .gitignore -m 'create project specified gitignore conf'
```

### 共享式 与 独享式

ignore 规则既可以选择“共享式”让全组员使用同样的规则（文件位置是项目根目录下的``.gitignore``文件），好处是大家的配置一样，不好是``.gitignore``内容太多，维护太累。也可以选择“独享式”，只对自己生效，其他组员看不到（因为都不上传到版本库）。“独享式”有两种形式：
- 用户级的  位置在``~/.gitignore`` 用户HOME目录下；
- 项目级的 位置在``.git/info/exclude``，它也是一个ignore文件，语法规则是一样的。注意：尽管``.git``目录一定是要上传到版本库的（它就是版本库本身），但是却留下了``exclue``是不上传的。感觉``.git``的设计者很有用心。

那我们什么时候共享式，什么时候独享式呢？个人觉得，更多的是团队的一个约定。我们可以先对需要ignore的东西，做个大致分类：

- **操作系统层面**的   比如Mac OS的 ``.DS_Store``， windows的``Thumbs.db``；
- **IDE层面**的  比如Eclipse的``.project``, ``.settings/`` 和 ``.classpath``。 IDE层面还包括“朴素IDE”，比如临时用VIM应急修改了个东西，意外的闪崩生成了一个``.swap``文件或有些编辑器会生成``.bak``备份文件。
- **中间结果**类  比如程序运行一下，就打些日志到文件。再比如嵌入式数据库生成的临时文件。
- **语言相关**的  刚说的“中间结果”日志类的是通用的，无论哪种语言开发的程序都会输出日志，除此外，还有喝多跟语言编译相关的，比如JAVA的.class字节码，比如Web项目构建时生成的.war包。

了解这些后，或许我们可以把前面两类作为“独享式”只作用于自己本地，比如你用Mac那你配Mac的ignore，用Eclipse配Eclipse的；别人用Window，他自己配置Windows的。然后把中间结果和语言相关的，弄成“共享式”的，在全组员中共享。

这么多配置需要我们自己写吗？ 当然不用，这些问题很多开发者都是要遇到同样的问题的，把各种环境穷举下？ 事实上有人给我们做了。

###官方ignore模板

官方提供ignore模板 [https://github.com/github/gitignore](https://github.com/github/gitignore)

它的组织形式就是按上文说的“分层组织”。比如：

- 系统层
 * Mac的： [macOS.gitignore](https://github.com/github/gitignore/blob/master/Global/macOS.gitignore)
 * Linux的： [Linux.gitignore](https://github.com/github/gitignore/blob/master/Global/Linux.gitignore)

- IDE层
 * Eclipse的： [Eclipse.gitignore](https://github.com/github/gitignore/blob/master/Global/Eclipse.gitignore)
 * VIM朴素编辑器的： [Vim.gitignore](https://github.com/github/gitignore/blob/master/Global/Vim.gitignore)
 * NetBeans的： [NetBeans.gitignore](https://github.com/github/gitignore/blob/master/Global/NetBeans.gitignore)

- 语言层
 * JAVA的：[Java.gitignore](https://github.com/github/gitignore/blob/master/Java.gitignore)
 * GO的： [Go.gitignore](https://github.com/github/gitignore/blob/master/Go.gitignore)
 * LUA的： [Lua.gitignore](https://github.com/github/gitignore/blob/master/Lua.gitignore)
 * C的：[C.gitignore](https://github.com/github/gitignore/blob/master/C.gitignore)
 * C++的： [C++](https://github.com/github/gitignore/blob/master/C%2B%2B.gitignore)
