#include <cstring>
#include <iostream>
#include <iterator>
#include <stdexcept>
#include <vector>

// current file include
#include <bitset>
#include <tuple>

// old common file include
#include <fstream>
#include <sstream>

#include "Sales_data.h"
#include "Sales_item.h"
#include "ex19.h"
#include "tool.h"

using namespace std;

// define current function
// ex19.12
void ptrTest() {
    const string Screen::*pdata;
    pdata = &Screen::contents;  // must be in class or in a friend
    Screen myScreen(20, 20, 'x'), *pScreen = &myScreen;
    cout << myScreen.*pdata << endl;
    cout << pScreen->*pdata << endl;
}

// ex19.15
void callAvgPriceByMenFuncPtr(const Sales_data &item) {
    using Avg = double (Sales_data::*)() const;  // 类型别名
    Avg price = &Sales_data::getAvgPrice;
    cout << (item.*price)() << endl;
}

// 本章答案参考：https://github.com/chihyang/CPP_Primer/blob/master/chap19/Exer19_03_04.cpp
// g14 tool.cpp Sales_data.cpp ex19main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex19.3 ");
    A *pa1 = new C;
    if (B *pb1 = dynamic_cast<B *>(pa1)) {
        cout << "(a): cast succeeds!" << endl;
    } else {
        cout << "(a): cast fails!" << endl;
    }
    // (b)
    B *pb2 = new B;
    // fail, dynamic type of pb2 is not C or derived class of C
    if (C *pc1 = dynamic_cast<C *>(pb2)) {
        cout << "(b): cast succeeds!" << endl;
    } else {
        cout << "(b): cast fails!" << endl;
    }
    // (c)
    // A *pa2 = new D;          // ERROR
    // if (B *pb3 = dynamic_cast<B *>(pa2)) {
    //     cout << "(c): cast succeeds!" << endl;
    // } else {
    //     cout << "(c): cast fails!" << endl;
    // }

    lnPrintln("\n习题 ------ ex19.4 ");
    try {
        C &cref = dynamic_cast<C &>(*pa1);
        cref.print();
    } catch (const std::exception &e) {
        std::cerr << e.what() << '\n';
    }

    lnPrintln("\n习题 ------ ex19.6_8 ");
    {
        using EX196_8::AndQuery;
        using EX196_8::OrQuery;
        using EX196_8::Query;
        using EX196_8::Query_base;

        Query_base *query_baseptr_1 = new AndQuery(Query("value1"), Query("value2"));
        Query_base *query_baseptr_2 = new OrQuery(Query("value1"), Query("value2"));
        if (AndQuery *pa1 = dynamic_cast<AndQuery *>(query_baseptr_1)) {
            cout << "cast succeeds!" << endl;
        } else {
            cout << "cast fails!" << endl;
        }
        if (AndQuery *query_baseptr_2 = dynamic_cast<AndQuery *>(pb2)) {
            cout << "cast succeeds!" << endl;
        } else {
            cout << "cast fails!" << endl;
        }

        lnPrintln("\n习题 ------ ex19.7 ");
        try {
            AndQuery &ra1 = dynamic_cast<AndQuery &>(*query_baseptr_1);
            cout << "cast succeeds!" << endl;
        } catch (bad_cast e) {
            cout << e.what() << endl;
        }
        try {
            AndQuery &ra2 = dynamic_cast<AndQuery &>(*pb2);
            cout << "cast succeeds!" << endl;
        } catch (bad_cast e) {
            cout << e.what() << endl;
        }

        lnPrintln("\n习题 ------ ex19.8 ");
        if (typeid(*query_baseptr_1) == typeid(*pb2))
            cout << "query_baseptr_1 and pb2 points to the same type" << endl;
        else
            cout << "dynamic types of query_baseptr_1 and pb2 are different" << endl;
        if (typeid(*query_baseptr_1) == typeid(AndQuery))
            cout << "dynamic type of query_baseptr_1 is AndQuery" << endl;
        else
            cout << "dynamic type of query_baseptr_1 is not AndQuery" << endl;
        if (typeid(*query_baseptr_2) == typeid(AndQuery))
            cout << "dynamic type of query_baseptr_2 is AndQuery" << endl;
        else
            cout << "dynamic type of query_baseptr_2 is not AndQuery" << endl;
    }

    lnPrintln("\n习题 ------ ex19.9 ");
    {
        using namespace EX19_9_10;

        int arr[10];
        Derived d;
        Base *p = &d;
        cout << typeid(42).name() << "\n"
             << typeid(arr).name() << "\n"
             << typeid(Sales_data).name() << "\n"
             << typeid(std::string).name() << "\n"
             << typeid(p).name() << "\n"
             << typeid(*p).name() << "\n"
             << endl;
        println("translate 函数用于将typeid().name()返回的C风格字符串转成类类型");
        translate(cout, typeid(42)) << "\n";
        translate(cout, typeid(arr)) << "\n";
        translate(cout, typeid(Sales_data)) << "\n";
        translate(cout, typeid(std::string)) << "\n";
        translate(cout, typeid(p)) << "\n";
        translate(cout, typeid(*p)) << endl;

        {
            using EX19_9_10::A;
            using EX19_9_10::B;
            using EX19_9_10::C;

            lnPrintln("\n习题 ------ ex19.10 ");
            A *pa = new C;
            cout << typeid(pa).name() << endl;
            C cobj;
            A &ra = cobj;
            cout << typeid(&ra).name() << endl;
            B *px = new B;
            A &ra1 = *px;
            cout << typeid(ra1).name() << endl;
        }
    }

    lnPrintln("\n习题 ------ ex 枚举类型 ");
    LimitedEnum enum1 = LimitedEnum::ZhangSanFeng;
    UnlimitedEnum name = SunYindong;

    callUsingEnum(name);

    lnPrintln("\n习题 ------ ex19.12 ");
    ptrTest();
    const string Screen::*pdata12 = Screen::data();
    Screen myScreen(5, 5, 'a'), *pScreen = &myScreen;
    cout << myScreen.*pdata12 << endl;
    cout << pScreen->*pdata12 << endl;

    lnPrintln("\n习题 ------ ex19.13 ");
    Sales_data item13("978-7-121-20038-0", 120, 128.0), *pitem13 = &item13;
    const string Sales_data::*pdata13 = Sales_data::data();
    cout << item13.*pdata13 << endl;
    cout << pitem13->*pdata13 << endl;

    lnPrintln("\n习题 ------ ex19.15 ");
    Sales_data item1501("978-7-121-20038-0", 12, 128.0), item1502("978-7-121-20038-0", 10, 98.0);
    callAvgPriceByMenFuncPtr(item1501 + item1502);

    lnPrintln("\n习题 ------ ex19.18 成员函数指针 转为可调用对象");
    vector<string> ex18{"sun", "yin", "dong", "", "", "", "hu"};

    function<bool(const string &)> func_empty = &string::empty;
    println("多少个空元素： ");
    auto ex18ret = count_if(ex18.begin(), ex18.end(), func_empty);
    cout << ex18ret << " ";
    ex18ret = count_if(ex18.begin(), ex18.end(), mem_fn(&string::empty));
    cout << ex18ret << " ";
    ex18ret = count_if(ex18.begin(), ex18.end(), bind(&string::empty, std::placeholders::_1));
    cout << ex18ret << " ";

    lnPrintln("\n习题 ------ ex19.21");
    {
        using EX19_21::Token;
        string s = "this is a string";
        Sales_data item("978-7-121-20038-0", 12, 128.0);
        int i = 12;
        char c = 'c';
        double d = 1.28;

        Token tkn;
        tkn = i;
        cout << tkn << endl;
        tkn = c;
        cout << tkn << endl;
        tkn = d;
        cout << tkn << endl;
        tkn = s;
        cout << tkn << endl;
        tkn = item;
        cout << tkn << endl;

        Token tkn2 = tkn;
        cout << tkn2 << endl;
        tkn2 = s;
        cout << tkn2 << endl;
        tkn2 = tkn;
        cout << tkn2 << endl;
        tkn2 = c;
        cout << tkn2 << endl;
        tkn = s;
        tkn2 = std::move(tkn);
        cout << tkn2 << endl;
        
        Token tkn3 = std::move(tkn2);
        cout << tkn3 << endl;
        tkn3 = tkn3;
        cout << tkn3 << endl;
        tkn3 = item;
        cout << tkn3 << endl;
        tkn2 = std::move(tkn3);
        cout << tkn2 << endl;
    }

    lnPrintln("\n习题 ------ ex ");

    println_line(2);

    return 0;
};
