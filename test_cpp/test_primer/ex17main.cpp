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
#include "ex17.h"
#include "tool.h"

using namespace std;

// define current function

// g14 tool.cpp Sales_data.cpp ex12main.cpp -o main && ./main
int main() {
    lnPrintln("\n习题 ------ ex17.1-3 ");
    tuple<int, int, int> ex1(10, 20, 30);
    tuple<std::string, std::vector<std::string>, std::pair<std::string, int>> ex2;
    vector<vector<Sales_data>> files;
    for (int cnt = 1; cnt <= 4; ++cnt)
        files.push_back(EX04::build_store("primer/data/store" + std::to_string(cnt)));

    EX04::reportResults(std::cin, std::cout, files);

    lnPrintln("\n习题 ------ ex17.10-13 ");
    std::bitset<22> bitseq("1000000010000100101110");
    std::cout << bitseq << std::endl;

    // Default initialize, then turn on.
    std::bitset<22> bit_default;
    for (auto i : {1, 2, 3, 5, 8, 13, 21}) bit_default.set(i);
    std::cout << bit_default << std::endl;

    assert(bitseq == bit_default);

    lnPrintln("\n习题 ------ ex17.13 ");
    EX11::QuizResponses<10> simple_quiz_answers("1100110101");

    EX11::QuizResponses<100> complicated_quiz_answers;
    complicated_quiz_answers.answer(1, true);
    complicated_quiz_answers.answer(2, false);
    complicated_quiz_answers.answer(3, true);

    EX11::QuizResponses<10> simple_quiz_correct("1010101010");
    std::cout << simple_quiz_answers.score(simple_quiz_correct) << std::endl;

    lnPrintln("\n习题 ------ ex17.28 随机数 ");
    std::string temp;
    for (size_t i = 0; i < 10; i++) {
        std::cout << std::hex << random_gen(19, 1, 10) << " ";
    }
    println();

    // lnPrintln("\n章节 ------------------ 10 ");

    return 0;
};
