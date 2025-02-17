# first init

```
git init
git config user.email "1404131532@qq.com"
git config user.name "sunyd1006"

ga .
gcm "init all test_code"

git remote add origin git@github.com:sunyd1006/learn.git
git branch -M main
git push -u origin main
```

# second usage

```
git config user.email "1404131532@qq.com"
git config user.name "sunyd1006"
```

# 当前仓库完全合并引用的开源仓库的提交历史

如果你希望将嵌套仓库的历史记录完全并入当前仓库：

```
# 举例子目前添加了一个测试仓库
# 1. 从 Git 索引中移除嵌套仓库：
cd test_ml/d2l-zh
git checkout -b test_ml/d2l-zh   # 替换为实际分支名


# 2. 将嵌套仓库推送到远程分支, 当前子仓库的内容推送到父仓库中。
git remote add temp <url_of_parent_repo>
git push temp test_ml/d2l-zh:nested-d2l-zh

# 3. 回到父仓库，合并远程仓库到本地
git fetch temp nested-d2l-zh
# --allow-unrelated-histories
# 常见于以下场景：	• 合并两个完全独立的 Git 仓库。	• 一个新项目初始化时，需要将一个现有仓库的内容合并进来。
git merge --allow-unrelated-histories temp/nested-d2l-zh

# 4. 清理嵌套目录
rm -rf test_ml/d2l-zh
git commit -m "Merged nested repository history into parent repository"


```

# Tool
