#ifndef TEST_CPP_EX19_H
#define TEST_CPP_EX19_H

#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <new>  // ex19.1
#include <numeric>
#include <random>  // 随机数
#include <set>
#include <sstream>
#include <string>
#include <vector>

#include "Sales_data.h"
#include "tool.h"

// EX19.1
// inline void* operator new(std::size_t size) {
//     std::cout << "You are using self-defined version of new!" << std::endl;
//     if (void* mem = std::malloc(size))
//         return mem;
//     else
//         throw std::bad_alloc();
// }
// inline void operator delete(void* mem) noexcept {
//     std::cout << "You are using self-defined version of delete!" << std::endl;
//     free(mem);
// }

class A {
public:
    A() = default;
    virtual void print() { println("Call print of " + std::string(typeid(*this).name())); }
    virtual ~A() = default;
};

class B : public A {
public:
    B() = default;
    virtual void print() override { A::print(); }
    ~B() = default;
};

class C : public B {
public:
    C() = default;
    virtual void print() override { A::print(); }
    ~C() = default;
};

class D : public B, public A {
public:
    D() = default;
    ~D() = default;
};

// ex19.6-8 --------------------------------------------------------------------------
namespace EX196_8 {

class TextQuery;

class QueryResult {
    using line_no = std::vector<std::string>::size_type;
    friend std::ostream &print(std::ostream &, const QueryResult &, line_no, line_no);

public:
    QueryResult(std::string s, std::shared_ptr<std::set<line_no>> p,
                std::shared_ptr<std::vector<std::string>> f)
        : sought(s), lines(p), file(f) {}
    // members required by exercise 12.33
    std::set<line_no>::iterator begin() const { return lines->begin(); }
    std::set<line_no>::iterator end() const { return lines->end(); }
    std::shared_ptr<std::vector<std::string>> get_file() const { return file; }

private:
    std::string sought;
    std::shared_ptr<std::set<line_no>> lines;
    std::shared_ptr<std::vector<std::string>> file;
};

class TextQuery {
public:
    using line_no = std::vector<std::string>::size_type;
    TextQuery(std::ifstream &is) : file(new std::vector<std::string>) {
        std::string text;
        while (getline(is, text))  // for each line in the file
        {
            file->push_back(text);          // remember this line of text
            int n = file->size() - 1;       // the current line number
            std::istringstream line(text);  // separate the line into words
            std::string word;
            while (line >> word)  // for each word in that line
            {
                // if word isn't already in wm, subscripting adds a new entry
                auto &lines = wm[word];  // lines is a std::shared_ptr
                if (!lines)              // that pointer is null the first time we see word
                    lines.reset(new std::set<line_no>);  // allocate a new std::set
                lines->insert(n);                        // insert this line number
            }
        }
    }
    QueryResult query(const std::string &sought) const {
        // we'll return a pointer to this std::set if we don't find sought
        static std::shared_ptr<std::set<line_no>> nodata(new std::set<line_no>);
        // use find and not a subscript to avoid adding words to wm!
        auto loc = wm.find(sought);
        if (loc == wm.end())
            return QueryResult(sought, nodata, file);  // not found
        else
            return QueryResult(sought, loc->second, file);
    }

private:
    std::shared_ptr<std::vector<std::string>> file;
    std::map<std::string, std::shared_ptr<std::set<line_no>>> wm;
};

class Query_base {
    friend void cast_test();
    friend class Query;

protected:
    using line_no = TextQuery::line_no;  // used in eval functions
    virtual ~Query_base() = default;

private:
    // eval returns the QueryResult that matches this Query
    virtual QueryResult eval(const TextQuery &) const = 0;
    // rep is a string representing of the query
    virtual std::string rep() const = 0;
};

class WordQuery : public Query_base {
    friend void cast_test();
    friend class Query;  // Query uses the WordQuery constructors
    WordQuery(const std::string &s) : query_word(s) {}
    // concrete class: WordQuery defines all inherited pure virtual functions
    QueryResult eval(const TextQuery &t) const { return t.query(query_word); }
    std::string rep() const { return query_word; }
    std::string query_word;  // word for which to search
};

class Query {
    // these operators access to the shared_ptr constructor
    friend Query operator~(const Query &);
    friend Query operator|(const Query &, const Query &);
    friend Query operator&(const Query &, const Query &);

public:
    Query(const std::string &s)
        : q(new WordQuery(s)), cnt(new std::size_t(1)){};  // builds a new WordQuery
    // interface functions: call the corresponding Query_base operations
    QueryResult eval(const TextQuery &t) const { return q->eval(t); }
    std::string rep() const { return q->rep(); }
    // copy-control operations
    Query(const Query &query) : q(query.q), cnt(query.cnt) { ++*cnt; }
    Query(Query &&query) noexcept : q(query.q), cnt(query.cnt) { query.q = 0; }
    Query &operator=(const Query &);
    Query &operator=(Query &&) noexcept;
    ~Query();

private:
    // Query(std::shared_ptr<Query_base> query) : q(query) {}
    Query(Query_base *query) : q(query), cnt(new std::size_t(1)) {}
    Query_base *q;
    std::size_t *cnt;  // reference counter
    // std::shared_ptr<Query_base> q;
};
inline Query &Query::operator=(const Query &rhs) {
    ++*rhs.cnt;
    if (--*cnt == 0) {
        delete q;
        delete cnt;
    }
    q = rhs.q;
    cnt = rhs.cnt;
    return *this;
}
inline Query &Query::operator=(Query &&rhs) noexcept {
    if (this != &rhs) {
        cnt = rhs.cnt;
        q = rhs.q;
        rhs.q = 0;
    }
    return *this;
}
inline Query::~Query() {
    if (--*cnt == 0) {
        delete q;
        delete cnt;
    }
}
inline std::ostream &operator<<(std::ostream &os, const Query &query) {
    // Query::rep makes a virtual call through its Query_base pointer to rep()
    return os << query.rep();
}

class BinaryQuery : public Query_base {
    friend void cast_test();

protected:
    BinaryQuery(const Query &l, const Query &r, std::string s) : lhs(l), rhs(r), opSym(s) {}
    // abstract class: BinaryQuery doesn't define eval
    std::string rep() const { return "(" + lhs.rep() + " " + opSym + " " + rhs.rep() + ")"; }
    Query lhs, rhs;     // right and left-hand operand
    std::string opSym;  // name of the operator
};

class OrQuery : public BinaryQuery {
public:
    OrQuery(const Query &left, const Query &right) : BinaryQuery(left, right, "|") {}

private:
    friend void cast_test();
    friend Query operator|(const Query &, const Query &);
    QueryResult eval(const TextQuery &text) const {
        // virtual calls through Query members, lhs and rhs
        // the calls to eval return the QueryResult for each operand
        auto left = lhs.eval(text), right = rhs.eval(text);
        // copy the line numbers from the left-hand operand into the result set
        auto ret_lines = std::make_shared<std::set<line_no>>(left.begin(), left.end());
        // insert lines from the right-hand operand
        ret_lines->insert(right.begin(), right.end());
        // returns the new QueryResult representing the union of lhs and rhs
        return QueryResult(rep(), ret_lines, left.get_file());
    }
};

class AndQuery : public BinaryQuery {
public:
    AndQuery(const Query &left, const Query &right) : BinaryQuery(left, right, "&") {}

private:
    friend void cast_test();
    friend Query operator&(const Query &, const Query &);

