import os

def getBuildTargetName(targetName, build_dir="build"):
    return os.path.join(build_dir, targetName)

env = Environment(CXXFLAGS="-std=c++11")


# gcc 不同文件夹下的源文件编译
# https://www.cnblogs.com/lihello/p/14435941.html

common_obj_li = Object(Glob('util/*.cpp'))
curr_ex_cpp_li = Glob("ex26*.cpp")
curr_ex_cpp_li += common_obj_li
env.Program(getBuildTargetName("ex26"), curr_ex_cpp_li, CPPPATH='../util')