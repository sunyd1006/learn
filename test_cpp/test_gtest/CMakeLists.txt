#// https://www.jianshu.com/p/daf0e8c084c9
project(gtest_example)
set(CMAKE_CXX_STANDARD 14)
add_subdirectory(src)
add_subdirectory(test)

set(TOP_DIR ${CMAKE_CURRENT_LIST_DIR}/../)
add_subdirectory(${TOP_DIR}/third_party/gtest gtest)
add_subdirectory(${TOP_DIR}/util util)
