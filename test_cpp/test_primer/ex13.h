//
// Created by sunyindong on 2022/3/3.
//

#ifndef TEST_CPP_EX13_H
#define TEST_CPP_EX13_H

#include <iostream>
#include <fstream>
#include <sstream>

#include <memory>
#include <string>

#include <vector>
#include <set>

#include "tool.h"

// ex13.24
class HasPtrLikeValue {
    friend void swap(HasPtrLikeValue &, HasPtrLikeValue &);

    friend bool operator<(const HasPtrLikeValue &lhs, const HasPtrLikeValue &rhs);

public:
    HasPtrLikeValue(const std::string &s = std::string("default")) : ps(new std::string(s)), i(0) {
        println(__func__ + std::string(" ") + *ps);
    };

    HasPtrLikeValue(const HasPtrLikeValue &ref) : ps(new std::string(*ref.ps)), i(ref.i) {};

    HasPtrLikeValue(HasPtrLikeValue &&ref) : ps(ref.ps), i(ref.i) {
        ref.ps = 0;
        println("call move constructor");
    };

    HasPtrLikeValue &operator=(const HasPtrLikeValue &right) {
        std::string *temp_ps = new std::string(*right.ps);
        delete ps;
        ps = temp_ps;
        i = right.i;
        return *this;
    }

    HasPtrLikeValue &operator=(HasPtrLikeValue &&right) {
        if (this != &right) {
            delete ps;
            ps = right.ps;
            i = right.i;
            right.ps = 0;
        }
        return *this;
    }

    ~HasPtrLikeValue() {
        delete ps;
    }

    void printInfo() {
        printlnPointer(ps);
        println(*ps + std::string(" ") + std::to_string(i));
    }

private:
    std::string *ps;
    int i;
};

inline void swap(HasPtrLikeValue &left, HasPtrLikeValue &right) {
    println(std::string("call ") + __func__);
    using std::swap;
    swap(left.ps, right.ps);
    swap(left.i, right.i);
}

inline bool operator<(const HasPtrLikeValue &lhs, const HasPtrLikeValue &rhs) {
    return *lhs.ps < *rhs.ps;
}

// ex13.27
class HasPtrLikePointer {
private:
    std::string *ps;
    int i;
    std::size_t *use;
public:
    HasPtrLikePointer(const std::string &s = std::string("default"))
            : ps(const_cast<std::string *>(&s)), i(100), use(new size_t(1)) {
        println(__func__ + std::string(" ") + *ps);
    };

    HasPtrLikePointer(const HasPtrLikePointer &ref) : ps(ref.ps), i(ref.i), use(ref.use) {
        ++(*use);
        println(__func__ + std::string(" ") + *ps);
    };

    HasPtrLikePointer &operator=(HasPtrLikePointer &right) {
        std::string *temp_ps = right.ps;
        delete ps;
        ps = temp_ps;
        i = right.i;
        use = right.use;
        ++(right.use);
        return *this;
    }

    ~HasPtrLikePointer() {
        if (*use == 0) {
            delete ps;
            delete use;
        }
    }

    void printInfo() {
        printlnPointer(ps);
        println(*ps + std::string(" ") + std::to_string(i) + " use: " + std::to_string(*use));
    }
};


struct EX13 {
    std::string name;

    EX13(const std::string &str) : name(str) {
        std::cout << "EX13(): " << name << std::endl;
    }

    EX13(const EX13 &item) : name(item.name) {
        std::cout << "EX13(const EX13&): " << name << std::endl;
    }

    // EX13(EX13 &item) : name(item.name) {
    //     std::cout << "EX13(EX13&): " << name << std::endl;
    // }

    void printIsConstObj(const EX13 &item) const {
        println(__func__ + std::string(" ") + item.name);
    }

    void printIsConstObj(EX13 &item) const {
        println(__func__ + std::string(" ") + item.name);
    }

    void printIsConstObj(const EX13 &item) {
        println(__func__ + std::string(" ") + item.name);
    }

    void printIsConstObj(EX13 &item) {
        println(__func__ + std::string(" ") + item.name);
    }

    EX13 &operator=(const EX13 &) {
        std::cout << "EX13& operator=(const EX13&)" << name << std::endl;
        return *this;
    }

    ~EX13() { std::cout << "~EX13(): " << name << std::endl; }
};

