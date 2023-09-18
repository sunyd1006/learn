//
// Created by sunyindong on 2021/12/14.
//

#include <iostream>
#include <iterator>
#include <vector>

#include "tool.h"
#include <cstring>

using namespace  std;

// duplicate symbols:  g11 Sales_data.cpp ex3main.cpp -o main && ./main
// g11 ex3main.cpp -o main && ./main
int main() {
    // C风格字符串
    cout << endl << endl << "start ex3_40: " << endl;

    // #include <cstring>
    const char cstr1[]="Hello";
    const char cstr2[]="world!";
    // ERROR: error: constexpr variable 'new_size' must be initialized by a constant expression
    // constexpr size_t new_size = strlen(cstr1) + strlen(" ") + strlen(cstr2) + 1;
    size_t new_size = strlen(cstr1) + strlen(" ") + strlen(cstr2) + 1;

    cout << "new_size" << new_size << endl;
    char cstr12[new_size];
    strcpy(cstr12, cstr1);
    strcat(cstr12, " ");
    strcat(cstr12, cstr2);
    cout << cstr12 << endl;

    // 一维数组
    cout << endl << endl << "start ex3_41: " << endl;
    int arr4[4]  = {11, 12, 13, 14};
    vector<int> vec4(begin(arr4), end(arr4));
    for (const auto &item : arr4) {
        cout << item << " ";
    }

    // 打印数组
    int array_print[] = {1, 2, 3, 4, 5};
    auto pbeg_print = begin(array_print), pend_print = end(array_print);
    cout << " 打印数组元素: ";
    while (pbeg_print != pend_print) {
        cout << *pbeg_print++ << " ";
    }
    cout << endl;

    lnPrintln("\n习题 ------ ex3.24 两类迭代器用法");
    vector<int> vec(10, 1);
    for (auto &item : vec) {
        item *= 2;
    }
    for (const auto &item : vec) {
        cout << item << " ";
    }
    for (auto it = vec.cbegin(); it != vec.cend(); it++) {
        cout << *it << " ";
    }

    lnPrintln("\n习题 ------ ex3.24 两类迭代器用法");
    char a1[] = {'c' , 'p', 'p'};
    char a2[] = {'c' , 'p', 'p', '\0'};
    cout << "size of : " << endl;
    cout << sizeof a1 / sizeof(char) << endl;
    cout << sizeof a2 / sizeof(char) << endl;

    // ex3_42
    vector<int> vc{0, 1, 2, 3, 4};
    int a[5];

    int i = 0;
    for (const auto &item : vc) {
        a[i++] = item;
    }
    cout << "copy of a: ";
    for (auto ps = begin(a); ps != end(a); ps++) cout << *ps << " " ;

    // ex3_46
    cout << endl << endl << "start ex3_46: " << endl;
    for (auto ps = begin(a); ps != end(a); ps++) cout << *ps << " " ;
    constexpr size_t sz = 5;
    int ia[sz] = {0, 1, 2, 3, 4};
    int *pbeg = begin(ia);
    int *pend = end(ia);
    while (pbeg != pend) {
        cout << *pbeg << " " << endl;
        pbeg++;
    }

    // 多维数组的学习
    cout << endl << endl << "start ex3_43: " << endl;
    int arr[3][4] = {
            {00, 01, 02, 03},
            {10, 11, 12, 13},
            {20, 21, 22, 32}
    };
    for (const auto &row : arr) {
        for (const auto &col : row) {
            cout << col << " ";
        }
        cout << endl;
    }
    for (const int(&row)[4] : arr) {
        for (int col : row) {
            cout << col << " ";
        }
        cout << endl;
    }
    // for loop
    for (size_t i = 0; i != 3; i++) {
        for (size_t j = 0; j != 4; j++) {
            cout << arr[i][j] << " ";
        }
        cout << endl;
    }
    // means: typedef int_array_4 int[4]
    using int_array_4 = int[4];
    for (int_array_4 *row = arr; row != arr + 3; row++) {
    // for (int (*row)[4] = arr; row != arr + 3; row++) {
        for (int *col = *row; col != *row + 4; col++) cout << *col << " ";
        cout << endl;
    }

    cout << endl << "================= tmp ============" << endl;
    cout << -21 % -8 << endl;
    cout << 21 % -5 << endl;
    cout << -21 % 8 << endl;
    cout << (5 + 10 * 20/2) << endl;

    return 0;
};
