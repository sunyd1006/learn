#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>
#include <map>

// current file include
#include "ex13.h"
#include "ex14.h"
#include "Sales_data.h"
#include "Sales_item.h"
#include "tool.h"
#include <sstream>
#include <fstream>
#include <functional>

using namespace std;

// define current function

int sunAdd(int i, int j) { return i + j; }

auto mod = [](int i, int j) { return i % j; };

struct Div {
    int operator()(int i, int j) const { return i / j; }
};


// g14 Sales_data.cpp ex14main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex14.7 ");
    Sales_data ex6("bookName");
    cout << ex6 << endl;

    lnPrintln("\n习题 ------ ex14.9 ");
    Sales_data ex9;
    print("please input a Sales_data item(bookname, soldNumber, price): ");
    istringstream in("bookname 10 20");
    in >> ex9;
    cout << ex9 << endl;

    lnPrintln("\n习题 ------ ex14.22  ");
    /*
        // Sales_data &operator=(const std::string &rhs) {
        //     println("call operator(const std::string &rhs)");
        //     Sales_data tmp = *this;
        //     tmp.bookNo = rhs;
        //     return tmp; // todo 返回局部变量的引用，竟然不报错？
        // }
     */
    Sales_data ex22;
    ex22 = "ex22name";
    cout << ex22 << endl;

    lnPrintln("\n习题 ------ ex14.23  ");
    StrVec ex23;
    ex23 = {"hello", "strvec", "using", "list"};
    cout << ex23 << endl;

    lnPrintln("\n习题 ------ ex14.35  ");
    GetInput getInput;
    print("please input a line: ");
    std::cout << getInput() << std::endl;

    lnPrintln("\n习题 ------ ex14.36  ");
    std::vector<std::string> vec36;
    print("please input a line until you input eof: ");
    for (std::string tmp; !(tmp = getInput()).empty();) {
        if (tmp == "eof") break;
        vec36.push_back(tmp);
    }
    for (const auto &str : vec36) std::cout << str << " ";

    lnPrintln("\n习题 ------ ex14.37  ");
    std::vector<int> vec = {3, 2, 1, 4, 3, 7, 8, 6};
    std::replace_if(vec.begin(), vec.end(), IsEqual(3), 325555);
    for (int i : vec) std::cout << i << " ";
    std::cout << std::endl;

    lnPrintln("\n习题 ------ ex14.38  ");
    //create predicates with various upper limits.
    std::size_t lower = 1;
    auto uppers = {3u, 4u, 5u, 6u, 7u, 8u, 9u, 10u, 11u, 12u, 13u, 14u};
    std::vector<IsInRange> predicates;
    for (auto upper : uppers)
        predicates.push_back(IsInRange{lower, upper});

    //create count_table to store counts
    std::map<std::size_t, std::size_t> count_table;

    //read file and count
    std::ifstream fin("./data/storyDataFile.txt");
    for (std::string word; fin >> word; /* */)
        for (auto is_size_in_range : predicates)
            if (is_size_in_range(word)) // 函数调用运算符() 使用的地方
                ++count_table[is_size_in_range.upper_limit()];

    for (auto pair : count_table) {
        std::cout << "count in range [1, " << pair.first << "] : " << pair.second << std::endl;
    }

    lnPrintln("\n习题 ------ ex14.42 ");
    vector<int> vec42 = {1, 2, 3, 4, 5, 1024, 1025};
    auto ret381 = std::count_if(vec42.begin(), vec42.end(),
                                std::bind(std::greater_equal<int>(), std::placeholders::_1, 1024));
    println(vec42, std::to_string(ret381));
    auto ret382 = std::find_if(vec42.begin(), vec42.end(), std::bind(std::greater<int>(), std::placeholders::_1, 1024));
    auto ret383 = std::transform(vec42.begin(), vec42.end(), vec42.begin(),
                                 std::bind(std::multiplies<int>(), std::placeholders::_1, 2));
    println(vec42);

    lnPrintln("\n习题 ------ ex14.43  ");
    int input = 2014;
    std::modulus<int> mod;
    auto predicator = [&](int i) { return 0 == mod(input, i); };
    auto is_divisible = std::all_of(vec42.begin(), vec42.end(), predicator);
    std::cout << (is_divisible ? "Yes!" : "No!") << std::endl;

    lnPrintln("\n习题 ------ ex14.44  ");
    std::map<std::string, std::function<int(int, int)>> opsMap;
    // opsMap.insert(std::pair<std::string, std::function<int(int, int)>>("+", sunAdd));       // function pointer
    opsMap.insert({"+", sunAdd});                                                       // function pointer
    opsMap.insert({"-", std::minus<int>()});                                        // library functor
    opsMap.insert({"*", [](int i, int j) { return i * j; }});                       // unnamed lambda
    opsMap.insert({"/", Div()});                                                    // user-defined functor
    opsMap.insert({"%", mod});                                                          // named lambda object

    vector<string> ex44 = {"1 + 1", "1 - 1", "1 * 1", "1 / 1", "1 % 1"};
    for (const auto &item : ex44) {
        istringstream in(item);
        int lhs, rhs;
        std::string op;
        in >> lhs >> op >> rhs;
        std::cout << (item + " =\t") << opsMap[op](lhs, rhs) << std::endl;
    }

    // ---------------------------------------------
    // lnPrintln("\n习题 ------ ex ");
    // lnPrintln("\n章节 ---\t10 ");

    return 0;
};
