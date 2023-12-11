cmake_minimum_required(VERSION 3.5)

project(cmake01_hello)
set(src_list main.cpp)

add_executable(${PROJECT_NAME} ${src_list} )

MESSAGE(STATUS "This is BINARY dir " ${PROJECT_BINARY_DIR})
MESSAGE(STATUS "This is SOURCE dir "${POJECT_SOURCE_DIR})
