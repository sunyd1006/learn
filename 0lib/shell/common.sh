#!/bin/bash

SHELL_DIR=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
PROJECT_DIR=$(realpath $SHELL_DIR/../../)

VERBOSE=${VERBOSE:-"false"}
function log_info() {
    if [[ ${VERBOSE} == "true" ]]; then
        echo "[INFO] $1 "
    fi
}

function log_warn() {
    if [[ ${VERBOSE} == "true" ]]; then
        echo -e "\033[33m[WARNING] $1 \033[0m"
    fi
}

function log_error() {
    if [[ ${VERBOSE} == "true" ]]; then
        echo -e "\033[31m[ERROR] $1 \033[0m"
    fi
}

log_info "PROJECT_DIR: $PROJECT_DIR"
log_info "SHELL_DIR: $SHELL_DIR"

# 函数功能：检查指定文件是否包含目标内容，包含则退出，不包含则添加内容到文件首行
# 参数说明：
#   $1: 目标文件路径（必填）
#   $2: 需检查/添加的内容（必填，支持含空格、特殊字符的字符串）
# 返回值：
#   0: 执行成功（内容已存在 或 内容已添加到首行）
#   1: 参数错误
#   2: 文件不可写
#   3: 添加内容失败
check_and_prepend() {
    # 1. 参数校验：确保传入2个必填参数
    if [ $# -ne 2 ]; then
        echo "错误：参数数量不足！"
        echo "用法：check_and_prepend <目标文件路径> <需检查/添加的内容>"
        return 1
    fi

    # 定义变量（用双引号包裹避免特殊字符解析问题）
    local file="$1"
    local content="$2"

    # 2. 检查文件是否存在：不存在则直接准备添加（后续操作会自动创建文件）
    #    存在则检查是否有写权限
    if [ -f "$file" ] && [ ! -w "$file" ]; then
        echo "错误：文件 '$file' 存在但无写入权限！"
        return 2
    fi

    # 3. 检查文件是否包含目标内容（grep -q 静默模式，仅返回状态码）
    #    -F: 将内容视为纯文本（避免正则表达式特殊字符干扰，如. * ?等）
    #    -x: 匹配整行内容（避免部分匹配，如"hello"误匹配"hello world"）
    if grep -qxF "$content" "$file"; then
        # echo "提示：文件 '$file' 已包含内容 -> $content，无需操作"
        return 0
    fi

    # 4. 内容不存在，添加到文件首行
    #    使用临时文件方式，将新内容放在第一行，然后是原文件内容
    local temp_file="${file}.tmp"

    # 如果文件存在，先保存原内容到临时文件，再写入新内容和原内容
    if [ -f "$file" ]; then
        # 将新内容和原文件内容写入临时文件
        { echo "$content"; cat "$file"; } > "$temp_file"
    else
        # 文件不存在，直接创建包含新内容的文件
        echo "$content" > "$temp_file"
    fi

    # 将临时文件重命名为原文件
    if mv "$temp_file" "$file"; then
        echo "成功：已将内容 '$content' 添加到文件 '$file' 的首行"
        return 0
    else
        # 清理临时文件（如果存在）
        rm -f "$temp_file"
        echo "错误：添加内容到文件 '$file' 失败！"
        return 3
    fi
}

# 函数功能：检查指定文件是否包含目标内容，包含则退出，不包含则追加内容到文件末尾
# 参数说明：
#   $1: 目标文件路径（必填）
#   $2: 需检查/追加的内容（必填，支持含空格、特殊字符的字符串）
# 返回值：
#   0: 执行成功（内容已存在 或 内容已追加）
#   1: 参数错误
#   2: 文件不可写
#   3: 追加内容失败
check_and_append() {
    # 1. 参数校验：确保传入2个必填参数
    if [ $# -ne 2 ]; then
        echo "错误：参数数量不足！"
        echo "用法：check_and_append <目标文件路径> <需检查/追加的内容>"
        return 1
    fi

    # 定义变量（用双引号包裹避免特殊字符解析问题）
    local file="$1"
    local content="$2"

    # 2. 检查文件是否存在：不存在则直接准备追加（后续echo会自动创建文件）
    #    存在则检查是否有写权限
    if [ -f "$file" ] && [ ! -w "$file" ]; then
        echo "错误：文件 '$file' 存在但无写入权限！"
        return 2
    fi

    # 3. 检查文件是否包含目标内容（grep -q 静默模式，仅返回状态码）
    #    -F: 将内容视为纯文本（避免正则表达式特殊字符干扰，如. * ?等）
    #    -x: 匹配整行内容（避免部分匹配，如"hello"误匹配"hello world"）
    if grep -qxF "$content" "$file"; then
        echo "提示：文件 '$file' 已包含内容 -> $content，无需操作"
        return 0
    fi

    # 4. 内容不存在，追加到文件末尾（用>>避免覆盖，双引号包裹保留内容格式）
    if echo "$content" >> "$file"; then
        echo "成功：已将内容 '$content' 追加到文件 '$file'"
        return 0
    else
        echo "错误：追加内容到文件 '$file' 失败！"
        return 3
    fi
}
