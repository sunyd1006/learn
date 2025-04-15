#!/bin/bash

scriptdir=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
cpplintdir=/Users/zhangchen/Desktop/develop/odps_src/bin

usage() {
  echo "mysync (get|put <path>) | putgit | aligngit | putgitpatch [-1|-2|<commit_id>] | abortgitpatch | listgit | cpplint [--verbose|<file_name>]"
}

######## config ########

# stgAG
#REMOTE_IP=100.81.183.34
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/apsarapangu/disk6/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# stgc100
#REMOTE_IP=100.81.181.100
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/apsarapangu/disk6/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# stgc85
#REMOTE_IP=100.81.181.85
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/apsarapangu/disk4/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# stgcAG
#REMOTE_IP=100.81.183.32
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/apsarapangu/disk11/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# stg94
#REMOTE_IP=100.81.182.94
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/home/admin/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# stgc86
# REMOTE_IP=100.81.182.86
# REMOTE_USER=admin
# REMOTE_BASE_DIR=/apsarapangu/disk3/habai.zc
# LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# dev197
# REMOTE_IP=10.101.210.197
# REMOTE_USER=admin
# REMOTE_BASE_DIR=/apsarapangu/disk1/habai.zc
# LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# dev129
# REMOTE_IP=10.101.219.129
# REMOTE_USER=admin
# REMOTE_BASE_DIR=/apsarapangu/disk3/habai.zc
# LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# 5KN 
#REMOTE_IP=10.101.215.200
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/home/admin/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# dev36
#REMOTE_IP=11.158.199.36
#REMOTE_USER=admin
#REMOTE_BASE_DIR=/apsarapangu/disk12/habai.zc
#LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop

# dev43
REMOTE_IP=11.158.199.43
REMOTE_USER=admin
REMOTE_BASE_DIR=/apsara/habai.zc
LOCAL_BASE_DIR=/Users/zhangchen/Desktop/develop
########################

CMD=$1

if [[ "$CMD" == "" ]]; then
  usage
  exit 1
fi

get_paths()
{
  # get full relative path related to LOCAL_BASE_DIR
  [ -d $RPATH ] && RPATH=`cd $RPATH && pwd -P`
  RDIR=$(cd -P -- $(dirname -- $RPATH) && pwd)
  RPATH=$RDIR/$(basename $RPATH)
  RPATH2=${RPATH##${LOCAL_BASE_DIR}/}
  if [[ "$RPATH2" == "$RPATH" ]]; then
    echo "can not find file or directory $RPATH in ${LOCAL_BASE_DIR}"
    exit 1
  fi
  RPATH2D=$(dirname $RPATH2)
}

put()
{
  scp -r ${LOCAL_BASE_DIR}/${RPATH2} ${REMOTE_USER}@${REMOTE_IP}:${REMOTE_BASE_DIR}/${RPATH2D}/
}

get()
{
  scp -r ${REMOTE_USER}@${REMOTE_IP}:${REMOTE_BASE_DIR}/${RPATH2} ${LOCAL_BASE_DIR}/${RPATH2D}/
}

remote_run()
{
  ssh ${REMOTE_USER}@${REMOTE_IP} "cd ${REMOTE_BASE_DIR}/${RPATH2D}/; $1; cd -"
}

case $CMD in

"get")
  RPATH=$2
  get_paths 
  get
;;

"put")
  RPATH=$2
  get_paths
  if [[ ! -e "$RPATH" ]]; then
    echo "can not find file or directory $RPATH in $(dirname $RPATH)"
    exit 1
  fi
  put
;;

"listgit")
  GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
  for i in $GITDIFFLIST; do
    echo $i
  done
;;

"putgit")
  GITDIFFLIST=$(git status -s | awk '{if($1!="D") {print $2}}')
  t_count=0
  for i in $GITDIFFLIST; do
    RPATH=$i
    get_paths
    if [[ ! -e "$RPATH" ]]; then
      echo "can not find file or directory $RPATH in $(dirname $RPATH)"
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

"putgitpatch")
  PARAM=-1
  if [[ "$2" != "" ]]; then
    PARAM="$2"
  fi
  GITPATCH=$(git format-patch $PARAM)
  for i in $GITPATCH; do
    RPATH=$i
    get_paths
    echo "[PUT PATCH] $i"
    put
    remote_run "mv -f $i ../; for f in \$(git status -s | awk '{print \$2}'); do rm -f \$f; done; git checkout .; git am ../$i; rm -f ../$i" 
  done
  rm -f $GITPATCH;
;;

"abortgitpatch")
  RPATH='./mock_file'
  get_paths
  remote_run "git am --abort" 
;;

"aligngit")
  RPATH='./mock_file'
  get_paths
  git pull --rebase
  commitid=`git rev-parse HEAD~1`
  remote_run "git reset --hard HEAD~1; git pull; git reset --hard $commitid" 
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
