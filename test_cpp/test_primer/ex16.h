//
// Created by sunyindong on 2022/3/3.
//

#ifndef TEST_CPP_EX16_H
#define TEST_CPP_EX16_H

#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <set>
#include <sstream>
#include <string>
#include <vector>

#include "tool.h"

// ex16.3
template <typename T>
int SunCompare(const T &lhs, const T &rhs) {
    if (lhs < rhs) return -1;
    if (rhs < lhs) return 1;
    return 0;
}

// ex16.4
template <typename Iterator, typename Value>
Iterator SunFind(Iterator first, Iterator last, Value value) {
    for (; first != last && *first != value; ++first)
        ;
    return first;
}

// ex16.5
template <typename T, unsigned Len>
void printlnArray(const T (&array)[Len]) {
    for (int i = 0; i < Len; i++) {
        std::cout << array[i] << " ";
    }
    println();
}

// ex16.5
template <typename Array>
void printlnArray2(const Array &arr) {
    for (const auto &elem : arr) std::cout << elem << " ";
    println();
}

// ex16.12, 类似于std::vector
template <typename T>
class Blob {
public:
    typedef T value_type;
    typedef typename std::vector<T>::size_type size_type;

    // constructors
    Blob();

    Blob(std::initializer_list<T> il);

    // ex16.24
    template <typename It>
    Blob(It b, It e) : data(std::make_shared<std::vector<T>>(b, e)) {}

    // number of elements in the Blob
    size_type size() const { return data->size(); }

    bool empty() const { return data->empty(); }

    void push_back(const T &t) { data->push_back(t); }

    void push_back(T &&t) { data->push_back(std::move(t)); }

    void pop_back();

    // element access
    T &back();

    T &operator[](size_type i);

    const T &back() const;

    const T &operator[](size_type i) const;

private:
    std::shared_ptr<std::vector<T>> data;

    // throw msg if data[i] isn't valid
    void check(size_type i, const std::string &msg) const;
};

// constructors
template <typename T>
Blob<T>::Blob() : data(std::make_shared<std::vector<T>>()) {}

template <typename T>
Blob<T>::Blob(std::initializer_list<T> il) : data(std::make_shared<std::vector<T>>(il)) {}

template <typename T>
void Blob<T>::check(size_type i, const std::string &msg) const {
    if (i >= data->size()) throw std::out_of_range(msg);
}

template <typename T>
T &Blob<T>::back() {
    check(0, "back on empty Blob");
    return data->back();
}

template <typename T>
const T &Blob<T>::back() const {
    check(0, "back on empty Blob");
    return data->back();
}

template <typename T>
T &Blob<T>::operator[](size_type i) {
    // if i is too big, check function will throw, preventing access to a nonexistent element
    check(i, "subscript out of range");
    return (*data)[i];
}

template <typename T>
const T &Blob<T>::operator[](size_type i) const {
    // if i is too big, check function will throw, preventing access to a nonexistent element
    check(i, "subscript out of range");
    return (*data)[i];
}

template <typename T>
void Blob<T>::pop_back() {
    check(0, "pop_back on empty Blob");
    data->pop_back();
}

template <typename>
class BlobPtr;

// 声明
template <typename T>
bool operator==(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs);

template <typename T>
bool operator<(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs);

template <typename T>
class BlobPtr {
    friend bool operator==<T>(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs);

    // friend bool operator<<T>(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs);

public:
    BlobPtr() : curr(0) {}

    BlobPtr(Blob<T> &a, std::size_t sz = 0) : wptr(a.data), curr(sz) {}

    T &operator*() const {
        auto p = check(curr, "dereference past end");
        return (*p)[curr];
    }

    // prefix
    BlobPtr &operator++();

    BlobPtr &operator--();

    // postfix
    BlobPtr operator++(int);

    BlobPtr operator--(int);

private:
    // returns  a std::shared_ptr to the std::vector if the check succeeds
    std::shared_ptr<std::vector<T>> check(std::size_t, const std::string &) const;

    std::weak_ptr<std::vector<T>> wptr;
    std::size_t curr;
};

// prefix ++
template <typename T>
BlobPtr<T> &BlobPtr<T>::operator++() {
    // if curr already points past the end of the container, can't increment it
    check(curr, "increment past end of StrBlob");
    ++curr;
    return *this;
}

// prefix --
template <typename T>
BlobPtr<T> &BlobPtr<T>::operator--() {
    --curr;
    check(curr, "decrement past begin of BlobPtr");

    return *this;
}

