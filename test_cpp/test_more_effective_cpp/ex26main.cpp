//
// Create by sunyindong on 2022/04/25
//
#include <iostream>
#include <string>

// include current header file
#include "ex26.h"

// include util file
#include "tool.h"

using namespace std;

static inline void  static_inline_function_non_member_function() {
    println("hahaha");
}

int main(int argc, char const* argv[]) {
    EX26_SingletonDemo::getInstance().printInfo();
    EX26_SingletonDemo::getInstance().printInfo();

    lnPrintln("--------------- Ex26 单例-使用模板函数");
    try {
        Sigleton* ex2601 = Sigleton::makePrinter();
        println("\tSigleton build ok");

        Sigleton* ex2602 = Sigleton::makePrinter();
        println("\tSigleton build ok");

        Sigleton* ex2603 = Sigleton::makePrinter();
        println("\tSigleton build ok");

        Sigleton* ex2604 = Sigleton::makePrinter();
        println("\tSigleton build ok");
    } catch (Sigleton::TooManyObjects& me) {
        cout << me.what();
    }

    lnPrintln("--------------- Ex26 static inline non-member function");
    static_inline_function_non_member_function();
    EX26::static_inline_function_non_member_function();
    EX26::EX26_Test::class_static_inline_print();
    EX26::EX26_Test::class_static_implicit_inline_print();
    auto ret = EX26::EX26_Test::getUnique(10);
    println("Ex26 unique_ptr<int> test: " + std::to_string(*ret));


    // lnPrintln("---------------  ");

    return 0;
}

// g++ -o ex26.o -c -std=c++11 -I/Users/sunyindong/codespace/test_cpp/util ex26.cpp
// g++ -o ex26main.o -c -std=c++11 -I/Users/sunyindong/codespace/test_cpp/util ex26main.cpp
// g++ -std=c++11 -o  build/ex26 ex26.o ex26main.o

// g++ -I/Users/sunyindong/codespace/test_cpp/util ex26.cpp ex26main.cpp -o main && ./main