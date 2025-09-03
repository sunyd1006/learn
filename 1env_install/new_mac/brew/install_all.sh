#！/bin/bash

#xcode-select --install 是 macOS 中的一个命令，用于安装 Xcode 命令行工具（Command Line Tools）。
# 这些工具包含开发、编译和构建应用程序所需的工具链，例如：
#    编译器（如 clang、gcc）
#    调试工具（如 lldb）
#    版本控制工具（如 git 的部分依赖）
#    构建系统工具（如 make）
#    开发框架（如 SDKs）
# 反之如何卸载呢，xcode-select --print-path 查询安装目录 && rm sudo rm -rf /Library/Developer/CommandLineTools
xcode-select --install


# 1. install brew first
# A. https://blog.csdn.net/weixin_63310665/article/details/143313410
#   一键安装brew并替换源 https://www.jianshu.com/p/5bfe47187b12
# /bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"


# == 2. 安装必要的装机软件
brew install wget
brew install openjdk
brew install go
brew install helm

# -- 3. 安装字体
brew install --cask font-hack-nerd-font