// postfix ++
template <typename T>
BlobPtr<T> BlobPtr<T>::operator++(int) {
    BlobPtr ret = *this;
    ++*this;

    return ret;
}

// postfix --
template <typename T>
BlobPtr<T> BlobPtr<T>::operator--(int) {
    BlobPtr ret = *this;
    --*this;

    return ret;
}

template <typename T>
bool operator==(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs) {
    if (lhs.wptr.lock() != rhs.wptr.lock()) {
        throw std::runtime_error("ptrs to different Blobs!");
    }
    return lhs.i == rhs.i;
}

template <typename T>
bool operator<(const BlobPtr<T> &lhs, const BlobPtr<T> &rhs) {
    if (lhs.wptr.lock() != rhs.wptr.lock()) {
        throw std::runtime_error("ptrs to different Blobs!");
    }
    return lhs.i < rhs.i;
}

// ex16.14
template <unsigned H, unsigned W>
class Screen {
    typedef std::string::size_type pos;

public:
    Screen() = default;  // needed because Screen has another constructor
    // cursor initialized to 0 by its in-class initializer
    Screen(char c) : contents(H * W, c) {}

    char get() const {  // get the character at the cursor
        return contents[cursor];
    }                            // implicitly inline
    Screen &move(pos r, pos c);  // can be made inline later

    friend std::ostream &operator<<(std::ostream &os, const Screen<H, W> &c) {
        unsigned int i;
        for (i = 0; i < c.height; i++) {
            os << c.contents.substr(0, W) << std::endl;
        }
        return os;
    }

    friend std::istream &operator>>(std::istream &is, Screen &c) {
        char a;
        is >> a;
        std::string temp(H * W, a);
        c.contents = temp;
        return is;
    }

private:
    pos cursor = 0;
    pos height = H, width = W;
    std::string contents;
};

template <unsigned H, unsigned W>
inline Screen<H, W> &Screen<H, W>::move(pos r, pos c) {
    pos row = r * width;
    cursor = row + c;
    return *this;
}

// ex16.16
// @brief a std::vector like class， rename from StrVec
template <typename T>
class Vec {
public:
    Vec() : element(nullptr), first_free(nullptr), cap(nullptr) {}

    Vec(std::initializer_list<T> l);

    Vec(const Vec &v);

    Vec &operator=(const Vec &rhs);

    ~Vec();

    void push_back(const T &t);

    template <typename... Args>
    void emplace_back(Args &&...args) {
        chk_n_alloc();
        alloc.construct(first_free++, std::forward<Args>(args)...);
    }

    std::size_t size() const { return first_free - element; }

    std::size_t capacity() const { return cap - element; }

    T *begin() const { return element; }

    T *end() const { return first_free; }

    void reserve(std::size_t n);

    void resize(std::size_t n);

    void resize(std::size_t n, const T &t);

private:
    T *element;
    T *first_free;
    T *cap;
    std::allocator<T> alloc;

    void reallocate();

    void chk_n_alloc() {
        if (size() == capacity()) reallocate();
    }

    void free();

    void wy_alloc_n_move(std::size_t n);

    std::pair<T *, T *> alloc_n_copy(T *b, T *e);
};

template <typename T>
Vec<T>::Vec(const Vec &v) {
    /**
     * @brief newData is a pair of pointers pointing to newly allocated and copied
     *        from range : [b, e)
     */
    std::pair<T *, T *> newData = alloc_n_copy(v.begin(), v.end());

    element = newData.first;
    first_free = cap = newData.second;
}

template <typename T>
Vec<T>::Vec(std::initializer_list<T> l) {
    // allocate memory as large as l.size()
    T *const newData = alloc.allocate(l.size());

    // copy elements from l to the address allocated
    T *p = newData;
    for (const auto &t : l) alloc.construct(p++, t);

    // build data structure
    element = newData;
    first_free = cap = element + l.size();
}

template <typename T>
Vec<T> &Vec<T>::operator=(const Vec &rhs) {  // operator =
    // allocate and copy first to protect against self_assignment
    std::pair<T *, T *> newData = alloc_n_copy(rhs.begin(), rhs.end());
    free();                   // destroy and deallocate
    element = newData.first;  // update data structure
    first_free = cap = newData.second;
    return *this;
}

template <typename T>
Vec<T>::~Vec() {  // destructor
    free();
}

/**
 * @brief   allocate new memeory if nessary and push back the new T
 * @param t new T
 */
template <typename T>
void Vec<T>::push_back(const T &t) {
    chk_n_alloc();
    alloc.construct(first_free++, t);
}

/**
 * @brief   preallocate enough memory for specified number of elements
 * @param n number of elements required
 */
