
cmake_minimum_required(VERSION 3.5)

project(cmake02_src)
add_subdirectory(src bin)

# 外部抄袭 ------------------------------



# 变量打印  --------------

# https://blog.csdn.net/weixin_44120025/article/details/122274849?utm_source=app&app_version=5.3.1

MESSAGE(STATUS "This is BINARY dir " ${PROJECT_BINARY_DIR})
MESSAGE(STATUS "This is SOURCE dir "${PROJECT_SOURCE_DIR})