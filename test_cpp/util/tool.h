//
// Create by sunyindong on 2022/04/25
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
inline void println(std::string str) { println(str.c_str()); }
inline void println() { println(""); }
inline void println_line(int howManyLn = 0) {
    while (howManyLn--) {
        println();
    }
    println("------------------------------");
}

inline void printInfoLine(std::string info="") {
    std::string infoReal = info=="" ? " 分隔栏 " : " " + info + " ";
    std::string line="-----------------------";
    println(line + infoReal + line);
}

inline void printlnCallFun(const std::string &str) {
    println(std::string("Call function (") + str + ")");
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
