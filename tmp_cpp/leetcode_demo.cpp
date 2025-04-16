#include <bits/stdc++.h>
#include <iomanip>
#include "util.h"

void vector_demo() {
    std::vector<int> res{1};
    std::vector<int> another{2, 3};

    // 在尾部插入
    res.insert(res.end(), another.begin(), another.end());
    println(res); // 1, 2, 3
    // 在头部插入
    res.insert(res.begin(), another.begin(), another.end());
    println(res); // 2, 3, 1, 2, 3

    // 按降序排序
    std::vector<int> vec = {5, 2, 8, 1, 3};
    std::sort(vec.begin(), vec.end(), [](int a, int b) {
        return a > b;  // 降序
    });
    println( vec, "降序排列: ");

    // resize() 会改变size，相当于含参数n的构造函数；
    // 但是reserve() 不会改变size, 它只改变了capacity()
    //   1. vector<vector<int>> sortedInput(maxIdx + 1);
    //   2. vector<vector<int>> sortedInput;  sortedInput.resize(maxIdx+1);
}

void string_demo() {
    std::string str = "hello....world";
    char ch = '.';

    // find Idx func
    auto findIdxFunc = [](const std::string& str, char ch) -> int {
        auto it = std::find(str.begin(), str.end(), ch);
        if (it == str.end()) {
            return -1;
        }
        return it - str.begin();
    };
    assert(findIdxFunc(str, ch) == 5);


    // 删除一个字符的函数
    auto removeCharFunc = [](const std::string& raw, char one) -> std::string {
        std::string res = raw;
        // std::remove one item.
        res.erase(std::remove(res.begin(), res.end(), one), res.end());
        return res;
    };
    assert( removeCharFunc(std::string("hello_world"), '_') == std::string("helloworld"));

    // 解释
    std::string str2 = "hello....world";
    std::remove(str2.begin(), str2.end(), ch);
    assert(str2 == "helloworldorld");
    str.erase(std::remove(str.begin(), str.end(), ch), str.end());
    assert(str == "helloworld");
}

int main() {

    vector_demo();
    string_demo();

    return 0;
}