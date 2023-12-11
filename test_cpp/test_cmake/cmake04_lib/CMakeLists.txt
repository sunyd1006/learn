
cmake_minimum_required(VERSION 3.5)

project(cmake04_lib)
add_subdirectory(lib bin)




#[[
在lib的cmake file 中使用了相对路径(/tmp/test_cmake)
故而此处编译cmake、make、make install, 即按照如下步骤执行
/tmp/test_cmake/cmake04_lib
├── include
│   └── hello.h
└── lib
    ├── libhello.a
    └── libhello.dylib

cd cmake-build-debug;
cmake -D CMAKE_INSTALL_PREFIX=/tmp/test_cmake ..
make
make install


]]