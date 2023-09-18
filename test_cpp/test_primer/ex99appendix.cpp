#include <iostream>
#include <vector>

// current file include
#include <iterator>
#include <numeric>


// old file include
#include "tool.h"

using namespace std;

// define current function

// g14 tool.cpp Sales_data.cpp ex19main.cpp -o main && ./main
int main() {
    vector<int> input{0, 1, 2, 3, 4};
    vector<int> dest;
    partial_sum(input.begin(), input.end(), back_inserter(dest));
    println(input);
    println(dest);

    return 0;
};