class Numbered16 {
public:
    Numbered16() {
        objId = objIdInit++;
    }

    Numbered16(const Numbered16 &n) {
        objId = objIdInit++;
    }

    int objId;
    static int objIdInit;
    static const int classId = 10;
};

int Numbered16::objIdInit = 10; // 适用静态变量

class Employee18 {
public:
    Employee18() : name("default"), id(idStart++) {}

    Employee18(const std::string &_name) : name(_name), id(idStart++) {}

    /**
     * NOTE: 删除拷贝构造，拷贝赋值运算符
     */
    Employee18(const Employee18 &) = delete;

    Employee18 &operator=(const Employee18 &) = delete;

    void printInfo() {
        println(__func__ + std::string(" name:") + name + " id: " + std::to_string(id));
    }

private:
    std::string name;
    int id;
    static int idStart;
};

int Employee18::idStart = 100;


class Message;


class Folder {
    friend class Message;

    friend void swap(Folder &, Folder &);

    friend std::ostream &operator<<(std::ostream &out, const Folder &msg);

private:
    std::string content;
    std::set<Message *> messages;

    void remove_from_messages() {
        messages.clear(); // 不要儿子了
    }

    void add_to_messages(const Folder &f) {
        for (auto message : f.messages) {  // 爸爸要儿子了，不能调用儿子方法
            messages.insert(message);
        }
    }

    void addMessage(Message &m) {
        messages.insert(&m);
    }

    void removeMessage(Message &m) {
        messages.erase(&m);
    }

public:
    explicit Folder(const std::string &str = "default") : content(str) {}

    Folder(const Folder &item) : content(item.content), messages(item.messages) {
        add_to_messages(item);
    }

    Folder &operator=(const Folder &rhs) {
        if (this != &rhs) {
            remove_from_messages();
            content = rhs.content;
            messages = rhs.messages;
            add_to_messages(rhs);
        }
        return *this;
    }

    ~Folder() {
        remove_from_messages();
    }
};

inline void swap(Folder &lhs, Folder &rhs) {
    using std::swap;
    lhs.remove_from_messages(); // Use existing member function to avoid duplicate code.
    rhs.remove_from_messages(); // Use existing member function to avoid duplicate code.
    swap(lhs.messages, rhs.messages);
    swap(lhs.content, rhs.content);
    lhs.add_to_messages(lhs); // Use existing member function to avoid duplicate code.
    rhs.add_to_messages(rhs); // Use existing member function to avoid duplicate code.
}

inline std::ostream &operator<<(std::ostream &out, const Folder &msg) {
    out << "Folder(" << msg.content << " ,[ ";
    for (const auto &item : msg.messages) {
        out << std::hex << item << " , ";
    }
    out << "]";
    return out;
}

class Message {
    friend class Folder;

    friend void swap(Message &, Message &);

    friend std::ostream &operator<<(std::ostream &out, const Message &msg);

private:
    std::string content;
    std::set<Folder *> folders;

    void remove_from_folders() { // NOTE: 从folders中的每个folder中删除 本Message
        for (auto folder : folders) {  // 爸爸不要儿子了
            folder->removeMessage(*this);
        }
    }

    void add_to_folders(const Message &m) { // NOTE: 根据要拷贝的Message, 帮助m的父亲们添加this这个儿子
        for (auto folder : m.folders) {  // 爸爸要儿子了
            folder->addMessage(*this);
        }
    }

public:
    explicit Message(const std::string &str = "default") : content(str) {}

    Message(const Message &item) : content(item.content), folders(item.folders) {
        add_to_folders(item);
    }

    Message &operator=(const Message &rhs) {
        if (this != &rhs) {
            remove_from_folders();
            content = rhs.content;
            folders = rhs.folders;
            add_to_folders(rhs);
        }
        return *this;
    }

    ~Message() {
        remove_from_folders();
    }

    void addFolderAndHelpFolder(Folder &f) {  // save: 相当于指派了一个爸爸 f
        addFolder(&f); // folders.insert(&f);
        f.addMessage(*this);
    }

    void removeFolderAndHelpFolder(Folder &f) {  // remove: 相当于删除了一个爸爸
        removeFolder(&f); // folders.erase(&f);
        f.removeMessage(*this);
    }

