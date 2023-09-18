#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

// current file include
#include "Sales_data.h"
#include "tool.h"

using namespace  std;

// define current function
void check_and_print(vector<int> &vec) {
    cout << " vector<int> size: " << vec.size() << " content: ";
    for (const auto &item : vec) {
        cout << item << ",";
    }
    cout << endl;
}

void check_and_print(vector<string> &vec) {
    cout << " vector<string> size: " << vec.size() << " content: ";
    // use 迭代器
    for (auto it = vec.begin(); it != vec.end(); it++) {
        cout << *it << (it + 1 != vec.end() ? "," : "");
    }
    cout << endl;
}

// ex2.10
std::string global_str;
int global_int;

// g14 Sales_data.cpp ex2main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex2.1 ");
    vector<int> v1;
    vector<int> v2(10);
    vector<int> v3(10, 42);
    vector<int> v4{ 10 };
    vector<int> v5{ 10, 42 };
    vector<string> v6{ 10 };
    vector<string> v7{ 10, "hi" };

    check_and_print(v1);
    check_and_print(v2);
    check_and_print(v3);
    check_and_print(v4);
    check_and_print(v5);
    check_and_print(v6);
    check_and_print(v7);

    lnPrintln("\n习题 ------ ex2.10 ");
    std::string local_str;
    int local_int;
    std::cout << "global_str: " <<  global_str << std::endl;
    std::cout << "global_int: " <<  global_int << std::endl;
    std::cout << "local_str: " <<  local_str << std::endl;
    std::cout << "local_int: " <<  local_int << std::endl;

    lnPrintln("\n习题 ------ ex2.42 ");
    string str_bo("aaaa");
    Sales_data item(str_bo);
    // item.printHello();
    item.printString();

    // ----------------------------------------------
    // lnPrintln("\n习题 ------ ex10.36 ");
    // lnPrintln("\n章节 ------------------ 10.word_transformation_bad.txt");

    return 0;
};
