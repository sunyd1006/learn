#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>
#include <stdexcept>

// current file include
#include "ex13.h"
#include "Sales_data.h"
#include "Sales_item.h"
#include "tool.h"
#include <sstream>
#include <fstream>

using namespace std;

// define current function
void func13(const EX13 &rx, EX13 x) {
    println("\tfunc13(const EX13 &rx, EX13 x)形参传入的是非引用形参，故而打印: " + x.name);
    std::vector<EX13> vec;
    println("\tstd::vector<EX13> vec; 未利用EX13, 故而不打印: ");
    vec.reserve(2);
    println("\tvec.reserve(2); 故而不打印: " + x.name);
    vec.push_back(rx);
    println("\tvec.push_back(rx); 因为vector的push_back定义是(const_reference& param), 即param是引用形参，故而此处使用拷贝构造函数EX13(const EX13&)");
    vec.push_back(x);
    println("\tvec.push_back( x); 因为vector的push_back定义是(const_reference& param), 即param是引用形参，故而此处使用拷贝构造函数EX13(const EX13&)");
    rx.printIsConstObj(x);
    println("\t [ rx.printIsConstObj(x) ] x是非常量，故而调用 void printIsConstObj(EX13 &item) ");
    x.printIsConstObj(rx);
    println("\t [ x.printIsConstObj(rx) ] rx是常量，故而调用 void printIsConstObj(const EX13 &item) ");
}

// ex13_16
void func16_nonRef(Numbered16 s) {
    std::cout << __func__ << " classId: " << std::to_string(s.classId) << "\tobjId: " << std::to_string(s.objId)
              << std::endl;
}

void func16_constRef(const Numbered16 &s) {
    std::cout << __func__ << "classId: " << std::to_string(s.classId) << "\tobjId: " << std::to_string(s.objId)
              << std::endl;
}

// ex13.44
void printNonRefString(String x) {
    std::cout << x.c_str() << std::endl;
}

void printConstStringRef(const String &x) {
    std::cout << x.c_str() << std::endl;
}

String getString() {
    String ret("world");
    return ret;
}


