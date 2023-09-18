*忘了如何在VScode 中使用 git*
知乎：在VScode上配置Git
git命令手册：csdn中

*从github中克隆*
1. clone 到本地
2. vscode 打开，并写代码
1. 用vscode 提交上。

# tips
廖雪峰Git教程总结
1、 几种status：
2、 配置git：
3、 版本回退：
4、 版本前进：
5、 add和Git 的意义：
6、 撤销工作区（修改或删除）：
7、 撤销暂存区修改：
8、 删除文件：
9、 添加远程库:
10、 从远程库克隆：
11、 Git鼓励大量使用分支：
12、 分支管理策略： 
13、 Bug分支：
14、 多人协作：
15、 标签管理：
16、 创建标签：
17、 操作标签： 
18、 忽略特殊文件：
19、 配置别名：



## 1、 几种status：

nothing to commit(working directort clean)  //什么都没有改动

Changes not stage for commit.                   // 工作区有改动，但还没add

Changes to be commited.                          // 有add. 但没commit

 

## 2、 配置git：

git config --m user.name "yourname"

git config --m user.email "youremail"

 

初始化一个Git仓库： git init 

​       添加文件到Git仓库，分两步：

   第一步，使用命令git add <file>，注意，可反复多次使用，添加多个文件；

   第二步，使用命令git commit，完成。

常用命令：

git status: 告诉你文件有没有被修改过

git diff :告诉你那里被修改了

git add 的各种区别:

   git add -A     // 添加所有改动

   git add *      // 添加新建文件和修改，但是不包括删除

   git add .      // 添加新建文件和修改，但是不包括删除

   git add -u     // 添加修改和删除，但是不包括新建文件

 

##  3、 版本回退：

git  log ：告诉我们版本历史记录

git --pretty=online 好看一点的git log

回退上一次的版本 回退上上次：^^

git reset --hard HEAD^ 

git reset --hard HEAD~100

 

## 4、 版本前进：

git reflog : 用来记录用户的每一次命令,查看版本号

git reset --hard 版本号

 

## 5、 add和Git 的意义：

你已理解了Git是如何跟踪修改的，你工作的地方叫工作区，如果不add到暂存区，那就不会加入到commit中。

 

## 6、 撤销工作区（修改或删除）：

git checkout -- <file>

一种是file自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；

一种是file已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。

(简单说：离你最近的那个git commit 或者 git add 状态)

 

## 7、 撤销暂存区修改：

​       git reset HEAD <file>   // 把暂存区的修改撤销，从新放回工作区

场景1： 当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命  令git checkout -- file。

场景2： 当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令git reset HEAD file，就回到了场景1，第二步按场景1操作。

场景3： 已经提交了不合适的修改到版本库时，想要撤销本次提交，参考版本回退一节，不过前提是没有推送到远程库。

 

## 8、 删除文件：

场景1：确实要删除版本库里的文件：git rm file 并且 git commit

场景2：删错了，我不删了：git checkout -- file

 

## 9、 添加远程库:

​0. 移除已关联的远程库（如果之前已关联）

​	git remote rm  origin    (添加多应用后就不叫origin)

1. 关联远程库：(默认名叫：origin. 添加多个应用后就用自定义名字)

​    git remote add origin git@server-name.com : Sun-Xian-Sen/repo-name.git

​	git remote add github git@github.com : Sun-Xian-Sen/repo-name.git

​	git remote add gitee  git@gitee.com :  Sun-Xian-Sen/repo-name.git

2. 第一次通常推送所有分支内容：git push -u origin master

3. 一般只推送最新master：git push origin master

 

## 10、 从远程库克隆：

(怎么把Github上的repository给下载到本地)

git clone git@github.com:yourName/yourRepository.git

 

## 11、 Git鼓励大量使用分支：

查看分支：git branch

创建+切换分支：git checkout -b <name>

创建分支：git branch <name>

切换分支：git checkout <name>

合并某分支到当前分支：git merge <name>

删除分支：git branch -d <name>

​     使用套路

先创建+切换：git checkout -b <name>

更新分支：...

一般返回master：git checkout <name>

将分支并入master: git merge <name>

删除分支：git branch -d <name>

 

## 12、 分支管理策略：

若不用快速合并，则分支不丢失，它在合并是已经创建又

commit了一次，就是多了一个节点。

将来还能看到分支

git merge --no-ff -m "your note" <name>

怎么查看分支图呢：

git log --graph --pretty=oneline --abbrev-commit

可用配置别名简写！



## 13、 Bug分支：

你在工作的时候，突然要改个bug,而又不想提交当前的

半途中分支。你需要把现在的这个分支给存下（藏起来）

改完bug后，我们在回到这个分支。

git stash list   列出所有的藏匿内容

步骤：

1. 保存分支使其成为藏匿内容: git stash   

2. 回到主分，创建bug分支，改你的bug，删掉bug分支

3. 回到原来的现场：

   git stash pop    恢复现场同时删除藏匿内容

   等价于( git stash apply  恢复 + git stash drop   删除 )

 

 

## 14、 多人协作：

查看远程库信息，使用git remote -v；

本地新建的分支如果不推送到远程，对其他人就是不可见的；

从本地推送分支: git push origin branch-name，如果推送失败，先用git pull抓取远程的新提交；

在本地创建和远程分支对应的分支:

git checkout -b branch-name origin/branch-name，

建立本地分支和远程分支的关联:

git branch --set-upstream branch-name origin/branch-name；

从远程抓取分支: git pull，

如果有冲突，要先处理冲突。

 

## 15、 标签管理：

tag 是一个版本的快照，比如QQ 8.0版，比你的commit号6a5819e

好多了是不是？

 

## 16、 创建标签：

1，用于新建一个标签，默认为HEAD，也可以指定一个commit id：git tag <name>,

2，指定标签信息：git tag -a <tagname> -m "blablabla..."

3，用PGP签名标签：git tag -s <tagname> -m "blablabla...",

4，查看所有标签：git tag

 

## 17、 操作标签：

推送一个本地标签: git push origin <tagname>

推送全部未推送过的本地标签: git push origin --tags

删除一个本地标签: git tag -d <tagname>

删除一个远程标签: git push origin :refs/tags/<tagname>

 

## 18、 忽略特殊文件：

1，你的工作区中有些文件不能被上传的，编辑 <.gitignore>文件

2，提交 .gitignore 文件即可

 

## 19、 配置别名：

方法1：

1. git config --global alias.st status  

2. git config --global alias.co checkout

3. git config --global alias.ci commit

4. git config --global alias.br branch

 

方法2：

每个仓库配置文件 用户/windows^/.git/config 中有：

[alias]

  last= log -l

  st=status

 

方法3：

当前用户配置文件 用户/windows^/.gitconfig 中有：

[alias]

  last= log -l

  st=status