    void addFolder(Folder *f) {
        folders.insert(f);
    }

    void removeFolder(Folder *f) {
        folders.erase(f);
    }
};

inline void swap(Message &lhs, Message &rhs) {
    using std::swap;
    lhs.remove_from_folders(); // Use existing member function to avoid duplicate code.
    rhs.remove_from_folders(); // Use existing member function to avoid duplicate code.
    swap(lhs.folders, rhs.folders);
    swap(lhs.content, rhs.content);
    lhs.add_to_folders(lhs); // Use existing member function to avoid duplicate code.
    rhs.add_to_folders(rhs); // Use existing member function to avoid duplicate code.
}

inline std::ostream &operator<<(std::ostream &out, const Message &msg) {
    out << "Message(" << msg.content << " ,[ ";
    for (const auto &item : msg.folders) {
        out << std::hex << item << " , ";
    }
    out << "]";
    return out;
}

// ex13.39
class StrVec {
    friend std::ostream &operator<<(std::ostream &out, const StrVec &msg);

private:
    std::allocator<std::string> alloc;
    std::string *elements, *first_free, *cap;

    void free() {
        if (elements) {
            // NOTE: version 1
            // while (first_free != elements) {
            //     alloc.destroy(--first_free);
            // }
            std::for_each(elements, first_free, [this](std::string &rhs) { alloc.destroy(&rhs); });
            alloc.deallocate(elements, cap - elements);
        }
    }

    std::pair<std::string *, std::string *> alloc_n_copy(const std::string *start, const std::string *end) {
        auto newData = alloc.allocate(end - start);
        return {newData, std::uninitialized_copy(start, end, newData)};
    }

    void check_n_alloc() {
        if (size() == capacity()) {
            reallocateByDouble();
        }
    }

    void reallocateByDouble() {
        auto newSize = size() ? size() * 2 : 1;
        reallocate(newSize);
    }

    void reallocate(int n) {
        auto newElements = alloc.allocate(n);
        // uninitialized_copy 第三个值是值拷贝，没有改变原来的 newElements
        auto newFirst = std::uninitialized_copy(begin(), end(), newElements);
        free();
        elements = newElements;
        first_free = newFirst;
        cap = n + newElements;
    }

    void range_initialize(const std::string *first, const std::string *last) {
        auto newdata = alloc_n_copy(first, last);
        elements = newdata.first;
        first_free = cap = newdata.second;
    }

public:
    StrVec() : elements(nullptr), first_free(nullptr), cap(nullptr) {};

    StrVec(std::initializer_list<std::string> li) {
        range_initialize(li.begin(), li.end());
    }

    StrVec(const StrVec &rhs) {
        range_initialize(rhs.begin(), rhs.end());
    }

    StrVec(StrVec &&rhs) : elements(rhs.elements), first_free(rhs.first_free), cap(rhs.cap) {
        rhs.elements = rhs.first_free = rhs.cap = nullptr;
    }

    StrVec &operator=(const StrVec &rhs) {
        auto rhs_tmp = alloc_n_copy(rhs.begin(), rhs.end());
        /**
            auto rhs_tmp = rhs;
            free();
            elements = rhs_tmp.elements;
            first_free = cap = rhs_tmp.cap;
            return *this;

            处理自赋值, 但这里rhs_tmp 这个对象会被析构，且rhs_tmp其析构函数释放了一个[elements, cap]故而报错
            main(40957,0x1003d0580) malloc: *** error for object 0x600003db8000: pointer being freed was not allocated
            main(40957,0x1003d0580) malloc: *** set a breakpoint in malloc_error_break to debug
         */
        free();
        elements = rhs_tmp.first;
        first_free = cap = rhs_tmp.second;
        return *this;
    }

    StrVec &operator=(StrVec &&rhs) { // 移动赋值
        if (this != &rhs) {
            free();
            elements = rhs.elements;
            first_free = rhs.first_free;
            cap = rhs.cap;
            rhs.elements = rhs.first_free = rhs.cap = nullptr;
        }
        return *this;
    }

    StrVec &operator=(const std::initializer_list<std::string> il) {
        auto data = alloc_n_copy(il.begin(), il.end());
        free();
        elements = data.first;
        first_free = cap = data.second;
        return *this;
    }