// g14 Sales_data.cpp ex13main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex13.5 ");
    HasPtrLikeValue ptr5("Hello World"), ptr501(ptr5);
    ptr5.printInfo();
    ptr501.printInfo();

    lnPrintln("\n习题 ------ ex13.8 ");
    HasPtrLikeValue ptr8("Hello World"), ptr801("Primer");
    ptr8.printInfo();
    ptr8 = ptr801;
    ptr8.printInfo();
    ptr801.printInfo();

    lnPrintln("\n习题 ------ ex测试未考虑 自己给自己赋值场景的 operator函数 ");
    ptr801 = ptr801;
    ptr801.printInfo();

    /** todo 错误写法: 此处应为没有 delete ps 等危险操作，暂时安全
        HasPtrLikeValue &operator=(const HasPtrLikeValue &right) {
            *ps = *(right.ps);
            i = right.i;
            return *this;
        }
     */

    {
        lnPrintln("\n习题 ------ ex13.13 ");
        EX13 ex13("ex13");
        const EX13 con_ex13("con_ex13");
        func13(con_ex13, ex13);

        lnPrintln("\n习题 ------ 测试构造函数匹配，拷贝构造 ");
        EX13 ex13_test = std::string("this is ex13test using string init it"), ex13_test2 = ex13_test;
        println("应该打印出：" + std::string("EX13(): ******"));
        println("应该打印出：" + std::string("看定义没有EX13(EX13&) 或者 EX13(const EX13&): ******"));

    }

    Numbered16 ex16a, ex16b = ex16a, ex16c = ex16b;
    lnPrintln("\n习题 ------ ex13.15 ");
    func16_nonRef(ex16a);  // 13
    func16_nonRef(ex16b);  // 14
    func16_nonRef(ex16c);  // 15

    lnPrintln("\n习题 ------ ex13.16 ");
    func16_constRef(ex16a);  // 10
    func16_constRef(ex16b);  // 11
    func16_constRef(ex16c);  // 12

    lnPrintln("\n习题 ------ ex13.18 ");
    Employee18 ex18default, ex18("MaYun");
    ex18default.printInfo();
    ex18.printInfo();

    lnPrintln("\n习题 ------ ex13.21 ");
    HasPtrLikeValue ex21default, ex21copy = ex21default;
    ex21default.printInfo();
    ex21copy.printInfo();

    lnPrintln("\n习题 ------ ex13.27 ");
    HasPtrLikePointer ex27default, ex27copy = ex27default;
    ex27default.printInfo();
    ex27copy.printInfo();

    HasPtrLikePointer ex2702("NewHelloWorld");
    ex2702.printInfo();

    lnPrintln("\n习题 ------ ex13.30 ");
    HasPtrLikeValue ex30left("left"), ex30right("right");
    ex30left.printInfo();
    ex30right.printInfo();
    swap(ex30left, ex30right);
    ex30left.printInfo();
    ex30right.printInfo();

    lnPrintln("\n习题 ------ ex13.31 ");
    HasPtrLikeValue ex3101{"ex3101"}, ex3102{"ex3102"}, ex3103{"ex3103"}, ex3105{"ex3105"};
    std::vector<HasPtrLikeValue> ex31{ex3105, ex3101, ex3103, ex3102};
    std::sort(ex31.begin(), ex31.end());

    for (auto &elem : ex31) {
        elem.printInfo();
    }

    lnPrintln("\n习题 ------ ex13.33-38 请看 ex13.h ");
    Message msg33_01("msg33_01"), msg33_02("msg33_02");
    Folder fold33_01("fold33_01"), fold33_02("fold33_02"), fold33_03("fold33_03");
    msg33_01.addFolderAndHelpFolder(fold33_01);
    msg33_02.addFolderAndHelpFolder(fold33_02);
    msg33_01.addFolderAndHelpFolder(fold33_03);

    cout << std::hex << &msg33_01 << " " << msg33_01 << endl;
    cout << std::hex << &msg33_02 << " " << msg33_02 << endl;
    cout << std::hex << &fold33_01 << " " << fold33_01 << endl;
    cout << std::hex << &fold33_02 << " " << fold33_02 << endl;
    cout << std::hex << &fold33_03 << " " << fold33_03 << endl;

    lnPrintln("\n习题 ------ ex13.39 ");
    StrVec vec39;
    vec39.reserve(2);
    cout << vec39 << endl;
    vec39.push_back("hello1");
    cout << vec39 << endl;
    vec39.push_back("hello2");
    cout << vec39 << endl;
    vec39.push_back("hello3");
    cout << vec39 << endl;
    vec39.push_back("hello4");
    vec39.push_back("hello5");
    cout << vec39 << endl;
    vec39.push_back("hello6");

    /**
        todo: 拷贝赋值过程中，处理自赋值时新建对象是走的拷贝函数，而新建的对象又在局部作用域失效后被析构，故而出错
        auto rhs_tmp = rhs;
        free();
        elements = rhs_tmp.elements;
        first_free = cap = rhs_tmp.cap;
        return *this;

        处理自赋值, 但这里rhs_tmp 这个对象会被析构，且rhs_tmp其析构函数释放了一个[elements, cap]故而报错
        main(40957,0x1003d0580) malloc: *** error for object 0x600003db8000: pointer being freed was not allocated
        main(40957,0x1003d0580) malloc: *** set a breakpoint in malloc_error_break to debug
     */

    lnPrintln("\n习题 ------ ex13.39 测试自赋值");
    vec39 = vec39;
    cout << vec39 << endl;

    {
        lnPrintln("\n习题 ------ ex13.40 ");
        StrVec ex40 = {"hello", "world", "ma", "yun", "ha!"};
        cout << ex40 << endl;
    }

    lnPrintln("\n习题 ------ ex13.44 ex13.48 ");
    char text[] = "world";
    String ex4401, ex4402("hello"), ex4403(ex4401), ex4404 = ex4402, ex4405(text);
    println("------------------------- line ");
    ex4403 = ex4402;
    println("------------------------- line ");
    printNonRefString(ex4402);
    println("------------------------- line 3");
    printConstStringRef(ex4402);
    printNonRefString("temporary");
    printConstStringRef("temporary");
    String ex4406Ret = getString();

    std::vector<String> svec;
    svec.reserve(8);
    println("------------------------- line 4 ");
    svec.push_back(ex4401);
    svec.push_back(ex4402);
    svec.push_back(ex4403);
    svec.push_back(ex4404);
    svec.push_back(ex4405);
    svec.push_back(ex4406Ret);
    svec.push_back(getString());
    svec.push_back("good job");

    for (const auto &s : svec) {
        std::cout << s.c_str() << "\t";
    }
    std::cout << std::endl;

    lnPrintln("\n习题 ------ ex13.50 ");
    std::vector<String> svec50;
    String ex50 = getString();
    svec50.push_back(ex50);

    lnPrintln("\n习题 ------ ex13.50 ");
    EX58().sorted(); // call "&&"
    EX58 ex58;
    ex58.sorted(); // call "const &"

    // ----------------------------------------------
    // lnPrintln("\n习题 ------ ex ");
    // lnPrintln("\n章节 ---\t10 ");

    return 0;
};
