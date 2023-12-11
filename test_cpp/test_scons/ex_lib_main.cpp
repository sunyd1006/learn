#include <iostream>

#include "ex_lib.h"

using namespace std;


int main() {
    lib_print_one();

    #ifdef DEBUG
        std::cout << "DEBUG" << std::endl;
    #endif

    cout << "main file" << endl;
    return 0;
}

// 参考： https://qppw4bc6rk.feishu.cn/wiki/wikcnr2DzC5wSuVEEWqCuKttXkh?sheet=oRBMoe#0LIySj