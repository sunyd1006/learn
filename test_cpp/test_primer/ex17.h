#ifndef TEST_CPP_EX17_H
#define TEST_CPP_EX17_H

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

namespace EX04 {
using matches =
    std::tuple<std::vector<Sales_data>::size_type, std::vector<Sales_data>::const_iterator,
               std::vector<Sales_data>::const_iterator>;

std::vector<matches> findBook(const std::vector<std::vector<Sales_data>>& files,
                              const std::string& book) {
    std::vector<matches> ret;  // initally empty
    // for each store find the range of matching books, if any
    for (auto it = files.cbegin(); it != files.cend(); ++it) {
        // find the range of Sales_data that have the same ISBN
        auto found = std::equal_range(it->cbegin(), it->cend(), Sales_data(book),
                                      [](const Sales_data& sd1, const Sales_data& sd2) {
                                          return sd1.getIsbn() < sd2.getIsbn();
                                      });
        if (found.first != found.second) {  // this store had sales
            // remember the index of this store and the matching range
            ret.push_back(make_tuple(it - files.cbegin(), found.first, found.second));
        }
    }
    return ret;  // empty if no matches found
}

void reportResults(std::istream& in, std::ostream& os,
                   const std::vector<std::vector<Sales_data>>& files) {
    std::string s;
    while (std::cout << "Enter the book ISBN to look for: ", in >> s) {
        if (s == "eof") return;
        auto trans = findBook(files, s);  // stores that sold this book
        if (trans.empty()) {
            std::cout << s << " not found in any stores." << std::endl;
            continue;  // get the next book to look for
        }
        for (auto&& store : trans) {  // // for every store with a sale
            // get<n> returns the specified member from the tuple in store
            os << "store " << get<0>(store) + 1
               << " sales: " << std::accumulate(get<1>(store), get<2>(store), Sales_data())
               << std::endl;
        }
        std::cout << "----" << std::endl;
    }
}

std::vector<Sales_data> build_store(const std::string& s) {
    std::vector<Sales_data> ret;
    std::ifstream is(s);
    Sales_data item;
    while (is >> item) ret.push_back(item);
    sort(ret.begin(), ret.end(), [](const Sales_data& lhs, const Sales_data& rhs) {
        return lhs.getIsbn() < rhs.getIsbn();
    });  //! need sort for equal_range to work
    return ret;
}
}  // namespace EX04

namespace EX11 {
using size = std::size_t;
template <size N>
class QuizResponses {
public:
    QuizResponses() = default;
    QuizResponses(const std::string& s) : answers(s) {}

    // added a function that takes a question number and a value to indicate
    // a true/false answer and updates the quiz results accordingly
    void answer(size n, bool v) { answers.set(n - 1, v); }

    // generate grades on the quiz
    size score(const QuizResponses& correct) {
        return (this->answers ^ correct.answers).flip().count() * 1.0 / N * 100;
    }

private:
    std::bitset<N> answers;
};

}  // namespace EX11

unsigned random_gen();
//! with seed spicified
unsigned random_gen(unsigned seed);
//! with seed and range spicified
unsigned random_gen(unsigned seed, unsigned min, unsigned max);

unsigned random_gen() {
    static std::default_random_engine e;
    static std::uniform_int_distribution<unsigned> ud;
    return ud(e);
}

unsigned random_gen(unsigned seed) {
    static std::default_random_engine e(seed);
    static std::uniform_int_distribution<unsigned> ud;
    return ud(e);
}

unsigned random_gen(unsigned seed, unsigned min, unsigned max) {
    static std::default_random_engine e(seed);
    static std::uniform_int_distribution<unsigned> ud(min, max);
    return ud(e);
}





#endif  // TEST_CPP_EX17_H