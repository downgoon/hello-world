# 写在前面的话

这是一个教程项目。它跟通常的教程***有些不同***。

通常教程以博客的形式展现，好的博客编辑器还支持“代码样式”（这点比“代码截图”年代强多了），作者可以把关键代码片段放入代码区（例如``Markdown``编辑器以\`\`一行代码\`\`符号表示一行代码，以\`\`\`多行代码\`\`\`表示多行代码）。

但是为了**重现**作者的示例代码运行过程，人们往往需要从博客中拷贝代码片段，并在IDE中编写其他代码。

---

这个过程**很浪费时间**。于是笔者发起这个``hello-world``项目，想做到几点：

- **以 markdown wiki 形式呈现**：markdown wiki 一方面保留了博客的“代码样式”，以便读者更好的阅读关键代码片段；另外一方面支持**社会化编辑**，任何人在学习的过程中，都可以贡献自己的力量，帮助完善教程。

- **step-by-step 重现作者代码过程**:   作者编写教程代码，需要遵循**“一个知识领域，一个git分支；一个知识点，一次git提交或一个 tag ”**的规则。这样读者想学习某个领域的知识，只需拉取对应的分支；在一个领域，不同知识点的演变，只需切换到指定的提交。

- **社会化贡献**： 任何人可以开分支贡献不同领域的代码。

---

# 使用步骤

- **查看教程列表**：教程列表体现在 [分支列表](https://github.com/downgoon/hello-world/branches) 里。
- **选择学习分支**： 比如想学习 Spring 配置方面的，可选择 [spring-config分支](https://github.com/downgoon/hello-world/tree/spring-config)。

克隆/拉取/下载``spring-config``分支到本地：

```
$ git clone -b spring-config https://github.com/downgoon/hello-world.git
```

或者 （先克隆主干，然后拉取``spring-config``分支）

```
$ git clone https://github.com/downgoon/hello-world.git
$ git checkout -b spring-config origin/spring-config
```

- **选择知识点**： 每个知识点，是一次commit，或者还打了一个tag （github会对每个tag发布一次release，或许比较浪费github资源）。

通过 commit log 查询知识点，并checkout 进入指定的点：

```
$ git log --grep="spring-config" -n 1

commit a730c52de03965fd304eb0e0dc57ffe298131344
Author: downgoon <downgoon@qq.com>
Date:   Sat Jan 14 16:52:27 2017 +0800

    spring-config-c5-confmulti

$ git checkout a730c52de0
```

如果作者打了tag，还可以通过以下指令列出所有以``spring-config``开头的tag，然后checkout 进入:

```
$ git tag --list "spring-config*"
spring-config-c1-annotation
spring-config-c2-twobeans
spring-config-c3-fileconf
spring-config-c4-confbyenc
spring-config-c5-confmulti

$ git checkout spring-config-c4-confbyenc
```

---

# 注意事项

- git push 时，默认不会push tag，需要显式``git push --tags`` （等效于 ``git push origin --tags``），当然也可以只push某个tag，比如``git push origin spring-config-c4-confbyenc``。
