#!/usr/bin/env bash
function print_center() {
    str="========================="
    echo -e "\n"$str" "$1" "$str"\n"
}

print_center "** Shell菜鸟教程 https://www.runoob.com/linux/linux-shell.html **"

print_center "Shell 变量 https://www.runoob.com/linux/linux-shell-variable.html"
# 2 $skill ${skill}只是变量位置跟清楚一些 
echo "I am good at ${skill}Script"
myUrl="https://www.runoob.com"
readonly myUrl  # 只读变量
myUrl2="helloAagin"
unset myUrl2     # 删除变量

string="abcd"
echo "字符串长度 " ${#string}   # 输出 4
echo "abcd[0:1): " ${string:0:1} 



print_center "Shell 传递参数 https://www.runoob.com/linux/linux-shell-passing-arguments.html"
echo "执行的文件名：$0";
echo "第一个参数为：$1";
echo "第二个参数为：$2";

echo "第一个参数为：$1";
echo "参数个数为：$#";
echo "传递的参数作为一个字符串显示：$*";

# 假设在脚本运行时写了三个参数 1、2、3，
# 则 "$*" 等价于 "1 2 3"    （传递了一个参数）
# 而 "$@" 等价于 "1" "2" "3"（传递了三个参数）
echo "-- \$* 演示 ---"
for i in "$*"; do
    echo $i
done

echo "-- \$@ 演示 ---"
for i in "$@"; do
    echo $i
done


print_center "Shell 数组 https://www.runoob.com/linux/linux-shell-array.html"

my_array=(A B "C" D)

echo "第一个元素为: ${my_array[0]}"
echo "第二个元素为: ${my_array[1]}"

echo "数组所有元素为: ${my_array[*]}"
echo "数组所有元素为: ${my_array[@]}"

echo "数组元素个数为: ${#my_array[*]}"
echo "数组元素个数为: ${#my_array[@]}"


array_name=(value0 value1 value2 value3)
echo "获取数组元素  " ${array_name[0]}
echo "获取数组所有元素  " ${array_name[@]}
echo "获取数组长度 " ${#array_name[@]}   # 输出 4



print_center "Shell 基本运算符 https://www.runoob.com/linux/linux-shell-basic-operators.html"
a=10
b=20
val=`expr $a + $b`
echo "a + b : $val"

val=`expr $a - $b`
echo "a - b : $val"

val=`expr $a \* $b`
echo "a * b : $val"

val=`expr $b / $a`
echo "b / a : $val"

val=`expr $b % $a`
echo "b % a : $val"


#  关系运算符
# 10 -eq 20: a 不等于 b
# 10 -ne 20: a 不等于 b
# 10 -gt 20: a 不大于 b
# 10 -lt 20: a 小于 b
# 10 -ge 20: a 小于 b
# 10 -le 20: a 小于或等于 b
a=10
aa=10
b=20
if [ $a -eq $aa ]
then
   echo "$a -eq $aa : a 等于 aa"
else
   echo "$a -eq $aa: a 不等于 aa"
fi

if [ $a -eq $b ]
then
   echo "$a -eq $b : a 等于 b"
else
   echo "$a -eq $b: a 不等于 b"
fi
echo -e "\n\n"

# 逻辑运算符： && ||

# -z 相当于 isEmpty()
# -n 相当于 isNotEmpty()
a=""
if [ -z $a ]       # isEmpty()
then
   echo -e "-z $a : 字符串长度为 0"
else
   echo -e "-z $a : 字符串长度不为 0"
fi

if [ -n "$a" ]       # isNotEmpty()
then
   echo "-n $a : 字符串长度不为 0"
else
   echo "-n $a : 字符串长度为 0"
fi

# 文件测试运算符
<<'COMMENT'
        https://www.runoob.com/linux/linux-shell-basic-operators.html#:~:text=%E5%AD%97%E7%AC%A6%E4%B8%B2%E4%B8%8D%E4%B8%BA%E7%A9%BA-,%E6%96%87%E4%BB%B6%E6%B5%8B%E8%AF%95%E8%BF%90%E7%AE%97%E7%AC%A6,-%E6%96%87%E4%BB%B6%E6%B5%8B%E8%AF%95%E8%BF%90%E7%AE%97
    -d : 是否是目录
    -f : 是否是普通文件
    -e : 目录或文件是否存在

    -r : 是否可读
    -w : 是否可写
COMMENT

readme_file="/Users/sunyindong/codespace/test_cpp/test_other/readme.md"
if [ -f $readme_file ]
then
    echo "readme 存在"
fi



print_center "Shell echo命令 https://www.runoob.com/linux/linux-shell-echo.html"

# 输出重定向至文件
echo "It is a test" > data/myfile



print_center "Shell printf 命令 https://www.runoob.com/linux/linux-shell-printf.html"
# 格式化字符串， -左对齐，没有则是右对齐
printf "%-10s %-8s %-4.2f\n" 郭靖 男 66.1234
printf "%-10s %-8s %-4.2f\n" 杨过 男 48.6543


print_center "Shell test 命令 https://www.runoob.com/linux/linux-shell-test.html"
num1=100
num2=100


echo "note: test 和 [] 是等价的，后续我们会交替使用 test 和 []，
        且[]内两个空格是必须的，否则会导致语法错误。
        http://c.biancheng.net/view/2742.html"     # note: 注意一下[]必须空格
val=$[2*3]  # 就是计算表达式的
echo 'val=$[2*3]'"   $val"

# if [$num1 -eq $num2 ]         # note: 两个数不相等， 这里语法错误。
# if [ $num1 -eq $num2 ]        # note: 两个数相等
# if test $num1 -eq $num2       # note: 两个数相等
if test $[num1] -eq $[num2]     # note: 两个数相等
then
    echo '两个数相等！'
else
    echo '两个数不相等！'
fi


print_center "Shell 流程控制 https://www.runoob.com/linux/linux-shell-process-control.html"
a=10
b=20
if [ $a -eq $b ]
then
   echo "a 等于 b"
elif [ $a -gt $b ]
then
   echo "a 大于 b"
elif [ $a -lt $b ]
then
   echo "a 小于 b"
else
   echo "没有符合的条件"
fi

# for 循环
for loop in 1 2 3 4 5
do
    echo "The value is: $loop"
done


int=1
while(( $int <= 5 ) )
do
    echo '$int is '"$int" 
    let "int++"         # let表达式可以简写$int为int
done


# echo '按下 <CTRL-D> 退出'
# echo -n '输入你最喜欢的网站名: '
# while read FILM              # note: 读取命令行输入
# do
#     echo "是的！$FILM 是一个好网站"
# done


a=5
until [ ! $a -lt 10 ]
do
   echo '$a is '"$a"
   let "a++"
#    a=`expr $a + 1`
done


# read aNum
echo -e "\ncase (switch case) "
aNum=1
while true
do
    case $aNum in
        1)  echo '你选择了 1'
        aNum=2      # note: 定义变量时不加$，包括重新赋值；使用变量是用$
        continue
        ;;
        2)  echo '你选择了 2'
        aNum=3
        continue
        ;;
        3)  echo '你选择了 3'
        break
        ;;
        4)  echo '你选择了 4'
        ;;
        *)  echo '你没有输入 1 到 4 之间的数字'
        ;;
    esac
    break
