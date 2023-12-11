
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
#include "ex20.h"
#include "tool.h"

using namespace std;

// 测试 extern const/ extern / const / non_const
// g11 tool.cpp Sales_data.cpp ex20main.cpp ex20.cpp -o main && ./main
int main() {

    cout << ext_int_def << " " << endl;
    cout << ext_const_int << " " << endl;
    cout << ext_const_long << " " << endl;
    cout << ext_const_char << " " << endl;

    return 0;

};
