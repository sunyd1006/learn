#!/bin/bash

# == 1.
# from google.colab import drive
# drive.mount('/content/gdrive')
# !cd /content/drive/MyDrive/

# 2. install python vritual env
python_env=python3.9
cd /content/drive/MyDrive/
drive_learn="/content/drive/Othercomputers/sunyd_mac/learn/"
rel_dependencies="${drive_learn}/ml_repo/d2l-zh/pytorch/0colab_env/requirements.txt"

pip3 install virtualenv
if [ ! -d "/content/drive/MyDrive/d2l" ]; then
    virtualenv -p ${python_env} /content/drive/MyDrive/d2l
    pip3 install -r ${drive_learn}/ml_repo/d2l-zh/pytorch/0colab_env/requirements.txt;
else
    source /content/drive/MyDrive/d2l/bin/activate;
    echo "D2L environment already exists, activate done..."
fi