    std::string &operator[](std::size_t i) {
        return elements[i];
    }

    const std::string &operator[](std::size_t i) const {
        return elements[i];
    }

    void push_back(const std::string &str) {
        check_n_alloc();
        alloc.construct(first_free++, str);
    }

    void push_back(std::string &&str) {
        check_n_alloc();
        alloc.construct(first_free++, std::move(str));
    }

    ~StrVec() { free(); }

    void reserve(int n) {
        if (n < 0) { throw std::out_of_range("Please input a positive value. "); }
        if (n > capacity()) {
            reallocate(n);
        }
    }

    void resize(size_t count, const std::string &s) {
        if (count > size()) {
            if (count > capacity()) reserve(count * 2);
            for (size_t i = size(); i != count; ++i)
                alloc.construct(first_free++, s);
        } else if (count < size()) {
            while (first_free != elements + count)
                alloc.destroy(--first_free);
        }
    }

    std::size_t size() const { return (first_free - elements); }

    std::size_t capacity() const { return (cap - elements); }

    std::string *begin() const { return elements; }

    std::string *end() const { return first_free; }
};

inline std::ostream &operator<<(std::ostream &out, const StrVec &msg) {
    out << "size: " << msg.size() << " cap: " << msg.capacity() << " ele:[ ";
    auto func = [&out](const std::string &s) { out << s << ", "; };
    std::for_each(msg.begin(), msg.end(), func);
    out << " ]";
    return out;
}

// ex13.44
class String {
    friend std::ostream &operator<<(std::ostream &out, const String &msg);

public:
    String() : String("default") {}

    String(const char *s) {
        char *sl = const_cast<char *>(s);
        while (*sl)
            ++sl;
        range_initializer(s, ++sl);
    };

    String(const String &rhs) {
        range_initializer(rhs.elements, rhs.end);
        std::cout << "copy constructor" << " " << rhs << std::endl;
    };

    String &operator=(const String &rhs) {
        auto newstr = alloc_n_copy(rhs.elements, rhs.end);
        free();
        elements = newstr.first;
        end = newstr.second;
        std::cout << "copy-assignment" << " " << (*this) << std::endl;
        return *this;
    };

    // 移动构造
    String(String &&s) noexcept: elements(s.elements), end(s.end) {
        s.elements = s.end = nullptr;
    }

    // 移动赋值
    String &operator=(String &&rhs) noexcept {
        if (this != &rhs) {
            free();
            elements = rhs.elements;
            end = rhs.end;
            rhs.elements = rhs.end = nullptr;
        }
        return *this;
    }

    ~String() {
        free();
    };

    const char *c_str() const { return elements; }

    size_t size() const { return end - elements; }

    size_t length() const { return end - elements - 1; }

private:
    std::pair<char *, char *> alloc_n_copy(const char *b, const char *e) {
        auto str = alloc.allocate(e - b);
        return {str, std::uninitialized_copy(b, e, str)};
    };

    void range_initializer(const char *first, const char *last) {
        auto newstr = alloc_n_copy(first, last);
        elements = newstr.first;
        end = newstr.second;
    };

    void free() {
        if (elements) {
            std::for_each(elements, end, [this](char &c) { alloc.destroy(&c); });
            alloc.deallocate(elements, end - elements);
        }
    };

private:
    char *elements;
    char *end;
    std::allocator<char> alloc;
};

inline std::ostream &operator<<(std::ostream &out, const String &msg) {
    out << "size: " << msg.size() << " content: " << msg.c_str();
    return out;
}

// ex13.58
class EX58 {
private:
    std::vector<int> data;
public:
    EX58 sorted() &&{
        sort(data.begin(), data.end());
        std::cout << "sorted() &&" << std::endl; // debug
        return *this;
    };

    EX58 sorted() const &{
        /**
         * NOTE:正确写法
         */
        std::cout << "sorted() const &" << std::endl; // debug
        EX58 ret(*this);
        sort(ret.data.begin(), ret.data.end());
        return ret;

        /**
         * 死循环版本
         */
        // EX58 ret(*this);
        // ret.sorted();     // Exercise 13.56
        // return ret;

        /**
         * 临时右值EX58(*this)  版本
         */
        // return EX58(*this).sorted(); // Exercise 13.57
    };
};

#endif //TEST_CPP_EX13_H