#!/bin/bash

if [ -d "$HOME/miniconda3" ]; then
    echo "Miniconda3 already installed"
    exit 0
fi

if [ ! -f "/tmp/miniconda.sh" ]; then
    wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh -O /tmp/miniconda.sh
fi
bash /tmp/miniconda.sh -b -p $HOME/miniconda3
~/miniconda3/bin/conda init bash

conda tos accept --override-channels --channel https://repo.anaconda.com/pkgs/main
conda tos accept --override-channels --channel https://repo.anaconda.com/pkgs/r
conda create --name d2l python=3.9 -y




