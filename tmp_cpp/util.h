#ifndef UTIL_CPP
#define UTIL_CPP

#include <iostream>
#include <list>
#include <map>
#include <sstream>
#include <string>
#include <vector>

inline void print(std::string str) { std::cout << str << " "; }
inline void println(const char* dis) { std::cout << dis << std::endl; }

// print a container like vector, tuple, deque, list, etc.
// 针对容器元素为std::pair类型的特化打印
template <typename T>
void print_element(const T& elem) {
    std::cout << elem << " ";
}

template <typename K, typename V>
void print_element(const std::pair<K, V>& elem) {
    std::cout << elem.first << " -> " << elem.second << " ";
}

// 打印容器中的所有元素
template <typename Sequence>
inline std::ostream& println(Sequence const& seq, const std::string& dis) {
    std::cout << dis;
    for (auto const& elem : seq) {
        print_element(elem);
    }
    std::cout << std::endl;
    return std::cout;
}

// 不带dis参数时使用默认值
template <typename Sequence>
inline std::ostream& println(Sequence const& seq) {
    return println(seq, "container values: ");
}

inline void printLog(const std::string& info) {
    // 获取当前时间
    auto now = std::chrono::system_clock::now();
    auto now_time_t = std::chrono::system_clock::to_time_t(now);
    std::tm* tm_ptr = std::localtime(&now_time_t);  // 转换为本地时间

    // 格式化时间为字符串
    std::ostringstream timeStream;
    timeStream << std::put_time(tm_ptr, "%Y-%m-%d %H:%M:%S");

    // 获取当前线程 ID
    std::thread::id threadID = std::this_thread::get_id();

    std::ostringstream out;
    out << "[Thread ID: " << threadID << "] "
        << "[Time: " << timeStream.str() << "] "
        << "Info: " << info << std::endl;

    // print at end
    std::cout << out.str();
}

inline std::vector<int> splitString(const std::string& input, char delimiter) {
    std::vector<int> result;
    std::stringstream ss(input);
    std::string token;
    while (getline(ss, token, delimiter)) {
        result.push_back(stoi(token));
    }
    return result;
}

// ========= println 指针 ==========
template <typename Pointer>
inline void printlnPointer(Pointer* ptr) {
    std::cout << "ptr: " << std::hex << ptr << std::endl;
}

template <typename Pointer>
inline std::string getHexPointer(Pointer* ptr) {
    std::stringstream out;
    out << "ptr: " << std::hex << ptr;
    return out.str();
}




#endif  // UTIL_CPP
