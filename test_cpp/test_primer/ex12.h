//
// Created by sunyindong on 2022/3/3.
//

#ifndef TEST_CPP_EX12_H
#define TEST_CPP_EX12_H

#include <iostream>
#include <fstream>
#include <sstream>

#include <memory>
#include <string>

#include <vector>
#include <set>

#include "tool.h"

// using std::vector, std::string;  // cpp 17语法

class StrBlobPtr;  // 先声明一下，不然后面找不到

// 多人共享底层的数据结构 vector<string> ，并互相可见更改
class StrBlob {
    friend class StrBlobPtr;

    friend bool operator==(const StrBlob &lhs, const StrBlob &rhs);

    friend bool operator!=(const StrBlob &lhs, const StrBlob &rhs);

    friend bool operator<(const StrBlob &, const StrBlob &);

    friend bool operator>(const StrBlob &, const StrBlob &);

    friend bool operator<=(const StrBlob &, const StrBlob &);

    friend bool operator>=(const StrBlob &, const StrBlob &);

private:
    void check(size_type i, const std::string &msg) const;

    std::shared_ptr<std::vector<std::string>> data;
public:
    typedef std::vector<std::string>::size_type size_type;

    StrBlob();

    StrBlob(std::initializer_list<std::string> il);

    StrBlob &StrBlob::operator=(const StrBlob &lhs) {
        data = make_shared < vector < string >> (*lhs.data);
        return *this;
    }

    StrBlob &StrBlob::operator=(StrBlob &&rhs) noexcept {
        if (this != &rhs) {
            data = std::move(rhs.data);
            rhs.data = nullptr;
        }
        return *this;
    }

    size_type size();

    void push_back(const std::string &str);

    std::string &front();

    std::string &back();

    std::string &front() const;

    std::string &back() const;

    StrBlobPtr begin();// todo 注意这个伴随类

    StrBlobPtr end();

    StrBlobPtr begin() const;// note: const重载类

    StrBlobPtr end() const;
};

bool operator==(const StrBlob &, const StrBlob &);

bool operator!=(const StrBlob &, const StrBlob &);

bool operator<(const StrBlob &, const StrBlob &);

bool operator>(const StrBlob &, const StrBlob &);

bool operator<=(const StrBlob &, const StrBlob &);

bool operator>=(const StrBlob &, const StrBlob &);

// 不影响 StrBlob的生存周期，但阻止用户访问不存在的StrBlob
class StrBlobPtr {
    typedef std::vector<std::string>::size_type size_type;

    friend bool operator==(const StrBlobPtr &, const StrBlobPtr &);

    friend bool operator!=(const StrBlobPtr &, const StrBlobPtr &);

    friend bool operator<(const StrBlobPtr &, const StrBlobPtr &);

    friend bool operator>(const StrBlobPtr &, const StrBlobPtr &);

    friend bool operator<=(const StrBlobPtr &, const StrBlobPtr &);

    friend bool operator>=(const StrBlobPtr &, const StrBlobPtr &);

private:
    std::weak_ptr<std::vector<std::string >> wptr;
    size_type curr;

    auto check(size_type _curr, const std::string &msg) const {
        auto sptr = wptr.lock();
        if (!sptr) throw std::runtime_error("unbound StrBlobPtr");
        if (_curr >= sptr->size()) {
            throw std::out_of_range(msg);
        }
        return sptr;
    }

public:
    StrBlobPtr() : curr(0) {};

    StrBlobPtr(StrBlob &_wptr, size_type _curr = 0) : wptr(_wptr.data), curr(_curr) {};

    // note: for 12.22 StrBlobPtr 支持 const StrBlob
    StrBlobPtr(const StrBlob &_wptr, size_type _curr = 0) : wptr(_wptr.data), curr(_curr) {};

    std::string &deref() const {
        auto sp = check(curr, "out of bound");
        return (*sp)[curr];
    }

    StrBlobPtr &incr() {
        check(curr, "increment past end of StrBlobPtr");
        ++curr;
        return *this;
    }

    bool operator!=(const StrBlobPtr &p) {
        return p.curr != curr;
    }

    inline StrBlobPtr &StrBlobPtr::operator++() {
        check(curr, "increment past end of StrBlobPtr");
        ++curr;
        return *this;
    }

    inline StrBlobPtr &StrBlobPtr::operator--() {
        check(--curr, "decrement past begin of StrBlobPtr");
        return *this;
    }

    inline StrBlobPtr StrBlobPtr::operator++(int) {
        StrBlobPtr ret = *this;
        ++*this;
        return ret;
    }

    inline StrBlobPtr StrBlobPtr::operator--(int) {
        StrBlobPtr ret = *this;
        --*this;
        return ret;
    }
};

class QueryResult {
    using LineNo = std::vector<std::string>::size_type;
    using DataVec = std::vector<std::string>;
public:
    QueryResult() = delete;

    QueryResult(std::shared_ptr<DataVec> _data, std::pair<std::string, std::set<LineNo>> _ref) : data(_data),
                                                                                                 ref(_ref) {};

    void printResult() {
        println(ref.first + " occurs " + std::to_string(ref.second.size()) + std::string("  times"));
        for (const auto &item : ref.second) {
            print(std::string("   (line ") + std::to_string(item + 1) + "\t) " + (*data)[item]);
            println();
        }
    }

private:
    std::shared_ptr<DataVec> data;
    std::pair<std::string, std::set<LineNo>> ref;
};

class QueryResult;

class QueryText {
    using LineNo = std::vector<std::string>::size_type;
    using DataVec = std::vector<std::string>;

    friend class QueryResult;

public:
    QueryText() = delete;

    QueryText(std::string file_name) : data(new DataVec()) {
        std::fstream in(file_name.c_str());
        int index = 0;
        for (std::string line; std::getline(in, line); ++index) {
            data->push_back(line);
            std::istringstream line_stream(line);
            std::string word_raw, word;
            while (line_stream >> word_raw) {
                std::remove_copy_if(word_raw.begin(), word_raw.end(), std::back_inserter(word), ispunct);
                auto &ret = nameIndex[word];
                // if (!ret) ret.reset(new std::set<LineNo>());
                ret.insert(index);
                word.clear();
            }
        }
    }

    QueryResult find(std::string word) {
        static std::set<LineNo> empty_index;
        auto ret = nameIndex.find(word);
        if (ret == nameIndex.end()) {
            return QueryResult(data, {word, empty_index});
        }
        return QueryResult(data, {word, ret->second});
    }

    void printNameIndex() {
        std::cout << "size is : " << nameIndex.size() << " ";
        for (const auto &item : nameIndex) {
            std::cout << "word: " << item.first << "\t[";
            for (const auto &second : item.second) {
                std::cout << "  " << second;
            }
            std::cout << "\t]" << std::endl;
        }
    }

private:
    std::shared_ptr<std::vector<std::string>> data;
    std::map<std::string, std::set<LineNo>> nameIndex;
};


#endif //TEST_CPP_EX12_H
