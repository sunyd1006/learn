//
// Created by sun yin dong on 2021/9/17.
//
#ifndef TEST_CPP_SALES_DATA_H
#define TEST_CPP_SALES_DATA_H

#include <iostream>
#include <string>

// syd include common util
#include "tool.h"

class Sales_data { // 第7章 p230
    // 不用 friend 这里是无法访问私有数据成员的。有了friend 相当于不用get/set方法了
    friend Sales_data add(const Sales_data &, const Sales_data &);

    friend std::ostream &print(std::ostream &out, const Sales_data &item);

    friend std::istream &read(std::istream &in, Sales_data &item);

    // 重载输入、输出、加法、等号
    friend std::istream &operator>>(std::istream &, Sales_data &);

    friend std::ostream &operator<<(std::ostream &, const Sales_data &);

    friend Sales_data operator+(const Sales_data &, const Sales_data &);

    friend bool operator==(const Sales_data &, const Sales_data &);

    friend bool operator!=(const Sales_data &, const Sales_data &);

    // 16.62 重载hash方法
    friend std::hash<Sales_data>;

public:
    Sales_data() = default;

    Sales_data(std::istream &is);

    Sales_data(std::string book) : bookNo(book) {};

    Sales_data(std::string book, unsigned sold, double reve)
            : bookNo(book), units_sold(sold), revenue(reve) {}

    double getAvgPrice() const;

    Sales_data &combine(const Sales_data &);

    void printString();

    void printHello() {
        std::cout << "hah " << std::endl;
    }

    std::string getIsbn() const { return bookNo; } // implicit inline
    unsigned getUnitsSold() const { return units_sold; }

    double getRevenue() const { return revenue; }

    Sales_data &operator=(const std::string &rhs) {
        println("call operator(const std::string &rhs)");
        *this = Sales_data(rhs);
        return *this;
    }

    // Sales_data &operator=(const std::string &rhs) {
    //     println("call operator(const std::string &rhs)");
    //     Sales_data tmp = *this;
    //     tmp.bookNo = rhs;
    //     return tmp; // todo 返回局部变量的引用，竟然不报错？
    // }

    Sales_data &operator+=(const Sales_data &); // compound-assignment

    explicit operator std::string() const { return bookNo; }

    explicit operator double() const { return getAvgPrice(); }

    static const std::string Sales_data::* data() { return &Sales_data::bookNo; } 
    static int initByCall();

    void printStaticVal() {
        std::cout << " valInner: " << valInner << " valOuter: " << valOuter << " valInitByCall: "
                  << valInitByCall << std::endl;
    }

private:
    std::string bookNo;
    unsigned units_sold = 0;
    double revenue = 0;

    static const int valInner = 2021;
    static int valOuter;
    static int valInitByCall;     // TODO:
};

// Sales_data相关的非成员函数
void startMain(std::istream &in, std::ostream &out);

Sales_data add(const Sales_data &, const Sales_data &);

std::ostream &print(std::ostream &out, const Sales_data &item);

std::istream &read(std::istream &in, Sales_data &item);

std::istream &operator>>(std::istream &, Sales_data &);

std::ostream &operator<<(std::ostream &, const Sales_data &);

Sales_data operator+(const Sales_data &, const Sales_data &);

bool operator==(const Sales_data &, const Sales_data &);

bool operator!=(const Sales_data &, const Sales_data &);

// NOTE: 如果read，和 print在这里定义，则会在Sales_data-ea10df.o ex7-6f0e09.o 里面重复的符号
// std::ostream &print(std::ostream& out, const Sales_data& item) {
//     out << item.getIsbn() << " " << item.getUnitsSold() << " " << item.getRevenue() << std::endl;
//     return out;
// }
// std::istream &read(std::istream& in, Sales_data& item) {
//     in >> item.bookNo >> item.units_sold >>  item.revenue ;
//     return in;
// }

// 第二章
#endif // TEST_CPP_SALES_DATA_H
