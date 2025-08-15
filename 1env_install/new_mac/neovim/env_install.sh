#!/bin/bash

source ${LEARN}/0lib/shell/common.sh

SHELL_DIR=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
function bak_old_nvim() {
    if [ -d "$HOME/.config/nvim" ]; then
        log_info "backup old nvim config"
        mv $HOME/.config/nvim $HOME/.config/nvim.bak
    else
      log_info "unlink old nvim config"
      unlink $HOME/.config/nvim
    fi
}

function init_nvim() {
    log_info "init nvim"
    bak_old_nvim
    mkdir -p $HOME/.config
    ln -s $SHELL_DIR/nvim $HOME/.config/nvim
    [ $? -ne 0 ] && log_error "init nvim failed" && exit 1
}

function install_nvim_if_need() {
  nvim --version > /dev/null 2>&1
  if [ $? -ne 0 ]; then
    log_info "install nvim"
    yum install neovim -y
    init_nvim
    log_info "install nvim success"
  else
    log_info "nvim already installed"
  fi
}

init_nvim

