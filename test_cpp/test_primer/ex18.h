#ifndef TEST_CPP_EX18_H
#define TEST_CPP_EX18_H

#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <numeric>
#include <random>  // 随机数
#include <set>
#include <sstream>
#include <string>
#include <vector>

#include "Sales_data.h"
#include "tool.h"

namespace EX18_25 {
struct Base1 {
    /* ... */
    virtual void print() { std::cout << "Print from Base1" << std::endl; }
    virtual ~Base1() { std::cout << "Base1" << std::endl; }
};
struct Base2 {
    /* ... */
    virtual void print() { std::cout << "Print from Base2" << std::endl; }
    virtual ~Base2() { std::cout << "Base2" << std::endl; }
};

struct D1 : public Base1 {
    /* ... */
    void print() override { std::cout << "Print from D1" << std::endl; }
    ~D1() override { std::cout << "D1" << std::endl; }
};
struct D2 : public Base2 {
    /* ... */
    void print() override { std::cout << "Print from D2" << std::endl; }
    ~D2() override { std::cout << "D2" << std::endl; }
};
struct MI : public D1, public D2 {
    /* ... */
    void print() override { std::cout << "Print from MI" << std::endl; }
    ~MI() override { std::cout << "MI" << std::endl; }
};
}  // namespace EX18_25

namespace EX18_26 {
struct Base1 {
    void print(int) const { std::cout << "Base1 Print Used" << std::endl; };

protected:
    int ival;
    double dval;
    char cval;

private:
    int *id;
};
struct Base2 {
    void print(double) const;

protected:
    double fval;

private:
    double dval;
};

struct Derived : public Base1 {
    void print(std::string) const;

protected:
    std::string sval;
    double dval;
};

struct MI : public Derived, public Base2 {
    void print(std::vector<double>){};
    void print(int x) { Base1::print(x); }

protected:
    int *ival;
    std::vector<double> dvec;
};






}  // namespace EX18_25

#endif  // TEST_CPP_EX18_H