    // concrete class: AndQuery inherits rep and defines the remaining pure virtual
    QueryResult eval(const TextQuery &text) const {
        // virtual calls through the Query operands to get result sets for the operands
        auto left = lhs.eval(text), right = rhs.eval(text);
        // set to hold the intersection of left and right
        auto ret_lines = std::make_shared<std::set<line_no>>();
        // writes the intersection of two ranges to a destination iterator
        // destination iterator in this call adds elements to ret_lines
        std::set_intersection(left.begin(), left.end(), right.begin(), right.end(),
                              std::inserter(*ret_lines, ret_lines->begin()));
        return QueryResult(rep(), ret_lines, left.get_file());
    }
};

class NotQuery : public Query_base {
    friend void cast_test();
    friend Query operator~(const Query &);
    NotQuery(const Query &q) : query(q) {}
    // concrete class: NotQuery defines all inherited pure virtual functoins
    std::string rep() const {}
    QueryResult eval(const TextQuery &text) const {
        // virtual call to eval through the Query operand
        auto result = query.eval(text);
        // start out with an empty set
        auto ret_lines = std::make_shared<std::set<line_no>>();
        // we have to iterate through the lines on which our operand appears
        auto beg = result.begin(), end = result.end();
        // for each line in the input file, if that line is not in result,
        // add that line number to ret_lines
        auto sz = result.get_file()->size();
        for (std::size_t n = 0; n != sz; ++n) {
            // if we haven't processed all the lines in result
            // check whether this line is present
            if (beg == end || *beg != n)
                ret_lines->insert(n);  // if not in result, add this line
            else
                ++beg;  // otherwise get the next line number in result if there is one
        }
        return QueryResult(rep(), ret_lines, result.get_file());
    }
    Query query;
};

inline Query operator|(const Query &lhs, const Query &rhs) { return (new OrQuery(lhs, rhs)); }

inline Query operator&(const Query &lhs, const Query &rhs) { return (new AndQuery(lhs, rhs)); }

inline Query operator~(const Query &operand) { return (new NotQuery(operand)); }

std::string make_plural(size_t, const std::string &, const std::string &);

inline std::ostream &print(std::ostream &os, const QueryResult &qr, TextQuery::line_no min,
                           TextQuery::line_no max) {
    // if the word was found, print the count and all occurrences
    os << qr.sought << " occurs " << qr.lines->size() << " "
       << make_plural(qr.lines->size(), "time", "s") << std::endl;
    // print each line in which the word appeared
    for (auto num : *qr.lines) {
        if ((num + 1) >= min &&
            (num + 1) <= max)  // limit display range required by exercise 15.42(c)
            os << "\t(line " << num + 1 << ") " << *(qr.file->begin() + num) << std::endl;
        else if ((num + 1 > max))
            break;
    }
    return os;
}

inline std::string make_plural(size_t ctr, const std::string &word, const std::string &ending) {
    return (ctr > 1) ? word + ending : word;
}

}  // namespace EX196_8

