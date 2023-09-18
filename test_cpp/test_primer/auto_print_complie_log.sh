#!/bin/bash
######################################################################
# 使用说明：直接在CLion中执行，即可先打印日志
######################################################################

current_floder_name=$(cd $(dirname $0); pwd)      # dirname $0 : 获取目录名
log_name=sunhu_compile_1005_xx873894873847384.log
main_name="main.out"
abs_log_name=$current_floder_name"/"$log_name
abs_main_name=$current_floder_name"/"$main_name

# :note 配置编辑文件命令
all_relative_compile_file_name="tool.cpp Sales_data.cpp ex17main.cpp";

#cpp_version="11"
cpp_version="14"
#cpp_version="17"

function read_file() {
  while IFS= read -r line;
  do
    # todo 因为没有找到 printf的颜色打印，就草草实现了
    # 不会忽略空格，而echo 会
    is_error_line=$(echo $line | grep -cE "error|warning" | awk '{print $1}')
    if [ $is_error_line -gt 0 ]; then
        echo $line | perl -pe 's/(fatal)/\e[0;41;37m$1\e[0m/g; s/(error)/\e[1;31m$1\e[0m/g; s/(warning)/\e[1;33m$1\e[0m/g; s/(info)/\e[1;32m$1\e[0m/g; s/(debug)/\e[1;34m$1\e[0m/g; s/(wechat)/\e[1;35m$1\e[0m/g'
    else
        printf '%s\n' "$line"
    fi
  done < $1
}

run_compile_param1=$(cd $current_floder_name && g++ -std=c++$cpp_version $all_relative_compile_file_name -Wall -Wextra -o $main_name -g 2>$log_name)

# :note 配置编辑命令
#if [ $abs_compile2 ] ; then
#  run_compile_param1=$(g++ -std=c++$cpp_version $abs_compile1 $abs_compile2 -o $abs_main_name 2>$abs_log_name)
#else
#  run_compile_param2=$(g++ -std=c++$cpp_version $abs_compile1 -o $abs_main_name 2>$abs_log_name)
#fi

if [ ! -f $abs_log_name ] ; then
    # 判断文件是否存在
    echo "------------------------------------------- 不存在$log_name"
    exit
else
    how_many_error=$(grep -cE "error" $abs_log_name | awk '{print $1}')
    if [ $how_many_error -gt 0 ] ; then
        echo -e "\nvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"
        echo -e "vv                                                                              vv"
        echo -e "vv                                                                              vv"
        echo -e "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n"
        read_file $abs_log_name
        echo -e "\n----------------------------------------------------------------------------------"

        # perl 高亮关键字用的
        error_content=$(grep -E "error|warning" $abs_log_name | awk '{print}' | perl -pe 's/(fatal)/\e[0;41;37m$1\e[0m/g; s/(error)/\e[1;31m$1\e[0m/g; s/(warning)/\e[1;33m$1\e[0m/g; s/(info)/\e[1;32m$1\e[0m/g; s/(debug)/\e[1;34m$1\e[0m/g; s/(wechat)/\e[1;35m$1\e[0m/g')
        echo "$error_content"
    else
        # abs_path_to_exe_file=$(cd $(dirname $0);pwd)"/"$main_name
        # echo "当前文件目录： "$abs_path_to_exe_file
        #        add_ch=$(chmod +x $abs_path_to_exe_file)

        #        https://blog.csdn.net/toby54king/article/details/103376514
        #        http://www.361way.com/shell-process/1126.html
        # 调用可执行文件
        $abs_main_name
    fi

    rm_file=$(rm -rf $abs_log_name)
fi



# 计算日志的 md5
#old_log_md5=$(md5sum -b $log_name | awk '{print $1}'| sed 's/ //g')

# 检测的文件是否变化： https://blog.csdn.net/diyiday/article/details/93077288
# pwd  # vscode, /Users/sunyindong/workspace/CLionProjects/test_cpp
