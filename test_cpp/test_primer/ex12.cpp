//
// Created by sunyindong on 2022/3/3.
//

#include "ex12.h"
#include "tool.h"

using namespace std;

//==================================================================
//
//		StrBlob - operators
//
//==================================================================

bool operator==(const StrBlob &lhs, const StrBlob &rhs) {
    return *lhs.data == *rhs.data;
}

bool operator!=(const StrBlob &lhs, const StrBlob &rhs) {
    return !(lhs == rhs);
}

bool operator<(const StrBlob &lhs, const StrBlob &rhs) {
    return std::lexicographical_compare(lhs.data->begin(), lhs.data->end(), rhs.data->begin(), rhs.data->end());
}

bool operator>(const StrBlob &lhs, const StrBlob &rhs) {
    return rhs < lhs;
}

bool operator<=(const StrBlob &lhs, const StrBlob &rhs) {
    return !(rhs < lhs);
}

bool operator>=(const StrBlob &lhs, const StrBlob &rhs) {
    return !(lhs < rhs);
}
//================================================================
//
//		StrBlobPtr - operators
//
//================================================================

bool operator==(const StrBlobPtr &lhs, const StrBlobPtr &rhs) {
    return lhs.curr == rhs.curr;
}

bool operator!=(const StrBlobPtr &lhs, const StrBlobPtr &rhs) {
    return !(lhs == rhs);
}

bool operator<(const StrBlobPtr &x, const StrBlobPtr &y) {
    return x.curr < y.curr;
}

bool operator>(const StrBlobPtr &x, const StrBlobPtr &y) {
    return x.curr > y.curr;
}

bool operator<=(const StrBlobPtr &x, const StrBlobPtr &y) {
    return x.curr <= y.curr;
}

bool operator>=(const StrBlobPtr &x, const StrBlobPtr &y) {
    return x.curr >= y.curr;
}

//==================================================================
//
//		other
//
//==================================================================

StrBlob::StrBlob() : data(std::make_shared<std::vector<std::string>>()) {}

StrBlob::StrBlob(std::initializer_list<std::string> il) : data(std::make_shared<std::vector<std::string>>(il)) {}

StrBlob::size_type StrBlob::size() {
    return data->size();
}

void StrBlob::push_back(const std::string &str) {
    data->push_back(str);
}

std::string &StrBlob::front() {
    check(0, "No elements in data");
    return data->front();
}

std::string &StrBlob::back() {
    check(0, "No elements in data");
    return data->back();
}

std::string &StrBlob::front() const {
    check(0, "No elements in data");
    return data->front();
}

std::string &StrBlob::back() const {
    check(0, "No elements in data");
    return data->back();
}

void StrBlob::check(size_type i, const std::string &msg) const {
    if (i >= data->size()) {
        throw out_of_range(msg);
    }
}


StrBlobPtr StrBlob::begin() {
    return StrBlobPtr(*this);
}

StrBlobPtr StrBlob::end() {
    return StrBlobPtr(*this, data->size());
}

StrBlobPtr StrBlob::begin() const {
    print(string("call const ") + __func__);
    return StrBlobPtr(*this);
}

StrBlobPtr StrBlob::end() const {
    print(string("call const ") + __func__);
    return StrBlobPtr(*this, data->size());
}
