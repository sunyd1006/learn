#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>

#include <stdexcept>

#include "ex6.h"

using namespace std;

void printNone() {
}

// using arrTen = int[10];
// arrTen getArrTen(int [10] arrRow) {
//     return arrRow;
// }

// reference to stack memory associated with parameter 'i' returned
// int & getParmReference(int i) {
//     int &ref = i;
//     return ref;
// }

// 使用数组引用，数组指针(传递数组）
void printUsingArr(int (&arr)[5], int *arr2, int arr2sz) {
    for (const auto &item : arr) {
        cout << item << " ";
    }
    // 数组循环，非得这样写真实的慢呀
    for (int i = 0; i < arr2sz; i++) {
        cout << *arr2++ << " ";
    }
}

// void test(char & i);
void vector_dynamic_param_list(initializer_list<string> list) {
    for (const auto &item : list) {
        cout << item << " ";
    }
}

using Iter = vector<int>::const_iterator;

void printVecByIter(Iter start, Iter end) {
#ifndef NDEBUG
    cout << "NDEBUG: " << endl;
#endif
    cout << __func__ << ":";
    while (start != end) {
        cout << *start++ << " ";
    }
    cout << endl;
}

int get_size(int cinsz) {
    return cinsz;
}

int main() {
    cout << "\n case: 使用错误的引用参数 " << endl;
    // test: reference to stack memory associated with parameter 'i' returned
    // int testParamReference = 100;
    // auto ret = getParmReference(testParamReference);
    // cout << ret << endl;

    cout << "\n case: 使用错误的引用参数 " << endl;
    // test('a'); //  candidate function not viable: expects an l-value for 1st argument

    cout << "\n case: 使用数组引用，数组指针(传递数组） " << endl;
    int arr1[5] = {1, 2, 3};
    int arr2[5] = {1, 2, 3};
    printUsingArr(arr1, arr2, 5);


    cout << "\n case: 627" << endl;
    // initializer_list
    vector_dynamic_param_list({"hello", "world"});

    cout << "\n case: 使用迭代器访问元素 " << endl;
    vector<int> vec{1, 2, 3, 4};
    printVecByIter(vec.cbegin(), vec.cend());

    // 联系使用 NDEBUG
    cout << "\n case: 联系使用 NDEBUG " << endl;
    printVecByIter(vec.cbegin(), vec.cend());


    cout << endl << "================= tmp ============" << endl;
    int cinsz;
    cin >> cinsz;
    const int getsz = get_size(cinsz);
    // constexpr int newsz = getsz;  //  'get_size' cannot be used in a constant expression
    cout << getsz << endl;
    // int arr[10] = {1, word_transformation_bad.txt, 3};
    // auto ret= getArrTen(arr);
    // for (auto start = begin(ret); start != end(ret); ) {
    //     cout << *start++ << " " ;
    // }

    return 0;
};