done


print_center "Shell 函数 https://www.runoob.com/linux/linux-shell-func.html"

# 在函数体内获取形参获取：
#   1. $n, 当n>=10时，需要使用${n}来获取参数
#   2. $@: 以每个变量带引号的方式返回
# 函数返回值获取: $? 

funWithParam(){
    echo "第一个参数为 $1 !"
    echo "第二个参数为 $2 !"
    echo "第十个参数为 $10 !"
    echo "第十个参数为 ${10} !"
    echo "第十一个参数为 ${11} !"
    echo "参数总数有 $# 个!"
    echo "作为一个字符串输出所有参数 $@ !"

    # 依次打印形参
    for param in $@
    do
        echo $param
    done
}
funWithParam 1 2 3 4 5 6 7 8 9 34 73

print_center "Shell 输入/输出重定向 https://www.runoob.com/linux/linux-shell-io-redirections.html"

# 输入定向为input, 输出定向为 output
wc -l < data/input > data/output


echo  "here document 重定向"
wc -l << EOF
    欢迎来到
    菜鸟教程
    www.runoob.com
EOF

# todo: n >& m n <& m

print_center "Shell 文件包含 https://www.runoob.com/linux/linux-shell-include-file.html"
#  相当于 import test_bash_input.sh
. ./test_bash_input.sh
echo "菜鸟教程官网地址：$export_url"

































