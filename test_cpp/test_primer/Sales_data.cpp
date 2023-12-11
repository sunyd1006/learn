
#include "Sales_data.h"

// static 部分
int Sales_data::initByCall() {
    valInitByCall = 2021;
    return valInitByCall;
}

int Sales_data::valOuter = 2021;
int Sales_data::valInitByCall = 2021;

// member functions
Sales_data::Sales_data(std::istream &is) { read(is, *this); }

// todo: 必须和声明一致
double Sales_data::getAvgPrice() const {
    if (units_sold) return revenue / units_sold;  // p232
    return 0;
}

Sales_data &Sales_data::combine(const Sales_data &item) {
    units_sold += item.units_sold;
    revenue += item.revenue;
    return *this;
}

void Sales_data::printString() {
    std::cout << " bookNo: " << bookNo << " units_sold: " << units_sold << " revenue: " << revenue
              << std::endl;
}

Sales_data &Sales_data::operator+=(const Sales_data &rhs) {
    units_sold += rhs.units_sold;
    revenue += rhs.revenue;
    return *this;
}

// -----------------------------------   有元函数
Sales_data add(const Sales_data &left, const Sales_data &right) {
    Sales_data sum = left;
    sum.combine(right);
    return sum;
}

std::ostream &print(std::ostream &out, const Sales_data &item) {
    out << "Item(isbn: " << item.getIsbn() << " unitsSold: " << item.getUnitsSold()
        << " revenue: " << item.getRevenue() << " ) " << std::endl;
    return out;
}

std::istream &read(std::istream &in, Sales_data &item) {
    double price = 0;
    in >> item.bookNo >> item.units_sold >> price;
    item.revenue = price * item.units_sold;
    return in;
}

void startMain(std::istream &in = std::cin, std::ostream &out = std::cout) {
    Sales_data total;
    std::cout << "pleas input bookNo, units_sold, price : " << std::endl;
    if (read(in, total)) {
        // if (std::cin >> total.bookNo >> total.units_sold >> total.revenue) {

        Sales_data trans;  // p229
        int twoCaseBreak = 0;
        // const int howManyCase = word_transformation_bad.txt;
        const int howManyCase = 20000;

        while (read(in, trans)) {
            // while (std::cin >> trans.bookNo >> trans.units_sold >> trans.revenue) {
            if (twoCaseBreak++ >= howManyCase) break;

            if (total.getIsbn() == trans.getIsbn()) {
                total.combine(trans);
            } else {
                print(out, total) << std::endl;
                total = trans;
            }
        }
        print(out, total) << std::endl;
    } else {
        std::cerr << "No data?!" << std::endl;
    }
}

std::istream &operator>>(std::istream &in, Sales_data &item) {
    double price = 0;
    in >> item.bookNo >> item.units_sold >> price;
    item.revenue = price * item.units_sold;
    return in;
}

std::ostream &operator<<(std::ostream &out, const Sales_data &item) {
    out << "Item(bookNo: " << item.bookNo << " unitsSold: " << item.units_sold
        << " price: " << (item.revenue / item.units_sold) << " revenue: " << item.getRevenue()
        << " ) " << std::endl;
    return out;
}

Sales_data operator+(const Sales_data &left, const Sales_data &right) {
    Sales_data sum = left;
    sum.combine(right);
    return sum;
}

bool operator==(const Sales_data &lhs, const Sales_data &rhs) {
    return lhs.getIsbn() == rhs.getIsbn() && lhs.units_sold == rhs.units_sold &&
           lhs.revenue == rhs.revenue;
}

bool operator!=(const Sales_data &lhs, const Sales_data &rhs) {
    return !(lhs == rhs);
}