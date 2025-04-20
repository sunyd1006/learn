#!/bin/bash
source "../../0lib/shell/common.sh"

scriptdir=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
# NOTE: woring_dir is bigsun
SHELL_DIR="${scriptdir}"
cd ${SHELL_DIR}

usage() {
    echo """
          env [install_conda|download_learn])

         """
    echo "file path: ${SHELL_DIR}/start"
}

# https://repo.anaconda.com/miniconda/Miniconda3-py39_23.5.2-0-Linux-x86_64.sh
# https://repo.anaconda.com/miniconda/Miniconda3-py39_23.5.2-0-MacOSX-x86.sh
conda_py_version="py39_23.5.2-0"
conda_os_mac="MacOSX-x86_64.sh"
conda_os_linux="Linux-x86_64.sh"
function gen_conda_url() {
    conda_os=""
    if [ "$(uname)" == "Darwin" ]; then
        # Do something under Mac OS X platform
        conda_os=$conda_os_mac
        log_info 'Mac OS X'
    elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
        # Do something under GNU/Linux platform
        conda_os=$conda_os_linux
        log_info 'Linux'
    else
        log_info 'Other'
    fi
    conda_fullname="Miniconda3-${conda_py_version}-${conda_os}"
    conda_url="https://repo.anaconda.com/miniconda/$conda_fullname"

    log_warn "working dir: ${SHELL_DIR}"
    log_warn "conda_url: ${conda_url}"
    log_warn "whoami: $(whoami)"
}
gen_conda_url

# https://blog.csdn.net/NBDwo/article/details/136272442
# https://github.com/pytorch/pytorch/blob/main/RELEASE.md
# 1. nivida-smi 显示的cuda是硬件最高支持的版本
# 2. nvcc -V 显示的当前cuda编译器的版本，一般<= nvida-smi显示的cuda版本
# 3. torch必须适配cuda版本
function install_torch_and_d2l() {
    pip install torch==1.12.0            # cpu版本
    pip install torchvision==0.13.0      # gpu 版本
    pip install d2l==0.17.6              # d2l包
    [ $? -ne 0 ] && log_error "install d2l pythonlib failed" && exit 1
}

function install_d2l_and_cu115() {
    pip install torch==1.12.0            # cpu版本
    pip install torchvision==0.13.0      # gpu 版本
    pip install d2l==0.17.6              # d2l包
    [ $? -ne 0 ] && log_error "install d2l pythonlib failed" && exit 1
}

function install_d2l_on_cu121() {
    # https://pytorch.org/get-started/previous-versions/?utm_source=chatgpt.com
    # pytorch v2.2.2
    conda install pytorch==2.2.2 torchvision==0.17.2 torchaudio==2.2.2 pytorch-cuda=12.1 -c pytorch -c nvidia
    [ $? -ne 0 ] && log_error "install d2l pythonlib failed" && exit 1
}

function is_file_exists() {
    if [[ -f $1 ]]; then
        echo 0
    else
        echo 1
    fi
}

# 判定能够安装什么版本的 cuda

# lspci | grep -i nvidia
# 00:07.0 3D controller: NVIDIA Corporation TU104GL [Tesla T4] (rev a1)


#  安装conda是需要确认的，所以不能自动执行
function init_conda() {
    # download miniconda and d2l notes
    if [[ "$optional_p" == "install_conda" ]]; then
        log_warn "start to download miniconda and d2l notes"

        conda_path=${SHELL_DIR}/backup/$conda_fullname
        if [[ ! -f $conda_path ]]; then
            res=$(wget $conda_url -P ${SHELL_DIR}/backup/)
            log_warn "downloading successfully"
        fi

        if [[ ! -d "$HOME/miniconda3" ]]; then
          log_warn "start to install miniconda"
          rm -rf ~/miniconda3              # 如果你已安装miniconda3想要卸载，需要执行这个步骤
          sh ${SHELL_DIR}/backup/$conda_fullname -b
          ~/miniconda3/bin/conda init
          source ~/.bash_profile
          [ $? -ne 0 ] && log_error "conda env installation failed" && exit 1
        else
          log_info "miniconda exists"
        fi
    fi

    if [[ "$optional_p" == "download_learn" ]]; then
        yum install git -y
        git clone https://github.com/sunyd1006/learn
    fi

    # activate conda env
    source ~/.bash_profile
    conda env list | grep d2l
    deep_exists=$?
    if [[ $deep_exists != 0 ]]; then
        # conda env remove --name d2l
        log_warn "recreate conda d2l environment and pip install pytorch"

        conda create --name d2l python=3.9 -y
        [ $? -ne 0 ] && log_error "conda virtual env d2l failed" && exit 1
        conda activate d2l
        [ $? -ne 0 ] && log_error "conda activate d2l failed" && exit 1

        # 重新进入虚拟环境
        install_torch_and_d2l
    fi

    conda activate d2l
}

function start_jupyter() {
    # open notebook,         when your current dir is /root/bigsun/d2l-zh/pytorch
    cd ${SHELL_DIR}"/d2l-zh/pytorch"
    jupyter notebook --port=8888 --allow-root  --root=/root/bigsun/d2l-zh/pytorch --token=fdsafdsafdsaxfdsafdsunyindongtest
}


### ===== 主程序

CMD=$1
if [[ "$CMD" == "" ]]; then
    usage
    exit 1
fi

case $CMD in
"env")
    optional_p=$2
    init_conda
;;

"jupyter")
    start_jupyter
;;

*)
    usage
    exit 1
;;
esac