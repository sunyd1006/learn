
scriptdir=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
# NOTE: woring_dir is bigsun
working_dir="${scriptdir}"
cd ${working_dir}

usage() {
    echo "start jupyter | (install [download|recreate])"
    echo ""
    echo "file path: ${working_dir}/start"
}

function log_info() {
    echo "[INFO] $1 "
}

function log_warn() {
    echo -e "\033[33m[WARNING] $1 \033[0m"
}

function log_error() {
    echo -e "\033[31m[ERROR] $1 \033[0m"
}

# https://repo.anaconda.com/miniconda/Miniconda3-py39_23.5.2-0-Linux-x86_64.sh
# https://repo.anaconda.com/miniconda/Miniconda3-py39_23.5.2-0-MacOSX-x86.sh
conda_py_version="py39_23.5.2-0"
conda_os=""
conda_os_mac="MacOSX-x86_64.sh"
conda_os_linux="Linux-x86_64.sh"
function log_init() {
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

    log_warn "working dir: ${working_dir}"
    log_warn "conda_url: ${conda_url}"
    log_warn "whoami: $(whoami)"

}
log_init

function install_torch_and_d2l() {
    # pip install torch==1.12.0          # cpu版本
    pip install torchvision==0.13.0      # gpu 版本
    pip install d2l==0.17.6   # d2l包
}

function is_file_exists() {
    if [[ -f $1 ]]; then
        echo 0
    else
        echo 1
    fi
}


#  安装conda是需要确认的，所以不能自动执行
function init_conda() {
    # download miniconda and d2l notes
    if [[ "$FROM_ZERO" == "download" ]]; then
        log_warn "start to download miniconda and d2l notes"

        conda_path=${working_dir}/backup/$conda_fullname
        if [[ ! -f $conda_path ]]; then
            res=$(wget $conda_url -P ${working_dir}/backup/)
            log_warn "downloading successfully"
        fi

        # sudo apt-get update; sudo apt-get upgrade; sudo apt-get install unzip wget

        # mkdir d2l-zh && cd d2l-zh
        # curl https://zh-v2.d2l.ai/d2l-zh-2.0.0.zip -o d2l-zh.zip
        # unzip d2l-zh.zip && rm d2l-zh.zip
    fi

    if [[ ! -d "$HOME/miniconda3" ]]; then
        log_warn "start to install miniconda"
        rm -rf ~/miniconda3              # 如果你已安装miniconda3想要卸载，需要执行这个步骤
        sh ${working_dir}/backup/$conda_fullname -b
        ~/miniconda3/bin/conda init
        source ~/.bash_profile
        if [[ $? != 0 ]]; then
            log_error "conda env installation failed"
            exit 1
        fi
    fi

    source ~/.bash_profile

    # 查看conda env
    conda env list | grep d2l
    deep_exists=$?
    if [[ $deep_exists != 0 ]]; then
        # conda env remove --name d2l
        log_warn "recreate conda d2l environment and pip install pytorch"
        # conda create --name d2l python=3.9 -y
        # install_torch_and_d2l
    fi

    # 重新进入虚拟环境
    source activate d2l

    # 退出虚拟环境
    # conda deactivate d2l
}


function start_jupyter() {
    # open notebook,         when your current dir is /root/bigsun/d2l-zh/pytorch
    cd ${working_dir}"/d2l-zh/pytorch"
    jupyter notebook --port=8888 --allow-root  --root=/root/bigsun/d2l-zh/pytorch --token=fdsafdsafdsaxfdsafdsunyindongtest
}



CMD=$1
if [[ "$CMD" == "" ]]; then
    usage
    exit 1
fi


case $CMD in

"install")
    FROM_ZERO=$2
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