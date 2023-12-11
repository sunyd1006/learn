# src/CmakeLists.txt

set(SRC_LIST main.cpp)

# NOTE: 相当于 -I 指定目录, 本文main.cpp中用到的hello.h 就在此目录中
include_directories(/tmp/test_cmake/cmake04_lib/include/)
# NOTE: 相当于 -L 指定lib路径
link_directories(/tmp/test_cmake/cmake04_lib/lib)

add_executable(${PROJECT_NAME} ${SRC_LIST} )
# NOTE: 相当于 -l, 且指定具体的lib路径路径必须在 add_executable 后面
target_link_libraries(${PROJECT_NAME} libhello.so)
#target_link_libraries(${PROJECT_NAME} libhello.a)