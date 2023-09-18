
set(LIBCMAKE_SRC hello.cpp)

add_library(hello_static STATIC ${LIBCMAKE_SRC})

set_target_properties(hello_static PROPERTIES OUTPUT_NAME "hello")
# cmake 在构建一个新的target 时，会尝试清理掉其他使用这个名字的库，因为在构建 libhello.so 时， 就会清理掉 libhello.a
set_target_properties(hello_static PROPERTIES CLEAN_DIRECT_OUTPUT 1)

add_library(hello SHARED ${LIBCMAKE_SRC})
set_target_properties(hello PROPERTIES OUTPUT_NAME "hello")
set_target_properties(hello PROPERTIES CLEAN_DIRECT_OUTPUT 1)

# 安装 hello.h 到 ${CMAKE_INSTALL_PREFIX}/XXX下面
install(FILES hello.h DESTINATION cmake04_lib/include)

# 安装 动态库和静态库 到 ${CMAKE_INSTALL_PREFIX}/XXX下面
install(TARGETS hello hello_static  LIBRARY DESTINATION cmake04_lib/lib ARCHIVE DESTINATION cmake04_lib/lib)





#[[

安装后结果：
/tmp/test_cmake/cmake04_lib
├── include
│   └── hello.h
└── lib
    ├── libhello.a
    └── libhello.dylib

]]