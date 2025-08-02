cmake_minimum_required(VERSION 3.15)
project(learn)

set(CMAKE_CXX_STANDARD 17)
set(ODPS_SRC_DEP "/Users/sunyindong.syd/codespace/develop_ali/odps_src_dependencies")
set(SYSTEM_HEADER_FILE "/Library/Developer/CommandLineTools/SDKs/MacOSX15.4.sdk/usr/include/c++/v")


include_directories(.)
include_directories(${SYSTEM_HEADER_FILE})

file(GLOB_RECURSE files
        test_cpp/*.cpp
        tmp_cpp/*.cpp
        2code_snippet/cpp/*.cpp
)

add_executable (learn ${files})