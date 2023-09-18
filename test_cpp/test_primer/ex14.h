//
// Created by sunyindong on 2022/3/3.
//

#ifndef TEST_CPP_EX14_H
#define TEST_CPP_EX14_H

#include <iostream>
#include <fstream>
#include <sstream>

#include <memory>
#include <string>

#include <vector>
#include <set>

#include "tool.h"

class GetInput {
public:
    GetInput(std::istream &i = std::cin) : is(i) {}

    std::string operator()() const {
        std::string str;
        std::getline(is, str);
        return is ? str : std::string();
    }

private:
    std::istream &is;
};

class IsEqual {
    int value;
public:
    IsEqual(int v) : value(v) {}

    bool operator()(int elem) {
        return elem == value;
    }
};


class StrLen38 {
    std::size_t operator()(std::string str) {
        return str.size();
    }
};

struct IsInRange {
    IsInRange(std::size_t lower, std::size_t upper)
            : _lower(lower), _upper(upper) {}

    bool operator()(std::string const &str) const {
        printlnCallFun(__func__ + str);
        return str.size() >= _lower && str.size() <= _upper;
    }

    std::size_t lower_limit() const {
        return _lower;
    }

    std::size_t upper_limit() const {
        return _upper;
    }

private:
    std::size_t _lower;
    std::size_t _upper;
};

#endif //TEST_CPP_EX14_H