# 1988年，自由软件基金会的第一个付薪程序员 Brian Fox 写了一个 Shell，功能基本上是 Bourne shell 的克隆，叫做 Bourne-Again SHell，简称 Bash，程序名为bash，任何人都可以免费使用。
# 后来，它逐渐成为 Linux 系统的标准 Shell。

index=0
function print_center() {
    str="========================="
    echo -e "\n"$str" "$1" "$str"\n"
}

function print_center2() {
    str="========="
    echo -e "\n"$str" "$1" "$str"\n"
}

# 当前的 Linux 系统安装的所有 Shell
cat /etc/shells
# 当前默认的 Shell
echo $SHELL 
# bash 的版本
echo $(bash --version)
echo $BASH_VERSION
# zsh 的版本
echo $ZSH_VERSION


# https://wangdoc.com/bash/grammar.html
# -e : 解释引号（双引号和单引号）里面的特殊字符（比如换行符\n）
# -n : 参数可以取消末尾的回车符
echo -e "Hello\nWorld"

# 命令的组合符：&&和||
echo -n "hello "; echo "world"
echo -n "hello " && echo "world"
echo -n "hello " || echo world;

# type  https://wangdoc.com/bash/grammar.html#type-%E5%91%BD%E4%BB%A4
type -a echo
type -t bash

<<'COMMENT'
echo is a shell builtin
echo is /bin/echo
file
COMMENT

# 快捷键
<<'COMMENT'
    Ctrl + L：清除屏幕并将当前行移到页面顶部。
    Ctrl + C：中止当前正在执行的命令。
    Shift + PageUp：向上滚动。
    Shift + PageDown：向下滚动。
    Ctrl + U：从光标位置删除到行首。
    Ctrl + K：从光标位置删除到行尾。
    Ctrl + W：删除光标位置前一个单词。
    Ctrl + D：关闭 Shell 会话。
COMMENT

# 模式扩展：https://wangdoc.com/bash/expansion.html


print_center "模式扩展"
<<'COMMENT'
~扩展：会自动扩展成当前用户的主目录。
?扩展：代表文件路径里面的任意单个字符，不包括空字符
*字符：代表文件路径里面的任意数量的任意字符，包括零个字符。
- ls *不会输出隐藏文件；  ls .*匹配隐藏文件
- * 只匹配当前目录，不会匹配子目录；*/*.txt类似写法匹配几层子目录；**/*.txt可以匹配顶层的文本文件和任意深度子目录的文本文件
[...]：只有文件确实存在的前提下才会扩展
[start-end]: 表示匹配一个连续的范围
{...}：表示分别扩展成大括号里面的所有值，各个值之间使用逗号分隔
{start..end}：大括号扩展有一个简写形式，表示扩展成一个连续序列。比如，{a..z}可以扩展成26个小写英文字母。
$(...)：可以扩展成另一个命令的运行结果，该命令的所有输出都会作为返回值。
$((...))：可以扩展成整数运算的结果
[[:class:]]表示一个字符类，扩展成某一类特定字符之中的一个

COMMENT

pwd_str=$PWD
echo $pwd_str
echo -e "*.txt      :" *.txt
echo -e ".*         :" .*
echo -e ".[!.]*     :" .[!.]*   # 不匹配.和..文件， 输出.[!.]* 表示不存在隐藏文件

# mkdir {2007..2009}-{01..12} 
for i in {1..4}
do
  echo "this is used $i in {1..4} "
done

echo "echo {001..5}    :" {001..5} 
echo "返回所有匹配给定字符串string的变量名    :" ${!S*}
echo "$(...)可以扩展成另一个命令的运行结果    :" $(date)
echo '$((2 + 2))    :' $((2 + 2))

echo "所有不以数字开头的文件名" [![:digit:]]*

# 大括号扩展
# cp a.log{,.bak}   # cp a.log a.log.bak

#  todo: 看到13点，我就不想看太多了