#!/bin/bash

echo "mysync params: $@"
set -x
scriptdir=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
cpplintdir=/Users/sunyindong.syd/codespace/develop_ali/odps_src/bin

usage() {
  echo "\nmysync (get|put <path>) | putdf | aligngit | putgitpatch|pp [-1|-2|<commit_id>] | abortgitpatch | listgit | cpplint [--verbose|<file_name>]"
}

######## config ########

lower_target=$(echo "$TARGET" | tr '[:upper:]' '[:lower:]')
case "$lower_target" in
    "at-40n"|"40n"|"at40n")
        REMOTE_HOSTNAME="AT-40N"
        REMOTE_IP="11.158.199.145"
        REMOTE_USER="admin"
        REMOTE_BASE_DIR="/apsarapangu/disk11/sunyindong.syd"
        LOCAL_BASE_DIR="/Users/sunyindong.syd/codespace/develop_ali"
        ;;
    "at-odps-dev"|"")
        REMOTE_HOSTNAME="AT-ODPS-DEV"
        REMOTE_IP="11.158.199.43"
        REMOTE_USER="admin"
        REMOTE_BASE_DIR="/apsara/sunyindong.syd/codespace/"
        LOCAL_BASE_DIR="/Users/sunyindong.syd/codespace/develop_ali"
        ;;
    *)
        echo "Unknown TARGET: $TARGET"
        exit 1
        ;;
esac

########################

set -e
CMD=$1
print_config() {
  echo "===== CONFIG ===="
  echo "REMOTE_HOSTNAME: $REMOTE_HOSTNAME, REMOTE_IP: $REMOTE_IP"
  echo "LOCAL_BASE_DIR: $LOCAL_BASE_DIR, REMOTE_BASE_DIR: $REMOTE_BASE_DIR"
  echo "file_path: $scriptdir/mysync.sh \n"
}

if [[ "$CMD" == "" ]]; then
    usage
    print_config
    exit 1
fi

get_paths()
{
  echo "get_paths relative_path: $relative_path"

  # get full relative path related to LOCAL_BASE_DIR
  [ -d $relative_path ] && relative_path=`cd $relative_path && pwd -P`
  RDIR=$(cd -P -- $(dirname -- $relative_path) && pwd)
  relative_path=$RDIR/$(basename $relative_path)

  # 移除 LOCAL_BASE_DIR 目录, 得到repo/file_path
  repo_relative_path=${relative_path##${LOCAL_BASE_DIR}/}
  echo "LOCAL_BASE_DIR: $LOCAL_BASE_DIR"
  echo "relative_path: $relative_path"
  echo "file_path: $repo_relative_path"

  if [[ "$repo_relative_path" == "$relative_path" ]]; then
    echo "can not find file or directory $relative_path in ${LOCAL_BASE_DIR}"
    exit 1
  fi

  # repo_relative_dirname: repo_name/path/to/dirname
  repo_relative_dirname=$(dirname $repo_relative_path)
#   echo "relative_path: $relative_path, repo_relative_path: $repo_relative_path, repo_relative_dirname: $repo_relative_dirname"
}

put()
{
  scp -r ${LOCAL_BASE_DIR}/${repo_relative_path} ${REMOTE_USER}@${REMOTE_IP}:${REMOTE_BASE_DIR}/${repo_relative_dirname}/
}

get()
{
  scp -r ${REMOTE_USER}@${REMOTE_IP}:${REMOTE_BASE_DIR}/${repo_relative_path} ${LOCAL_BASE_DIR}/${repo_relative_dirname}/
}

remote_run()
{
  echo "    will remote_run: $1"
  ssh ${REMOTE_USER}@${REMOTE_IP} "cd ${REMOTE_BASE_DIR}/${repo_relative_dirname}/; $1; cd -"
}

case $CMD in

"get")
  relative_path=$2
  get_paths
  get
;;

"put")
  relative_path=$2
  get_paths
  if [[ ! -e "$relative_path" ]]; then
    echo "can not find file or directory $relative_path in $(dirname $relative_path)"
    exit 1
  fi
  put
;;

# git status
"gst")
  GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
  for i in $GITDIFFLIST; do
    echo $i
  done
;;

# push to remote diff file
"putdf")
  GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
  t_count=0
  for i in $GITDIFFLIST; do
    relative_path=$i
    get_paths
    if [[ ! -e "$relative_path" ]]; then
      echo "can not find file or directory $relative_path in $(dirname $relative_path)"
      exit 1
    fi
    if [[ $t_count -eq 5 ]]; then
        t_count=0
        wait
    fi
    ((t_count++))
    put &
  done
  wait
;;

# 主要目的是将当前工作目录下的Git补丁打包，并发送到远程服务器上进行应用。
"putgitpatch" | "pp" )
  PARAM=-1
  if [[ "$2" != "" ]]; then
    PARAM="$2"
  fi
  GITPATCH=$(git format-patch $PARAM)
  for i in $GITPATCH; do
    relative_path=$i
    get_paths
    echo "[PUT PATCH] $i"
    put
    remote_run "mv -f $i ../; for f in \$(git status -s | awk '{print \$2}'); do rm -f \$f; done; git checkout .; git am ../$i; rm -f ../$i"
  done
  rm -f $GITPATCH;
;;

"abortgitpatch")
  relative_path='./mock_file'
  get_paths
  remote_run "git am --abort"
;;

"aligngit")
  relative_path='./mock_file'
  get_paths
  git pull origin master --rebase
  commitid=`git rev-parse HEAD~1`
  remote_run "git reset --hard HEAD~1; git pull origin master --rebase ; git reset --hard $commitid"
;;

"cpplint")
  targetFile=$2
  allLinkOk=true
  if [[ "$targetFile" == "" ]]; then
    GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
    for i in $GITDIFFLIST; do
      $cpplintdir/cpplint.py $i >/dev/null 2>&1
      if [[ $? -ne 0 ]]; then
        echo "$i [FAILED]"
        allLinkOk=false
      fi
    done
  elif [[ "$targetFile" == "--verbose" ]]; then
    GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
    for i in $GITDIFFLIST; do
      $cpplintdir/cpplint.py $i
      if [[ $? -ne 0 ]]; then
        allLinkOk=false
      fi
    done
  else
      if [[ ! -e $targetFile ]]; then
          echo "[$targetFile] not exist"
          exit 2
      fi
      $cpplintdir/cpplint.py $targetFile
  fi
  if ! $allLinkOk; then
    exit 1
  fi
;;

*)
  usage
  exit 1
;;

esac
