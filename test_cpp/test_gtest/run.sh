
dir=$( cd "$( dirname "$0" )" && pwd )
cd $dir

rm -rf ./build;
mkdir -p build;

cd build;
cmake ..;
make;

# execute binary commands
./bin/src/test;