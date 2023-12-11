//
// Created by sunyindong on 2022/3/3.
//

#ifndef TEST_CPP_EX15_H
#define TEST_CPP_EX15_H

#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <set>
#include <sstream>
#include <string>
#include <vector>

#include "tool.h"

class Quote {
    friend bool operator!=(const Quote &lhs, const Quote &rhs);

private:
    std::string bookNo;

protected:
    double price = 0.0;

public:
    Quote() { printlnCallFunInfo(__func__, "default constructor"); }

    Quote(const std::string &_bookNo, double _price) : bookNo(_bookNo), price(_price) {
        printlnCallFunInfo(__func__, "init constructor");
    }

    // copy constructor
    Quote(const Quote &q) : bookNo(q.bookNo), price(q.price) {
        std::cout << "Quote: copy constructor\n";
    }

    // move constructor
    Quote(Quote &&q) noexcept : bookNo(std::move(q.bookNo)), price(std::move(q.price)) {
        std::cout << "Quote: move constructor\n";
    }

    // copy =
    Quote &operator=(const Quote &rhs) {
        if (*this != rhs) {
            bookNo = rhs.bookNo;  // align
            price = rhs.price;    // align
        }
        std::cout << "Quote: copy =() \n";
        return *this;
    }

    // move =
    Quote &operator=(Quote &&rhs) noexcept {
        if (*this != rhs) {
            bookNo = std::move(rhs.bookNo);
            price = std::move(rhs.price);
        }
        std::cout << "Quote: move =!!!!!!!!! \n";
        return *this;
    }

    std::string isbn() const { return bookNo; }

    virtual ~Quote() = default;

    virtual Quote *clone() const & { return new Quote(*this); }

    virtual Quote *clone() && { return new Quote(std::move(*this)); }

    virtual double net_price(std::size_t n) const {
        printlnCallFunInfo(__func__, "Quote");
        return n * price;
    }

    virtual std::string debug() const {
        std::string ret;
        ret.append("Quote(");
        ret.append("bookNo=" + this->bookNo + ", ");
        ret.append("price=" + std::to_string(this->price) + ") ");
        return ret;
    }
};

inline bool operator!=(const Quote &lhs, const Quote &rhs) {
    return (lhs.bookNo != rhs.bookNo) && (lhs.price != rhs.price);
}

class DiscQuote : public Quote {
protected:
    std::size_t min_qyt = 0;
    double discount = 0;

public:
    DiscQuote() { printlnCallFunInfo(__func__, "default constructor"); }

    DiscQuote(const std::string &_bookNo, double _price, std::size_t _min_qyt, double _discount)
        : Quote(_bookNo, _price), min_qyt(_min_qyt), discount(_discount) {
        printlnCallFunInfo(__func__, "init constructor");
    }

    // copy constructor
    DiscQuote(const DiscQuote &bq) : Quote(bq), min_qyt(bq.min_qyt), discount(bq.discount) {
        std::cout << "DiscQuote : copy constructor\n";
    }

    // move constructor
    // page 535, " In a constructor, noexcept appears between the parameter list
    // and the : that begins the constructor initializer list"
    DiscQuote(DiscQuote &&bq) noexcept
        : Quote(std::move(bq)), min_qyt(std::move(bq.min_qyt)), discount(std::move(bq.discount)) {
        std::cout << "DiscQuote : move constructor\n";
    }

    // copy =()
    DiscQuote &operator=(const DiscQuote &rhs) {
        std::cout << "DiscQuote : copy =()\n";
        if (this != &rhs) {
            Quote::operator=(std::move(rhs));
            min_qyt = std::move(rhs.min_qyt);
            discount = std::move(rhs.discount);
        }
        return *this;
    }

    // move =()
    DiscQuote &operator=(DiscQuote &&rhs) noexcept {
        std::cout << "DiscQuote : move =()\n";
        if (this != &rhs) {
            Quote::operator=(std::move(rhs));
            min_qyt = rhs.min_qyt;
            discount = rhs.discount;
        }
        return *this;
    }

    // 超过n个，就打9折
    virtual double net_price(std::size_t n) const override = 0;

    virtual std::string debug() const override {
        std::string ret;
        ret.append("DiscQuote(");
        ret.append(Quote::debug());
        ret.append("min_qyt=" + std::to_string(this->min_qyt) + ", ");
        ret.append("discount=" + std::to_string(this->discount) + ") ");
        return ret;
    }
};

