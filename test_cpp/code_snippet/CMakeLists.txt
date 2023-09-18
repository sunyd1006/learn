#// https://www.jianshu.com/p/daf0e8c084c9

project(code_snippet)

set(CMAKE_CXX_FLAGS   "-std=c++11")             # c++11
set(CMAKE_CXX_FLAGS   "-g")                     # 调试信息
set(CMAKE_CXX_FLAGS_DEBUG   "-O0" )             # 调试包不优化

set(TOP_DIR ${CMAKE_CURRENT_LIST_DIR}/../)
add_subdirectory(${TOP_DIR}/third_party/gtest gtest)
add_subdirectory(${TOP_DIR}/util util)


add_executable(${PROJECT_NAME} gtest_with_mock_and_real_obj.cpp)

target_link_libraries(${PROJECT_NAME} gtest gmock util)
