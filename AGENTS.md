# AGENTS.md

本文档用于快速理解仓库结构与开发约定，帮助在该仓库中进行高效协作与自动化操作。

## 1. 项目概览

这是一个综合性的学习与实验仓库，包含多语言代码示例与小型项目。主要方向是 C++，同时覆盖 Java、Go、Python、机器学习等内容。仓库以“多个独立示例/子模块”的形式组织，而不是单体应用。

## 2. 目录结构速览

- `test_cpp/`：C++ 学习与测试用例（含 gtest、cmake、concurrent、primer 等）
- `2code_snippet/`：多语言代码片段集合（cpp/go/python）
- `demo_repo/`：可运行的小型示例或演示项目
- `ml_repo/`：机器学习相关材料与代码（D2L、各框架示例）
- `1env_install/`：环境配置脚本与工具安装说明
- `0lib/`：可复用的工具库与脚本

## 3. 各语言环境配置说明

- Python：把包含 `readme.md` 或者 `README.md` 的目录视为子模块。如果子模块中包含 Python 脚本，请使用 `uv` 管理该子模块的虚拟环境与依赖。
- C++：仓库根目录使用 CMake 管理，可在根目录新建 `build/` 编译。
- Go/Java：以各自目录中的 `go.mod`、`pom.xml` 或者 `Makefile` 为准，按子模块独立构建与运行。

## 4. 常用构建与运行

- CMake 构建（根目录）：
  - `mkdir build && cd build`
  - `cmake ..`
  - `make`
- gtest 运行（示例）：
  - `cd test_cpp/test_gtest`
  - `./run.sh`
- 代码片段（示例）：
  - `cd test_cpp/code_snippet`
  - `./run.sh`

## 5. 安全与配置约定

- 不要在代码中硬编码密码、AK/SK、Cookie 等敏感信息。
- 处理外部输入时避免注入风险，并保持依赖更新。
- 环境配置脚本集中在 `1env_install/`，优先参考其中各子目录的 README。