class BulkQuote : public DiscQuote {
public:
    BulkQuote() { printlnCallFunInfo(__func__, "default constructor"); }

    // changed the below to the inherited constructor for ex15.27.
    // rules:  1. only inherit from the direct base class.
    //         2. default, copy and move constructors can not inherit.
    //         3. any data members of its own are default initialized.
    //         4. the rest details are in the section section 15.7.4.
    BulkQuote(const std::string &_bookNo, double _price, std::size_t _min_qyt, double _discount)
        : DiscQuote(_bookNo, _price, _min_qyt, _discount) {
        printlnCallFunInfo(__func__, "init constructor, " + debug());
    }
    // using DiscQuote::DiscQuote;

    // copy constructor
    BulkQuote(const BulkQuote &bq) : DiscQuote(bq) {
        std::cout << "BulkQuote : copy constructor\n";
    }

    // move constructor
    // page 535, " In a constructor, noexcept appears between the parameter list
    // and the : that begins the constructor initializer list"
    BulkQuote(BulkQuote &&bq) noexcept : DiscQuote(std::move(bq)) {
        std::cout << "BulkQuote : move constructor\n";
    }

    // copy =()
    BulkQuote &operator=(const BulkQuote &rhs) {
        DiscQuote::operator=(rhs);
        std::cout << "BulkQuote : copy =()\n";

        return *this;
    }

    // move =()
    BulkQuote &operator=(BulkQuote &&rhs) noexcept {
        DiscQuote::operator=(std::move(rhs));
        std::cout << "BulkQuote : move =()\n";
        return *this;
    }

    virtual double net_price(std::size_t n) const override {
        printlnCallFunInfo(__func__, "BulkQuote        " + DiscQuote::debug());
        if (n < min_qyt) return n * price;
        return n * (1 - discount) * price;
    }

    virtual BulkQuote *clone() const & override { return new BulkQuote(*this); }

    virtual BulkQuote *clone() && override { return new BulkQuote(std::move(*this)); }
};

class LimitQuote : public DiscQuote {
public:
    LimitQuote() { printlnCallFunInfo(__func__, "default constructor"); }

    LimitQuote(const std::string &_bookNo, double _price, std::size_t _min_qyt, double _discount)
        : DiscQuote(_bookNo, _price, _min_qyt, _discount) {
        printlnCallFunInfo(__func__, "init constructor");
    }

    // 超过n个，就打9折
    virtual double net_price(std::size_t n) const override {
        if (n <= min_qyt) return n * (1 - discount) * price;
        return min_qyt * (1 - discount) * price + (n - min_qyt) * price;
    }
};

// ex15.19
class Base {
private:
    int private_int = 0;

public:
    Base() = default;

    Base(int _int) : private_int(_int) {}

    void print() { std::cout << private_int << std::endl; }
};

class Pub_Derv : public Base {
public:
    Pub_Derv() = default;

    Pub_Derv(int _int) : Base(_int) {}

    virtual void memfcn(Base &b) { b = *this; }
};

class Pri_Derv : private Base {
public:
    Pri_Derv() = default;

    Pri_Derv(int _int) : Base(_int) {}

    virtual void memfcn(Base &b) { b = *this; }
};

class Derv_from_Private : private Pri_Derv {
public:
    Derv_from_Private() = default;

    Derv_from_Private(int _int) : Pri_Derv(_int) {}

    // virtual void memfcn(Base &b) { b = *this; }
};

// ex15.1
inline auto print_total_info(const Quote &item, size_t n) {
    printlnCallFun(__func__);
    double ret = item.net_price(n);
    std::cout << "ISBN:" << item.isbn() << "\tsold: " << n << " \t应付总额: " << ret;
    return ret;
}

// ex 15.30
class Basket {  // 模拟购物车，其中 Quote 是一条购物记录, 比如1本书，
                // 10本相同的书有10个 Quote
private:
    // NOTE: 变量未示例化，却访问它的内容。
    static bool compareBookNo(const std::shared_ptr<Quote> &lhs,
                              const std::shared_ptr<Quote> &rhs) {
        return lhs->isbn() < rhs->isbn();
    }

    // ask: 为什么此句报错
    std::multiset<std::shared_ptr<Quote>, decltype(compareBookNo) *> items(compareBookNo);

    // ask: 此句正确 
    // std::multiset<std::shared_ptr<Quote>, decltype(compareBookNo) *> items{compareBookNo};

public:
    void addItem(const Quote &item) { items.insert(std::shared_ptr<Quote>(item.clone())); }