template <typename T>
void Vec<T>::reserve(std::size_t n) {
    // if n too small, just return without doing anything
    if (n <= capacity()) return;

    // allocate new memory and move data from old address to the new one
    wy_alloc_n_move(n);
}

/**
 *  @brief  Resizes to the specified number of elements.
 *  @param  n  Number of elements the %std::vector should contain.
 *
 *  This function will resize it to the specified
 *  number of elements.  If the number is smaller than the
 *  current size it is truncated, otherwise
 *  default constructed elements are appended.
 */
template <typename T>
void Vec<T>::resize(std::size_t n) {
    resize(n, T());
}

/**
 *  @brief  Resizes it to the specified number of elements.
 *  @param  __new_size  Number of elements it should contain.
 *  @param  __x  Data with which new elements should be populated.
 *
 *  This function will resize it to the specified
 *  number of elements.  If the number is smaller than the
 *  current size the it is truncated, otherwise
 *  the it is extended and new elements are populated with
 *  given data.
 */
template <typename T>
void Vec<T>::resize(std::size_t n, const T &t) {
    if (n < size()) {
        // destroy the range [element+n, first_free) using destructor
        for (auto p = element + n; p != first_free;) alloc.destroy(p++);
        // update first_free to point to the new address
        first_free = element + n;
    } else if (n > size()) {
        for (auto i = size(); i != n; ++i) push_back(t);
    }
}

/**
 * @brief   allocate new space for the given range and copy them into it
 * @param b
 * @param e
 * @return  a pair of pointers pointing to [first element , one past the last) in the new space
 */
template <typename T>
std::pair<T *, T *> Vec<T>::alloc_n_copy(T *b, T *e) {
    // calculate the size needed and allocate space accordingly
    T *data = alloc.allocate(e - b);
    return {data, std::uninitialized_copy(b, e, data)};
    //            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // which copies the range[first, last) to the space to which
    // the starting address data is pointing.
    // This function returns a pointer to one past the last element
}

/**
 * @brief   destroy the elements and deallocate the space previously allocated.
 */
template <typename T>
void Vec<T>::free() {
    // if not nullptr
    if (element) {
        // destroy it in reverse order.
        for (auto p = first_free; p != element;) alloc.destroy(--p);

        alloc.deallocate(element, capacity());
    }
}

/**
 * @brief   allocate memory for spicified number of elements
 * @param n
 * @note    it's user's responsibility to ensure that @param n is greater than
 *          the current capacity.
 */
template <typename T>
void Vec<T>::wy_alloc_n_move(std::size_t n) {
    // allocate as required.
    std::size_t newCapacity = n;
    T *newData = alloc.allocate(newCapacity);

    // move the data from old place to the new one
    T *dest = newData;
    T *old = element;
    for (std::size_t i = 0; i != size(); ++i) alloc.construct(dest++, std::move(*old++));

    free();

    // update data structure
    element = newData;
    first_free = dest;
    cap = element + newCapacity;
}

/**
 * @brief   Double the capacity and using std::move move the original data to the newly
 *          allocated memory
 */
template <typename T>
void Vec<T>::reallocate() {
    // calculate the new capacity required
    std::size_t newCapacity = size() ? 2 * size() : 1;

    // allocate and move old data to the new space
    wy_alloc_n_move(newCapacity);
}

// 16.19
template <typename Container>
void SunPrint(const Container &c) {
    for (typename Container::size_type i = 0; i != c.size(); ++i) std::cout << c[i] << " ";

    // for (auto it = c.begin(); it != c.end(); ++it)
    //     std::cout << *it << " ";
}

// 16.21
class DebugDelete {
public:
    DebugDelete(std::ostream &_os = std::cout) : os(_os){};

    template <typename T>
    void operator()(T *t) const {
        os << "(DebugDelete) deleting ptr " << std::hex << t << std::endl;
        delete t;
    }

private:
    std::ostream &os;
};

// 16.22
class TextQuery;
class QueryResult {
    using LineNo = std::vector<std::string>::size_type;

public:
    friend std::ostream &print(std::ostream &, const QueryResult &);

public:
    QueryResult(const std::string &s, std::shared_ptr<std::set<LineNo>> set,
                std::shared_ptr<std::vector<std::string>> v)
        : word(s), nos(set), input(v) {}

private:
    std::string word;
    std::shared_ptr<std::set<LineNo>> nos;
    std::shared_ptr<std::vector<std::string>> input;
};

