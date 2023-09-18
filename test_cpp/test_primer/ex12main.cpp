#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

// current file include
#include <memory> // for make_shared<>

#include "ex12.h"
#include "Sales_data.h"
#include "Sales_item.h"
#include "tool.h"
#include <sstream>
#include <fstream>

using namespace std;

// define current function
size_t static_count_func() {
    static size_t static_count_func_val = 0;
    return ++static_count_func_val;
}

// ex12.6
auto get_dynamic_vec() {
    return new vector<int>();
}

using dynamicVecPtr = vector<int> *;

auto init_dynamic_vec(dynamicVecPtr ptr) {
    // for (int tmp; cin >> tmp; ptr->push_back(tmp));
    for (int i = 0; i < 5; ++i) {
        ptr->push_back(i);
    }
    return ptr;
}

// ex12.7
using dynamicVecSPtr = shared_ptr<vector<int>>;

auto get_dynamic_sp_vec() {
    return make_shared<vector<int>>();
}

auto init_dynamic_vec(dynamicVecSPtr ptr) {
    for (int i = 0; i < 6; ++i) {
        ptr->push_back(i);
    }
    return ptr;
}

// ex12.14
struct SunConnection {
    string name;

    SunConnection();

    SunConnection(string _name) : name(_name) {};
};

void auto_free_use_connection(const string str) {
    SunConnection conn(str);
    println("正在使用: " + conn.name);
    auto deleteMethod = [](const SunConnection *ptr) {
        println("Delete " + (ptr->name) + " now.");
        // delete ptr;   // todo 此处模拟是应该用于关闭资源，而为何会报错呢?
    };
    // shared_ptr<SunConnection, decltype(deleteMethod) *> sptr(conPtr, deleteMethod);
    shared_ptr<SunConnection> sptr(&conn, deleteMethod);
    println("即将结束使用: " + (sptr->name));
}


// g14 tool.cpp Sales_data.cpp ex12.cpp ex12main.cpp -o main && ./main
int main() {
    lnPrintln("\n章节 ------------------ 12 测试 static 变量");
    for (auto i = 0; i != 5; ++i) {
        cout << static_count_func() << " ";
    }
    println();

    lnPrintln("\n习题 ------ ex12.2 ");
    const StrBlob csb{"hello", "world", "pezy"};
    StrBlob sb{"hello", "world", "pezy"};

    std::cout << csb.front() << " " << csb.back() << std::endl;
    sb.back() = "Mooophy";
    std::cout << sb.front() << " " << sb.back() << std::endl;

    lnPrintln("\n习题 ------ ex12.6 ");
    auto ex126 = init_dynamic_vec(get_dynamic_vec());
    println(*ex126);
    delete ex126;

    lnPrintln("\n习题 ------ ex12.7 ");
    auto ex127 = init_dynamic_vec(get_dynamic_sp_vec());
    println(*ex127);

    lnPrintln("\n习题 ------ ex12.14 ");
    auto_free_use_connection(string("CONNECTION"));

    lnPrintln("\n习题 ------ ex12.16 ");
    unique_ptr<string> ex16(new string("unique_ptr_value"));
    /**
    //  unique_ptr<string> p2(ex16); // copy
    //                      ^
    // Error: Call to implicitly-deleted copy constructor of 'unique_ptr<string>'
    //
    // unique_ptr<string> p3 = ex16; // assign
    //                      ^
    // Error: Call to implicitly-deleted copy constructor of 'unique_ptr<string>'
     */
    std::cout << *ex16 << std::endl;
    ex16.reset();
    // ex16.reset(nullptr);

    lnPrintln("\n习题 ------ ex12.20 ");
    std::ifstream ifs("./data/book.txt");
    // std::ifstream ifs("primer/data/book.txt");  // note: vscode 里面要用这个目录
    StrBlob blob;
    for (std::string str; std::getline(ifs, str);)
        blob.push_back(str);
    for (StrBlobPtr pbeg(blob.begin()), pend(blob.end()); pbeg != pend; pbeg.incr())
        std::cout << pbeg.deref() << std::endl;

    lnPrintln("\n习题 ------ ex12.22 ");
    const StrBlob const_blob(blob);
    for (StrBlobPtr pbeg(const_blob.begin()), pend(const_blob.end()); pbeg != pend; pbeg.incr())
        std::cout << pbeg.deref() << std::endl;


    lnPrintln("\n习题 ------ ex12.23 ");
    char *concatenate_string = new char[strlen("hello world") + 1]();
    strcat(concatenate_string, "hello ");
    strcat(concatenate_string, "world");
    std::cout << concatenate_string << std::endl;
    delete[] concatenate_string;

    std::string str1{"hello "}, str2{"world"};
    std::cout << str1 + str2 << std::endl;

    lnPrintln("\n习题 ------ ex12.24 ");
    std::cout << "How long do you want the string? default with 10" << endl;
    // int size{10};
    // char *input = new char[size + 1]();
    // std::cin.ignore();
    // std::cin.get(input, size + 1);
    // std::cout << input;
    // delete[] input;

    lnPrintln("\n习题 ------ ex12.26 ");
    std::allocator<std::string> alloc;
    const int DYNAMIC_ARRAY_SIZE = 2;
    auto const start = alloc.allocate(DYNAMIC_ARRAY_SIZE);
    std::string s;
    auto next_start = start;
    std::cout << "input " << DYNAMIC_ARRAY_SIZE << " str：";
    while (next_start != start + DYNAMIC_ARRAY_SIZE && std::cin >> s)
        alloc.construct(next_start++, s);

    while (next_start != start) {
        std::cout << *--next_start << " ";
        alloc.destroy(next_start);
    }
    alloc.deallocate(start, DYNAMIC_ARRAY_SIZE);

    lnPrintln("\n习题 ------ ex12.27 ");
    QueryText query27("data/storyDataFile.txt"); // 命令行请用这个
    auto ret27 = query27.find("thing");
    ret27.printResult();

    query27.find("sunyd").printResult();

    print("enter your word you wanna query: ");
    string word27;
    while (cin >> word27) {
        if (word27 == "eof") break;
        query27.find(word27).printResult();
    }

    // ----------------------------------------------
    // lnPrintln("\n习题 ------ ex10.36 ");
    // lnPrintln("\n章节 ------------------ 10.word_transformation_bad.txt");

    return 0;
};
