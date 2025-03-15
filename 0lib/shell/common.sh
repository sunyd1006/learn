#!/bin/bash

SHELL_DIR=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
PROJECT_DIR=$(realpath $SHELL_DIR/../../)

echo "PROJECT_DIR: $PROJECT_DIR"
echo "SHELL_DIR: $SHELL_DIR"

function log_info() {
    echo "[INFO] $1 "
}

function log_warn() {
    echo -e "\033[33m[WARNING] $1 \033[0m"
}

function log_error() {
    echo -e "\033[31m[ERROR] $1 \033[0m"
}
