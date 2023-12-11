
rm -rf ./build;

mkdir build;

cd build;
cmake ..;
make;

# execute binary commands
./bin/src/test;