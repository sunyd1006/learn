//
// Created by sunyindong
//

#ifndef TEST_CPP_TOOL_H
#define TEST_CPP_TOOL_H

#include <iostream>
#include <list>
#include <map>
#include <sstream>
#include <string>
#include <vector>

inline void print(std::string str) { std::cout << str << " "; }

inline void println(const char *dis) { std::cout << dis << std::endl; }
inline void println() { println(""); }
inline void println_line(int howManyLn = 0) {
    while (howManyLn--) {
        println();
    }
    println("------------------------------");
}

inline void println(std::string str) { println(str.c_str()); }

inline void printlnCallFun(const std::string &str) {
    println(std::string("Call function (") + str + ")\t: ");
}

inline std::string printlnCallFunInfo(const std::string &func,
                                      std::initializer_list<std::string> info) {
    std::string ret = std::string("Call function (") + func + "):";
    for (const auto &item : info) {
        ret.append(", " + std::string(item));
    }
    println(ret);
    return ret;
}

inline void printlnCallFunInfo(const std::string &func, std::string info) {
    printlnCallFunInfo(func, {info});
}
inline void printlnCallFunInfo(const std::string &func, std::string info1, std::string info2) {
    printlnCallFunInfo(func, {info1, info2});
}

inline void printlnDebugTodo(std::string func) {
    println(func + " ------------------- debug for this");
}

inline void lnPrintln(const char *dis) {
    println();
    println(dis);
}

template <typename Pointer>
inline void printlnPointer(Pointer *ptr) {
    std::cout << "ptr: " << std::hex << ptr << std::endl;
}

template <typename Pointer>
inline std::string getHexPointer(Pointer *ptr) {
    std::stringstream out;
    out << "ptr: " << std::hex << ptr;
    return out.str();
}

template <typename Pointer>
inline void printlnPointer(Pointer *lhs, Pointer *rhs) {
    std::cout << std::hex << lhs << " vs " << std::hex << rhs << "  eql: " << (lhs == rhs)
              << std::endl;
}

using Map = std::map<std::string, std::size_t>;

inline void printMapItem(Map const &m) {
    for (auto const &kv : m) std::cout << kv.first << " : " << kv.second << "\n";
}

/**
 * todo: 如何 extern vector , 没有实现故而放弃
 * 转而向 ex11mian, ex10main, 里面添加了声明
 */
// define some variables
// extern std::vector<int> test_int_vec;
// extern std::vector<std::string> test_string_vec;
// extern std::list<int> test_int_list;
//
// extern const std::vector<int> int_vec{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
// extern const std::vector<int> int_du_vec{0, 1, 2, 3, 4, 5, 5, 5, 5, 5};
// extern const std::vector<std::string> str_vec{
//         "the", "quick", "red", "fox", "jumps", "over", "the", "slow", "red", "turtle"
// };

// define some functions
inline void test_clear() {
    // test_int_vec.clear();
    // test_string_vec.clear();
    // test_int_list.clear();
    // println(dis + std::string(" test_int_vec.size: ") + std::to_string(test_int_vec.size())
    //             + std::string(" test_string_vec.size: ") +
    //             std::to_string(test_string_vec.size()));
}

// inline void clear_println(const char *dis) {
//     println();
//     test_clear();
//     println(dis);
// }

// ----------------------- 模板声明 --------------------------
inline std::ostream &println(const int &seq);
// ----------------------- 模板声明 --------------------------

// print a container like vector, deque, list, etc.
template <typename Sequence>
inline std::ostream &println(Sequence const &seq, const std::string &dis) {
    std::cout << dis << " : ";
    for (auto const &elem : seq) {
        // if (typeid(seq) == typeid(std::pair<std::string, std::size_t>)) {
        //     std::cout << elem.first << " -> " << elem.second << " , ";
        // } else {
        //     std::cout << elem << " ";
        // }
        std::cout << elem << " ";
    }
    std::cout << std::endl;
    return std::cout;
}

template <typename Sequence>
inline std::ostream &println(Sequence const &seq) {
    return println(seq, "容器元素为");
}

#endif  // TEST_CPP_TOOL_H