    void addItem(Quote &&item) { items.insert(std::shared_ptr<Quote>(std::move(item).clone())); }

    double getTotalPrice() {
        auto ret = 0.0;
        for (auto iter = items.cbegin(), end = items.cend(); iter != end;
             iter = items.upper_bound(*iter)) {
            ret += print_total_info(**iter, items.count(*iter));
            println();
            println();
        }
        return ret;
    }
};

// ex15.32 高级的 QueryText （书中叫 TextQuery)
class QueryText;  // 声明一下

class QueryResult {
    using LineNo = std::vector<std::string>::size_type;
    using DataVec = std::vector<std::string>;

public:
    QueryResult() = delete;

    QueryResult(std::string _rep, std::shared_ptr<std::set<LineNo>> _index,
                std::shared_ptr<DataVec> _data)
        : relationShip(_rep), queryIndex(_index), fileData(_data){};

    const std::shared_ptr<DataVec> &getFileData() const { return fileData; }

    void printResult() {
        println(relationShip + " occurs " + std::to_string(queryIndex->size()) +
                std::string("  times"));
        for (const auto &indexItem : *queryIndex) {
            println(std::string("   (line ") + std::to_string(indexItem + 1) + "\t) " +
                    (*fileData)[indexItem]);
        }
    }
    std::set<LineNo>::iterator begin() { return queryIndex->begin(); }

    std::set<LineNo>::iterator end() { return queryIndex->end(); }

private:
    std::string relationShip;
    std::shared_ptr<DataVec> fileData;
    std::shared_ptr<std::set<LineNo>> queryIndex;
};

class QueryText {
    friend class QueryResult;
    friend class QueryBase;
    using LineNo = std::vector<std::string>::size_type;
    using DataVec = std::vector<std::string>;

public:
    QueryText() = delete;

    QueryText(std::string file_name) : data(new DataVec()) {
        std::fstream in(file_name.c_str());
        LineNo index = 0;
        for (std::string line; std::getline(in, line); ++index) {
            data->push_back(line);
            std::istringstream line_stream(line);
            std::string word_raw, word;
            while (line_stream >> word_raw) {
                std::remove_copy_if(word_raw.begin(), word_raw.end(), std::back_inserter(word),
                                    ispunct);
                if (wordNameToLineIndex.find(word) != wordNameToLineIndex.end()) {
                    wordNameToLineIndex[word]->insert(index);
                } else {
                    std::shared_ptr<std::set<LineNo>> newItem(new std::set<LineNo>());
                    newItem->insert(index);
                    wordNameToLineIndex[word] = newItem;
                }
                word.clear();
            }
        }
    }

    QueryResult query(std::string word) const { return find(word); }

    QueryResult find(std::string word) const {
        static std::shared_ptr<std::set<LineNo>> noData(new std::set<LineNo>);
        auto ret = wordNameToLineIndex.find(word);
        if (ret == wordNameToLineIndex.end()) {
            return QueryResult(word, noData, data);
        }
        return QueryResult(word, ret->second, data);
    }

    void printResult() {
        std::cout << "size is : " << wordNameToLineIndex.size() << " ";
        for (const auto &item : wordNameToLineIndex) {
            std::cout << "word: " << item.first << "\t[";
            for (const auto &second : *item.second) {
                std::cout << "  " << second;
            }
            std::cout << "\t]" << std::endl;
        }
    }

private:
    std::shared_ptr<std::vector<std::string>> data;
    std::map<std::string, std::shared_ptr<std::set<LineNo>>> wordNameToLineIndex;
};

class QueryBase;

class WordQuery;

class NotQuery;

class BinaryQuery;

class AndQuery;

class OrQuery;

class QueryBase {
    friend class Query;

protected:
    using LineNo = QueryText::LineNo;

    virtual ~QueryBase() = default;

private:
    // eval 查询： 计算查询结果
    virtual QueryResult eval(const QueryText &text) const = 0;

    // rep : 获取查询的逻辑表达式
    virtual std::string rep() const = 0;
};

class WordQuery : public QueryBase {
    friend class Query;

    WordQuery(const std::string _query) : query_word(_query) {
        printlnCallFunInfo(__func__, "init constructor");
    };

    virtual QueryResult eval(const QueryText &text) const override {
        return text.query(query_word);
    }

    virtual std::string rep() const override { return query_word; }

    std::string query_word;
};

class Query {
    friend Query operator~(const Query &lhs);

