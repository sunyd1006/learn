# python3 相对导入

* 优点：可以python  main2.py 使用脚本模式运行
* 缺点：使用系统path的方式容易污染环境

```
test_non_std_project
├── main.py
├── readme.md
├── subpackage
│   └── config_manager.py
└── subpackage2
    └── main2.py
```

1. 通过 `os.path`计算出根目录的绝对路径
2. 将根目录加入 `sys.path`，让 Python 能识别到 `subpackage`和 `subpackage2`这两个同级目录
3. 使用绝对导入 `from subpackage import config_manager`而非相对导入

# python3 标准工程
