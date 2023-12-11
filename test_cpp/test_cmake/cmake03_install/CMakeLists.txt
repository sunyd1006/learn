
cmake_minimum_required(VERSION 3.5)

project(cmake03_install)
add_subdirectory(src bin)

install(DIRECTORY doc/ DESTINATION /tmp/test_cmake/cmake03_install/doc/)
install(FILES COPYRIGHT README DESTINATION /tmp/test_cmake/cmake03_install/)
install(PROGRAMS run_hello.sh DESTINATION /tmp/test_cmake/cmake03_install/bin/)


# 外部抄袭 ------------------------------



# 变量打印  --------------
# https://blog.csdn.net/weixin_44120025/article/details/122274849?utm_source=app&app_version=5.3.1

message(STATUS "This is BINARY dir " ${PROJECT_BINARY_DIR})
message(STATUS "This is SOURCE dir "${PROJECT_SOURCE_DIR})