    friend Query operator|(const Query &lhs, const Query &rhs);

    friend Query operator&(const Query &lhs, const Query &rhs);

    friend std::ostream &operator<<(std::ostream &os, const Query &base);

public:
    Query(const std::string &word) : ptr(new WordQuery(word)) {
        printlnCallFunInfo(__func__, "init constructor", word);
    }

    QueryResult eval(const QueryText &text) const { return ptr->eval(text); }

    std::string rep() const { return ptr->rep(); }

private:
    Query(std::shared_ptr<QueryBase> _ptr) : ptr(_ptr){};
    std::shared_ptr<QueryBase> ptr;
};

class NotQuery : public QueryBase {
    friend class Query;

    friend Query operator~(const Query &lhs);

    NotQuery(const Query &q) : query(q) { printlnCallFunInfo(__func__, "init constructor"); }

    virtual QueryResult eval(const QueryText &text) const override {
        // virtual call to eval through the Query operand
        QueryResult result = query.eval(text);

        // start out with an empty result set
        std::shared_ptr<std::set<LineNo>> queryIndexSet = std::make_shared<std::set<LineNo>>();

        std::set<LineNo>::iterator begin = result.begin(), end = result.end();

        auto sz = result.getFileData()->size();

        for (std::size_t n = 0; n != sz; ++n) {
            if (begin == end || *begin != n)
                queryIndexSet->insert(n);
            else if (begin != end)
                ++begin;
        }

        return QueryResult(rep(), queryIndexSet, result.getFileData());
    }

    virtual std::string rep() const override { return std::string(" ~( ") + query.rep() + " ) "; }

    Query query;
};

class BinaryQuery : public QueryBase {
protected:
    BinaryQuery(const Query &LHS, const std::string &OPS, const Query &RHS)
        : lhs(LHS), ops(OPS), rhs(RHS) {
        printlnCallFunInfo(__func__, "init constructor", rep());
    };

    virtual std::string rep() const override {
        return " ( " + lhs.rep() + " " + ops + " " + rhs.rep() + " ) ";
    }

    Query lhs, rhs;
    std::string ops;
};

class OrQuery : public BinaryQuery {
    friend class Query;

    friend Query operator|(const Query &lhs, const Query &rhs);

    OrQuery(const Query &lhs, const Query &rhs) : BinaryQuery(lhs, "|", rhs) {
        printlnCallFunInfo(__func__, "init constructor");
    };

    virtual QueryResult eval(const QueryText &text) const override {
        QueryResult right = rhs.eval(text), left = lhs.eval(text);

        // copy the left-hand operand into the result set
        std::shared_ptr<std::set<LineNo>> queryIndexSet =
            std::make_shared<std::set<LineNo>>(left.begin(), left.end());

        // inert lines from the right-hand operand
        queryIndexSet->insert(right.begin(), right.end());

        return QueryResult(rep(), queryIndexSet, left.getFileData());
    }
};

class AndQuery : public BinaryQuery {
    friend class Query;

    friend Query operator&(const Query &lhs, const Query &rhs);

    AndQuery(const Query &lhs, const Query &rhs) : BinaryQuery(lhs, "&", rhs) {
        printlnCallFunInfo(__func__, "init constructor");
    };

    virtual QueryResult eval(const QueryText &text) const override {
        // virtual calls through the Query operands to get result sets for the
        // operands
        QueryResult left = lhs.eval(text), right = rhs.eval(text);

        // set to hold the intersection of the left and right
        std::shared_ptr<std::set<LineNo>> queryIndexSet = std::make_shared<std::set<LineNo>>();

        // writes the intersection of two ranges to a destination iterator
        std::set_intersection(left.begin(), left.end(), right.begin(), right.end(),
                              std::inserter(*queryIndexSet, queryIndexSet->begin()));

        return QueryResult(rep(), queryIndexSet, left.getFileData());
    }
};

inline Query operator~(const Query &lhs) { return std::shared_ptr<QueryBase>(new NotQuery(lhs)); }

inline Query operator|(const Query &lhs, const Query &rhs) {
    return std::shared_ptr<QueryBase>(new OrQuery(lhs, rhs));
}

inline Query operator&(const Query &lhs, const Query &rhs) {
    return std::shared_ptr<QueryBase>(new AndQuery(lhs, rhs));
}

inline std::ostream &operator<<(std::ostream &os, const Query &base) { return os << base.rep(); }

#endif  // TEST_CPP_EX15_H