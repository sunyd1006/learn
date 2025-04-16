
SHELL_DIR=$(cd -P -- $(dirname -- ${BASH_SOURCE:-$0}) && pwd)
cd $SHELL_DIR

if [ -d /Library/Developer/CommandLineTools ]; then
    echo "MacOSX.sdk is already installed"

    # 拷贝所有include里面的头文件
    sudo cp -a ./cpp/include/ /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include
    echo "All include file is included."
else
    xcode-select --install
fi

