
import os
import sys

# 获取根目录路径（即main.py所在的目录）
root_path = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
# 将根目录添加到Python搜索路径
sys.path.append(root_path)
from subpackage.config_manager import getConfig

def printConfig():
    print(getConfig())

if __name__ == "__main__":
    print(getConfig())

