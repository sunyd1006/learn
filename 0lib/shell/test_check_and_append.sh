#!/bin/bash

# 导入lib.sh文件以使用check_and_append函数
source "$(cd -P -- "$(dirname -- "${BASH_SOURCE:-$0}")" && pwd)/lib.sh"

# 创建测试目录
TEST_DIR="/tmp/check_and_append_comprehensive_test"
mkdir -p "$TEST_DIR"

echo "=== 全面测试 check_and_append 函数 ==="

# 测试1: 正常情况 - 文件不存在时创建并追加内容
echo "测试1: 文件不存在时创建并追加内容"
TEST_FILE1="$TEST_DIR/test1.conf"
rm -f "$TEST_FILE1"  # 确保文件不存在
echo "执行: check_and_append '$TEST_FILE1' 'config_value=123'"
check_and_append "$TEST_FILE1" "config_value=123"
echo "结果:"
cat "$TEST_FILE1"
echo

# 测试2: 正常情况 - 文件存在且包含相同内容
echo "测试2: 文件存在且包含相同内容"
echo "执行: check_and_append '$TEST_FILE1' 'config_value=123' (应提示已存在)"
check_and_append "$TEST_FILE1" "config_value=123"
echo

# 测试3: 正常情况 - 文件存在但不包含该内容，追加新内容
echo "测试3: 文件存在但不包含该内容，追加新内容"
echo "执行: check_and_append '$TEST_FILE1' 'another_value=456'"
check_and_append "$TEST_FILE1" "another_value=456"
echo "结果:"
cat "$TEST_FILE1"
echo

# 测试4: 边界情况 - 测试只读文件
echo "测试4: 测试只读文件"
TEST_FILE2="$TEST_DIR/test2.conf"
echo "readonly_content=can't_modify" > "$TEST_FILE2"
chmod 444 "$TEST_FILE2"  # 设置为只读
echo "已创建只读文件 $TEST_FILE2 并设置权限为只读"
echo "执行: check_and_append '$TEST_FILE2' 'new_content=should_fail'"
check_and_append "$TEST_FILE2" "new_content=should_fail"
echo

# 测试5: 边界情况 - 空内容
echo "测试5: 空内容"
TEST_FILE3="$TEST_DIR/test3.conf"
echo "执行: check_and_append '$TEST_FILE3' ''"
check_and_append "$TEST_FILE3" ""
echo "结果:"
cat "$TEST_FILE3"
echo

# 测试6: 错误情况 - 参数不足
echo "测试6: 错误情况 - 参数不足"
echo "执行: check_and_append '/tmp/test.conf'"
check_and_append "/tmp/test.conf"
echo

# 测试7: 错误情况 - 参数过多
echo "测试7: 错误情况 - 参数过多"
echo "执行: check_and_append '/tmp/test.conf' 'content1' 'content2'"
check_and_append "/tmp/test.conf" "content1" "content2"
echo

echo "=== 测试完成 ==="
echo "清理测试文件..."
rm -rf "$TEST_DIR"
echo "测试结束"