std::ostream &print(std::ostream &out, const QueryResult &qr) {
    out << qr.word << " occurs " << qr.nos->size() << (qr.nos->size() > 1 ? " times" : " time")
        << std::endl;
    for (auto i : *qr.nos) out << "\t(line " << i + 1 << ") " << qr.input->at(i) << std::endl;
    return out;
}

class TextQuery {
public:
    using LineNo = std::vector<std::string>::size_type;

    TextQuery(std::ifstream &ifs) : input(new std::vector<std::string>, DebugDelete()) {
        LineNo lineNo{0};
        for (std::string line; std::getline(ifs, line); ++lineNo) {
            input->push_back(line);
            std::stringstream line_stream(line);
            for (std::string text, word; line_stream >> text; word.clear()) {
                // avoid read a word followed by punctuation(such as: word, )
                std::remove_copy_if(text.begin(), text.end(), std::back_inserter(word), ispunct);
                // use reference avoid count of std::shared_ptr add.
                auto &nos = result[word];
                if (!nos) nos.reset(new std::set<LineNo>, DebugDelete());
                nos->insert(lineNo);
            }
        }
    }

    QueryResult query(const std::string &str) const {
        // use static just allocate once.
        static std::shared_ptr<std::set<LineNo>> nodate(new std::set<LineNo>, DebugDelete());
        auto found = result.find(str);
        if (found == result.end())
            return QueryResult(str, nodate, input);
        else
            return QueryResult(str, found->second, input);
    }

private:
    std::shared_ptr<std::vector<std::string>> input;
    std::map<std::string, std::shared_ptr<std::set<LineNo>>> result;
};

template <typename T>
class SharedPtr {
    using DelFuncPtr = void (*)(T *);

public:
    SharedPtr(T *ptr = nullptr, DelFuncPtr del = nullptr)
        : ptr_(ptr), count_ptr_(new size_t(ptr != nullptr)), del_(del) {}

    ~SharedPtr() {
        if (!ptr_) return;
        if (--*count_ptr_ == 0) {
            del_ ? del_(ptr_) : delete ptr_;
            delete count_ptr_;
        }
        ptr_ = nullptr;
        count_ptr_ = nullptr;
    }

    SharedPtr(const SharedPtr &sp) : ptr_(sp.ptr_), count_ptr_(sp.count_ptr_), del_(sp.del_) {
        ++*count_ptr_;
    }

    SharedPtr &operator=(SharedPtr sp) {
        swap(sp);
        return *this;
    }

    SharedPtr(const SharedPtr &&sp) noexcept : SharedPtr() { swap(sp); }

    void reset(T *ptr = nullptr, DelFuncPtr del = nullptr) {
        SharedPtr tmp(ptr, del);
        swap(tmp);
    }

    void swap(SharedPtr &r) noexcept {
        using std::swap;
        swap(ptr_, r.ptr_);
        swap(count_ptr_, r.count_ptr_);
        swap(del_, r.del_);
    }

    T *get() const noexcept { return ptr_; }
    T &operator*() const noexcept { return *get(); }
    T *operator->() const noexcept { return get(); }
    size_t use_count() const noexcept { return *count_ptr_; }
    explicit operator bool() const noexcept { return ptr_ != nullptr; }

private:
    T *ptr_ = nullptr;
    size_t *count_ptr_ = nullptr;
    DelFuncPtr del_ = nullptr;
};

// ex16.28
class Delete {
public:
    template <typename T>
    void operator()(T *ptr) const {
        println("(Delete) deleting ptr " + getHexPointer(ptr));
        delete ptr;
    }
};

template <typename T, typename D = Delete>
class UniquePtr {
public:
    UniquePtr(T *ptr = nullptr, const D &d = D()) noexcept : ptr_(ptr), deleter_(d) {}
    ~UniquePtr() { deleter_(ptr_); }

    UniquePtr(const UniquePtr &) = delete;
    UniquePtr &operator=(const UniquePtr &) = delete;

    UniquePtr(UniquePtr &&other) noexcept : ptr_(other.ptr_), deleter_(std::move(other.deleter_)) {
        other.ptr_ = nullptr;
    }
    UniquePtr &operator=(UniquePtr &&other) noexcept {
        if (this != &other) {
            reset();
            ptr_ = other.ptr_;
            deleter_ = std::move(other.deleter_);
            other.ptr_ = nullptr;
        }
        return *this;
    }

    UniquePtr &operator=(std::nullptr_t) noexcept {
        reset();
        return *this;
    }

    T *release() noexcept {
        T *ret = ptr_;
        ptr_ = nullptr;
        return ret;
    }

