#!/bin/bash

# 导入包含prepend函数的lib文件
source "$(cd -P -- "$(dirname -- "${BASH_SOURCE:-$0}")" && pwd)/lib_with_prepend.sh"

# 创建测试目录
TEST_DIR="/tmp/check_and_prepend_test"
mkdir -p "$TEST_DIR"

echo "=== 测试 check_and_prepend 函数 ==="

# 测试1: 文件不存在时创建并添加内容到首行
echo "测试1: 文件不存在时创建并添加内容到首行"
TEST_FILE1="$TEST_DIR/test1.conf"
rm -f "$TEST_FILE1"  # 确保文件不存在
echo "执行: check_and_prepend '$TEST_FILE1' 'first_line=hello_world'"
check_and_prepend "$TEST_FILE1" "first_line=hello_world"
echo "结果:"
cat "$TEST_FILE1"
echo

# 测试2: 文件存在且包含相同内容
echo "测试2: 文件存在且包含相同内容"
echo "执行: check_and_prepend '$TEST_FILE1' 'first_line=hello_world' (应提示已存在)"
check_and_prepend "$TEST_FILE1" "first_line=hello_world"
echo

# 测试3: 文件存在但不包含该内容，添加新内容到首行
echo "测试3: 文件存在但不包含该内容，添加新内容到首行"
echo "执行: check_and_prepend '$TEST_FILE1' 'new_first_line=prepend_test'"
check_and_prepend "$TEST_FILE1" "new_first_line=prepend_test"
echo "结果:"
cat "$TEST_FILE1"
echo

# 测试4: 包含特殊字符的内容
echo "测试4: 包含特殊字符的内容"
TEST_FILE2="$TEST_DIR/test2.conf"
echo "执行: check_and_prepend '$TEST_FILE2' 'special_chars=.*[]{}?+|^$\\'"
check_and_prepend "$TEST_FILE2" "special_chars=.*[]{}?+|^$\\"
echo "结果:"
cat "$TEST_FILE2"
echo

# 测试5: 包含空格的内容
echo "测试5: 包含空格的内容"
TEST_FILE3="$TEST_DIR/test3.conf"
echo "执行: check_and_prepend '$TEST_FILE3' 'path with spaces = /path/with spaces/in it'"
check_and_prepend "$TEST_FILE3" "path with spaces = /path/with spaces/in it"
echo "结果:"
cat "$TEST_FILE3"
echo

# 测试6: 对比测试 - prepend vs append
echo "测试6: 对比测试 - prepend vs append"
TEST_FILE4="$TEST_DIR/test4.conf"
rm -f "$TEST_FILE4"
echo "先使用prepend添加第一行:"
check_and_prepend "$TEST_FILE4" "first_line=prepend"
echo "再使用prepend添加第二行:"
check_and_prepend "$TEST_FILE4" "second_line=prepend"
echo "使用append添加第三行:"
check_and_append "$TEST_FILE4" "third_line=append"
echo "最终文件内容:"
cat "$TEST_FILE4"
echo

# 测试7: 错误情况 - 参数不足
echo "测试7: 错误情况 - 参数不足"
echo "执行: check_and_prepend '/tmp/test.conf' (应该报错)"
check_and_prepend "/tmp/test.conf"
echo

echo "=== 测试完成 ==="
echo "清理测试文件..."
rm -rf "$TEST_DIR"
echo "测试结束"