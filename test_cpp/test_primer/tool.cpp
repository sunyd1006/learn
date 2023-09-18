//
// Created by sunyindong
//

#include "tool.h"

// define some variables

/**
 *  下面这行调用不成功, 它的编译命令如下：
 *  item.printHaha_failed_define();
 *
 *  1. g++ -std=c++14  Sales_data.cpp ex2main.cpp -o main && ./main
 *   报重定义错误： test_int_vec, test_str_vec等等
 *
 *  2.  g++ -std=c++14 ex2main.cpp -o main  && ./main
 *  Undefined symbols for architecture arm64:
 *  Sales_data::printHaha_failed_define()", referenced from:
          _main in ex2main-0a72e8.o
    ld: symbol(s) not found for architecture arm64
    clang: error: linker command failed with exit code 1 (use -v to see invocation)
*/


/**
* 下面两种声明方式，都不行
*/
// std::vector<int> test_int_vec(10);
// std::vector<std::string> test_string_vec(10);
// std::list<int> test_int_list(10);

// std::vector<int> test_int_vec = {};
// std::vector<std::string> test_string_vec = {};
// std::list<int> test_int_list = {};


// g++ tool.cpp Sales_data.cpp ex2main.cpp -o main -std=c++14 -v && ./main
