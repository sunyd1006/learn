//
// Created by sunyindong on 2022/3/9.
//
int add(int a, int b) {
    return a + b;
}

void swap(int *num1, int *num2) {
    int tmp = *num1;
    *num1 = *num2;
    *num2 = tmp;
}

/**
*
 * g++ ex_lib_static_test_main.cpp -c -o ex_lib_static_test_main.o
 * ar rcs include/ex_lib_static.a ex_lib_static_test_main.o
*/