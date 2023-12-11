//
// Create by sunyindong on 2022/04/25
//
#ifndef EX_26_H
#define EX_26_H

#include <iostream>
#include <string>

#include "tool.h"

// Ex26
class EX26_SingletonDemo {
private:
    std::string name;
    EX26_SingletonDemo() : name("defaultName") {}

public:
    // 1. must return reference 2. must static mem-function
    static EX26_SingletonDemo& getInstance() {
        static EX26_SingletonDemo item;
        return item;
    }
    void printInfo() {
        print(name);
        printlnPointer(this);
    }
};

// ex26 using template
template <typename BeingCounted>
class Counted {
private:
    static int objNumber;
    static size_t objMaxNumer;
    void init();

protected:
    Counted();
    Counted(const Counted& rhs);
    ~Counted() { --objNumber; }

public:
    static int getObjNumber() { return objNumber; }
    class TooManyObjects : public std::exception {  //抛异常
    public:
        TooManyObjects() : std::exception() {}
    };
};

class Sigleton : private Counted<Sigleton> {
private:
    std::string name;
    Sigleton() : name("defaultName") {}
    Sigleton(const Sigleton& rhs) : name("defaultName") {}

public:
    static Sigleton* makePrinter();
    static Sigleton* makePrinter(const Sigleton& rhs);

    // ask: 为什么定义 static inline function 就出问题了
    // static Sigleton* makePrinter() { return new Sigleton; };
    // static Sigleton* makePrinter(const Sigleton& rhs) { return new Sigleton(rhs); };

    ~Sigleton() {}
    using Counted<Sigleton>::getObjNumber;
    using Counted<Sigleton>::TooManyObjects;
};

namespace EX26 {
static inline void static_inline_function_non_member_function() { println("hahaha"); }

class EX26_Test {
public:
    static inline void class_static_inline_print() {
        println("EX26_Test::class_static_inline_print");
    }
    static void class_static_implicit_inline_print() {
        println("EX26_Test::class_static_implicit_inline_print");
    }

    static std::unique_ptr<int> getUnique(int param) {
        return std::unique_ptr<int>(new int(param));
    }
};

}  // namespace EX26

#endif  // EX_26_H