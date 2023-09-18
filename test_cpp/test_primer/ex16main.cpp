#include <cstring>
#include <iostream>
#include <iterator>
#include <stdexcept>
#include <vector>

// current file include
#include <fstream>
#include <sstream>
#include <unordered_set>

#include "Sales_data.h"
#include "Sales_item.h"
#include "ex16.h"
#include "tool.h"

using namespace std;

// define current function
struct Foo {
    Foo() { std::cout << "Foo()\n"; }

    Foo(const string &_name) : name(_name) { println("Foo() " + name); }

    ~Foo() { println("~Foo() " + name); }

    Foo(const Foo &) { println("Foo() copy ctor" + name); }

    Foo(Foo &&) { println("Foo() move cto" + name); }

    std::string name;
};

struct Fooo {
    Fooo(int n = 0) noexcept : bar(n) { std::cout << "Fooo: constructor, bar = " << bar << '\n'; }

    ~Fooo() { std::cout << "Fooo: destructor, bar = " << bar << '\n'; }

    int GetBar() const noexcept { return bar; }

private:
    int bar;
};

struct UserDefinedDeleter {
    void bar() { std::cout << "Call deleter UserDefinedDeleter::bar()...\n"; }

    void operator()(Foo *p) const {
        std::cout << "Call delete from function object...\n";
        delete p;
    }
};

// define some global variable
list<string> test_list_str{"hello", "world", "yeah!"};
vector<string> test_vec_str{"hello", "world", "yeah!"};