    void reset(T *q = nullptr) noexcept {
        deleter_(ptr_);  // todo 网上代码错误，网上写的是 delete_(q);
        ptr_ = q;
    }

    void swap(UniquePtr &other) noexcept {
        using std::swap;
        swap(ptr_, other.ptr_);
        swap(deleter_, other.deleter_);
    }

    T *get() const noexcept { return ptr_; }
    D &get_deleter() noexcept { return deleter_; }
    const D &get_deleter() const noexcept { return deleter_; }
    explicit operator bool() const noexcept { return ptr_ != nullptr; }
    T &operator*() const { return *ptr_; }
    T *operator->() const noexcept { return ptr_; }
    T &operator[](size_t i) const { return ptr_[i]; }

private:
    T *ptr_ = nullptr;
    D deleter_;
};

// 测试不同类
template <typename T1, typename T2>
void comparePrintByTwoArrayRef(const T1 &t1, const T2 &t2) {
    for (const auto &item : t1) {
        std::cout << item << " ";
    }
    println();
    for (const auto &item : t2) {
        std::cout << item << " ";
    }
    println();
}

template <typename T>
bool compare(const T &t1, const T &t2) {
    if (t1 < t2) return -1;
    if (t2 < t1) return 1;
    return 0;
}

struct Test40 {
    friend Test40 &operator+(const Test40 &lhs, const Test40 &rhs);
    Test40(int _val) : val(_val) {}
    int val;
};

inline Test40 &operator+(const Test40 &lhs, const Test40 &rhs) {
    Test40 ret = lhs;
    ret.val += rhs.val;
    return ret;
}

void func1(int v1, int &v2) { std::cout << std::dec << ++v1 << " " << ++v2 << std::endl; }

void func2(int &&i, int &j) { std::cout << std::dec << ++i << " " << ++j << std::endl; }

template <typename F, typename T1, typename T2>
void flip(F f, T1 &&t1, T2 &&t2) {
    f(std::forward<T2>(t2), std::forward<T1>(t1));
}

// ex16.51 printVariadicTemplateParamsLength
template <typename T, typename... Args>
void printLen(const T &t, const Args &...args) {
    std::cout << sizeof...(Args) << " " << sizeof...(args) << std::endl;
}

// ex15.52
template <typename T>  // 非可变参数模板应该先声明
std::ostream &print(std::ostream &os, const T &t) {
    os << t;
    return os;
}
template <typename T, typename... Args>
std::ostream &print(std::ostream &os, const T &t, const Args &...args) {
    os << t << ", ";
    return print(os, args...);
}
// ex15.55

std::string debug_rep(const std::string &s) { return '"' + s + '"'; }
//! convert the character pointers to string and call the string version of debug_rep
std::string debug_rep(char *p) { return debug_rep(std::string(p)); }
std::string debug_rep(const char *p) { return debug_rep(std::string(p)); }

//! print any type we don't otherwise.
template <typename T>
std::string debug_rep(const T &t) {
    std::ostringstream ret;
    ret << t;
    return ret.str();
}

//! print pointers as their pointer value, followed by the object to which the pointer points
template <typename T>
std::string debug_rep(T *p) {
    std::ostringstream ret;
    ret << "pointer: " << p;
    if (p)
        ret << " " << debug_rep(*p);
    else
        ret << " null pointer";

    return ret.str();
}

// 打印参数
template <typename... Args>
std::ostream &errorMsg(std::ostream &os, const Args... rest) {
    //! print(os,debug_rep(rest)...);
    return print(os, debug_rep(rest)...);
}

// ex16.62
namespace std {
template <>
struct hash<Sales_data> {
    typedef size_t result_type;
    typedef Sales_data argument_type;
    size_t operator()(const Sales_data &s) const {
        return hash<string>()(s.bookNo) ^ hash<unsigned>()(s.units_sold) ^
               hash<double>()(s.revenue);
    }
};
}  // namespace std

// -----------------------------------------------------------------------
template <typename T>
std::size_t count(const std::vector<T> &vec, T value);
template <>  // 16.64 模板特例化，要先声明
std::size_t count(const std::vector<const char *> &vec, const char *value);


template <typename T>
std::size_t count(const std::vector<T> &vec, T value) {
    std::size_t count = 0;
    for (auto &item : vec) count += (item == value) ? 1 : 0;

    return count;
}

//! where T == const char*
template <>
std::size_t count(const std::vector<const char *> &vec, const char *value) {
    std::size_t count = 0;
    for (auto &item : vec) count += (strcmp(item, value) == 0) ? 1 : 0;
    return count;
}

#endif  // TEST_CPP_EX16_H