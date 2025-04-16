#!/bin/bash

# https://zhuanlan.zhihu.com/p/704157473/

path="/tmp/miniforge.sh"

function download_sh() {
    mkdir -p /tmp/
    wget https://github.com/conda-forge/miniforge/releases/download/25.3.0-3/Miniforge3-25.3.0-3-MacOSX-arm64.sh -O $path
    sh $path -y
    ~/miniforge3/bin/conda init
}
# download_sh

echo "
channels:
  - conda-forge  # 优先使用社区维护的包
  - defaults
show_channel_urls: true
default_channels:
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
  - https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge
custom_channels:
  conda-forge: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge
  pytorch: https://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/pytorch" > ~/.condarc


# https://stackoverflow.com/questions/67380286/anaconda-channel-for-installing-python-2-7
# 可以看出已经不在支持py27, 就算支持也是勉强支持，所以不必强求本地有python2的环境。
# mamba create -y --name py28 python=2.8