// g14 tool.cpp Sales_data.cpp ex16main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex16.2 ");
    cout << SunCompare(10, 20) << endl;

    lnPrintln("\n习题 ------ ex16.3 ");
    Sales_data ex301("ex301"), ex302("ex302");
    /*
     * error: invalid operands to binary expression ('const Sales_data' and 'const Sales_data')
     */
    // cout << compare(ex301, ex302) << endl;

    lnPrintln("\n习题 ------ ex16.4 ");
    cout << *SunFind(test_list_str.cbegin(), test_list_str.cend(), "world") << endl;
    cout << *SunFind(test_vec_str.cbegin(), test_vec_str.cend(), "world") << endl;

    lnPrintln("\n习题 ------ ex16.5 ");
    int array[10] = {0, 1, 2, 3, 4};
    printlnArray(array);
    printlnArray2(array);

    lnPrintln("\n习题 ------ ex16.16 ");
    Vec<std::string> vec16 = {"hello", "this", "is", "Vec<string>"};
    for (const auto &item : vec16) {
        std::cout << item << " ";
    }
    println();

    lnPrintln("\n习题 ------ ex16.19 ");
    SunPrint(test_vec_str);

    lnPrintln("\n习题 ------ ex16.21 ");
    {
        double *p = new double(8.0);
        DebugDelete d;
        d(p);

        int *ip = new int;
        DebugDelete()(ip);

        std::unique_ptr<int, DebugDelete> up(new int(0), DebugDelete());
        std::unique_ptr<string, DebugDelete> usp(new string("defaultStr"), DebugDelete());
    }

    lnPrintln("\n习题 ------ ex16.28 自定义 SharedPtr UniquePtr ");

    lnPrintln("SharedPtr constructor with no managed object ");
    { SharedPtr<Foo> sh1; }
    lnPrintln("SharedPtr constructor with object ");
    {
        SharedPtr<Foo> sh2(new Foo);
        std::cout << sh2.use_count() << '\n';
        SharedPtr<Foo> sh3(sh2);
        std::cout << sh3.use_count() << '\n';
    }

    lnPrintln("SharedPtr constructor with deleter ");
    {
        SharedPtr<Foo> sh5(new Foo, [](Foo *p) {
            std::cout << "\tCall delete from lambda...\n";
            delete p;
        });
    }
    lnPrintln("SharedPtr constructor with reset ");
    {
        SharedPtr<Fooo> sptr(new Fooo(1));
        std::cout << "The first Fooo's bar is " << sptr->GetBar() << "\n";
        println_line();
        sptr.reset(new Fooo);
        std::cout << "The second Fooo's bar is " << sptr->GetBar() << "\n";
    }
    {
        lnPrintln("======================\nUniquePtr constructor: ");
        UniquePtr<Foo> up1;
        UniquePtr<Foo> up1b(nullptr);
        UniquePtr<Foo> up2(new Foo("up2"));

        DebugDelete d;
        UniquePtr<Foo, DebugDelete> up3(new Foo("up3"), d);
        UniquePtr<Foo, DebugDelete &> up3b(new Foo, d);
        UniquePtr<Foo, DebugDelete> up4(new Foo, DebugDelete());
        UniquePtr<Foo> up5b(std::move(up2));  // up2 will be destroy.
        UniquePtr<Foo, DebugDelete> up6b(std::move(up3));

        println_line();
        UniquePtr<Foo> up7 = std::move(up5b);
        Foo *fp = up7.release();
        println("up2 will be destroy");
        assert(up7.get() == nullptr);
        delete fp;

        println("---------- UniquePtr reset");
        up6b.reset(new Foo("up6bNew"));
        up6b.reset(nullptr);

        println_line();
        UniquePtr<Fooo> up71(new Fooo(1));
        UniquePtr<Fooo> up72(new Fooo(2));

        println("---------- UniquePtr swap ");
        up71.swap(up72);
        std::cout << "up71->val:" << up71->GetBar() << std::endl;
        std::cout << "up72->val:" << (up72.get())->GetBar() << std::endl;

        println("---------- UniquePtr deleter define by user. ");
        UniquePtr<Foo, UserDefinedDeleter> up8(new Foo("up8"), UserDefinedDeleter());
        UserDefinedDeleter &del = up8.get_deleter();
        del.bar();

        lnPrintln("------------end");
    }

    std::unique_ptr<std::string> test;

    lnPrintln("\n习题 ------ ex16.37- ");
    int a = 6;
    double b = 5.1231;
    std::cout << std::max<long double>(a, b) << std::endl;

    lnPrintln("\n习题 ------ ex16. ");
    auto ret = std::make_shared<std::string>();
    println(" *ret: " + *ret);

    lnPrintln("\n习题 ------ ex16.39 传输两个数组 ");
    int ex_arr1[10] = {1, 2, 3, 4};
    int ex_arr2[12] = {1, 2, 3, 4};
    comparePrintByTwoArrayRef(ex_arr1, ex_arr2);
    cout << compare<string>("abcd", "abcde") << endl;

    lnPrintln("\n习题 ------ ex16.40 ");
    Test40 ex401(10), ex402(20);
    auto ret40 = ex401 + ex402;
    println(to_string(ret40.val));

    lnPrintln("\n习题 ------ ex16.47 ");
    int willChangeByFun = 0;
    flip(func1, willChangeByFun, 42);
    flip(func2, willChangeByFun, 42);

    lnPrintln("\n习题 ------ ex16.51 ");
    int i = 0;
    double d = 3.14;
    string s = "how";
    printLen(i, s, 42, d);  // : int(rvalue), double        | sizeof...(Args): 3  sizeof...(rest): 3
    printLen(s, 42, "hi");  // : int(rvalue), const char[3] | sizeof...(Args): 2  sizeof...(rest): 2
    printLen(d, s);         // : string                     | sizeof...(Args): 1  sizeof...(rest): 1
    printLen("hi");         // : None                       | sizeof...(Args): 0  sizeof...(rest): 0
    printLen(i, s, s, d);   // : string, string, double     | sizeof...(Args): 3  sizeof...(rest): 3

    lnPrintln("\n习题 ------ ex16.53 ");
    print(std::cout, 1);                  //! printing one argument
    print(std::cout, 1, 2);               //! printing two argument
    print(std::cout, 1, 2, 3, 4, "sss");  //! printing 5 arguments

    lnPrintln("\n习题 ------ ex16.56 ");
    int *ptr53 = nullptr;
    errorMsg(std::cout, 1, 2, 3, 4, 9.0f, "sss", "alan", ptr53);
    println();

    lnPrintln("\n习题 ------ ex16.61  ");
    Vec<string> vec58;
    vec58.emplace_back(10, 'c');
    vec58.emplace_back("this is a line");
    for (const auto &item : vec58) {
        print(item);
    }
    println();

    lnPrintln("\n习题 ------ ex16.62  ");
    std::unordered_multiset<Sales_data> mset;
    Sales_data sd("Bible", 10, 0.98);

    mset.emplace(sd);
    mset.emplace("C++ Primer", 5, 9.99);

    for (const auto &item : mset) {
        std::cout << "the hash code of " << item.getIsbn() << ":\n0x  " << std::hex
                  << std::hash<Sales_data>()(item) << "\n";
    }

    lnPrintln("\n习题 ------ ex16.63/64  ");
    vector<int> ex63int = {1, 2, 3, 3, 4, 5};
    std::vector<const char*> ex63str = {"alan","alan","alan","alan","moophy"};

    std::cout << count(ex63int, 3) << endl;
    std::cout << count(ex63str, "alan");




    
    // lnPrintln("\n章节 ------------------ 10 ");

    return 0;
};
