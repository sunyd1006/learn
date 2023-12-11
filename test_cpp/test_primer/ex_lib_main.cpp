//
// Created by sunyindong on 2022/3/9.
//

#include <iostream>

#include "ex_lib.h"

using namespace std;

int main () {
    int a = 1, b = 2;

    swap(&a, &b);

    printf("%d %d\n", a, b);

    return EXIT_SUCCESS;
}

// 参考： https://qppw4bc6rk.feishu.cn/wiki/wikcnr2DzC5wSuVEEWqCuKttXkh?sheet=oRBMoe#0LIySj