// ex19.9
namespace EX19_9_10 {
class Base {
public:
    virtual ~Base() = default;
};
class Derived : public Base {};
inline std::ostream &translate(std::ostream &os, const std::type_info &t) {
    if (t == typeid(int))
        os << "int";
    else if (t == typeid(int[10]))
        os << "int[10]";
    else if (t == typeid(std::string))
        os << "std::string";
    else if (t == typeid(Base))
        os << "class Base";
    else if (t == typeid(Base *))
        os << "class Base *";
    else if (t == typeid(Derived))
        os << "class Derived";
    else if (t == typeid(Sales_data))
        os << "class Sales_data";
    return os;
}

struct A {
    A() = default;
    virtual ~A() = default;
};
struct B : public A {
    B() = default;
    virtual ~B() = default;
};
struct C : public B {
    C() = default;
    virtual ~C() = default;
};

}  // namespace EX19_9_10

enum class LimitedEnum { ZhangSanFeng };
enum UnlimitedEnum { SunYindong, HuWenRui };

inline void callUsingEnum(UnlimitedEnum item) { println(std::to_string(item) + " you are right"); }

class Screen {
    // for test, required by exercise 19.12
    friend void ptrTest();

public:
    typedef std::string::size_type pos;
    // return a pointer to data member, required by exercise 19.12
    static const std::string Screen::*data() { return &Screen::contents; }
    Screen() = default;
    Screen(pos ht, pos wd, char c) : height(ht), width(wd), contents(ht * wd, c) {}
    char get() const { return contents[cursor]; }
    char get_cursor() const { return contents[cursor]; }
    inline char get(pos r, pos c) const {
        pos row = r * width;
        return contents[row + c];
    }
    Screen &move(pos r, pos c) {
        pos row = r * width;
        cursor = row + c;
        return *this;
    }

private:
    pos cursor = 0;
    pos height = 0, width = 0;
    std::string contents;
};

namespace EX19_21 {
class Token {
    // overloaded output operator
    friend std::ostream &operator<<(std::ostream &, const Token &);

public:
    // copy control needed because our class has a union with a string member
    Token() : tok(INT), ival{0} {}
    Token(const Token &t) : tok(t.tok) { copyUnion(t); }
    // move constructor, required by exercise 19.23
    Token(Token &&t) noexcept : tok(std::move(t.tok)) { moveUnion(std::move(t)); }
    Token &operator=(const Token &);
    // move assignment operator, required by exercise 19.23
    Token &operator=(Token &&) noexcept;
    // if the union holds a string, we must destroy it
    ~Token() { freeUnion(); }
    // assignment operators to set the differing members of the union
    Token &operator=(const std::string &);
    Token &operator=(char);
    Token &operator=(int);
    Token &operator=(double);
    Token &operator=(const Sales_data &);  // required by exercise 19.25
private:
    enum { INT, CHAR, DBL, STR, SALE } tok;  // discriminant
    union {                                  // anonymous union
        char cval;
        int ival;
        double dval;
        std::string sval;
        Sales_data sdata;  // Sales_data member, required by exercise 19.22
    };                     // each Token object has an unnamed member of this unnamed union type
    // check the discriminant and copy the union member as appropriate
    void copyUnion(const Token &);
    void moveUnion(Token &&t);
    void freeUnion();  // free class type member of union
};
inline void Token::freeUnion() {
    if (tok == STR) sval.std::string::~string();
    if (tok == SALE) sdata.~Sales_data();
}
}  // namespace EX19_21

#endif  // TEST_CPP_EX19_H