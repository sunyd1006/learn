cmake_minimum_required(VERSION 3.5)

project(cmake05_usedlib)

add_subdirectory(src bin)

message("CMAKE_BINARY_DIR is: " ${CMAKE_BINARY_DIR} )

#[[
/tmp/test_cmake/cmake04_lib
├── include
│   └── hello.h
└── lib
    ├── libhello.a
    └── libhello.dylib

]]