//
// Created by sunyindong on 2022/word_transformation_bad.txt/6.
// copy from https://blog.csdn.net/weixin_44009743/article/details/86646203
//

#ifndef TEST_CPP_SALES_ITEM_H
#define TEST_CPP_SALES_ITEM_H
#include <iostream>
#include <string>

class Sales_item
{
public:
    Sales_item(const std::string &book) :isbn(book), units_sold(0), revenue(0.0) {}
    Sales_item(std::istream &is) { is >> *this; }
    friend std::istream& operator>>(std::istream &, Sales_item &);
    friend std::ostream& operator<<(std::ostream &, const Sales_item &);
public:
    Sales_item & operator+=(const Sales_item&);
public:
    double avg_price() const;
    bool same_isbn(const Sales_item &rhs) const {
        return isbn == rhs.isbn;
    }
    Sales_item() :units_sold(0), revenue(0.0) {}
public:
    std::string isbn;
    unsigned units_sold;
    double revenue;
};

using std::istream;
using std::ostream;

// note:
// Sales_item operator+(const Sales_item &, const Sales_item &);

inline Sales_item operator+(const Sales_item &lhs, const Sales_item &rhs) {
    Sales_item ret(lhs);
    ret += rhs;
    return ret;
}

inline bool operator==(const Sales_item &lhs, const Sales_item &rhs) {
    return lhs.units_sold == rhs.units_sold && lhs.revenue == rhs.revenue && lhs.same_isbn(rhs);
}

inline bool operator!=(const Sales_item &lhs, const Sales_item &rhs) {
    return !(lhs == rhs);
}

inline istream& operator>>(istream &in, Sales_item &s) {
    double price;
    in >> s.isbn >> s.units_sold >> price;
    if (in)
        s.revenue = s.units_sold * price;
    else
        s = Sales_item();
    return in;
}

inline ostream& operator<<(ostream &out, const Sales_item &s) {
    out << s.isbn << "\t" << s.units_sold << "\t" << s.revenue << "\t" << s.avg_price();
    return out;
}

inline Sales_item & Sales_item::operator +=(const Sales_item &rhs) {
    units_sold += rhs.units_sold;
    revenue += rhs.revenue;
    return *this;
}

inline double Sales_item::avg_price() const {
    if (units_sold)
        return revenue / units_sold;
    else
        return 0;
}
#endif //TEST_CPP_SALES_ITEM_H
