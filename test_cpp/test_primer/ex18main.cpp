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
#include "ex18.h"
#include "tool.h"

using namespace std;

// define current function

// g14 tool.cpp Sales_data.cpp ex18main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex18.25 ");
    EX18_25::Base1 *pb1 = new EX18_25::MI;
    EX18_25::Base2 *pb2 = new EX18_25::MI;
    EX18_25::D1 *pd1 = new EX18_25::MI;
    EX18_25::D2 *pd2 = new EX18_25::MI;
    std::cout << "pb1 print..........." << std::endl;
    pb1->print();
    println_line();

    std::cout << "pd1 print..........." << std::endl;
    pd1->print();
    println_line();

    std::cout << "pd2 print..........." << std::endl;
    pd2->print();
    println_line();

    std::cout << "delete pb2..........." << std::endl;
    delete pb2;
    println_line();

    std::cout << "delete pd1..........." << std::endl;
    delete pd1;
    println_line();

    std::cout << "delete pd2..........." << std::endl;
    delete pd2;
    println_line();

    lnPrintln("\n习题 ------ ex18.26 ");
    EX18_26::MI mi;
    mi.print(42);

    return 0;
};
