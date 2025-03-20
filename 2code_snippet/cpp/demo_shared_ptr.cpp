#include <iostream>


// template<typename T>
// class mySharedPtr<T> {
    // T* ptr;


struct Obj{
public:
    Obj() {
        std::cout << "Obj() dtor" << std::endl;
    }
    virtual ~Obj() {
        std::cout << "Obj() dtor" << std::endl;
    }

    void printLine() {
        std::cout << "printLine" << std::endl;
    }
};


template <typename T>
class mySharedPtr {
    T* ptr_;
    std::atomic<int> counter_;
public:
    mySharedPtr(T* ptr): ptr_(ptr), counter_(1) {
        std::cout << "mySharedPtr()" << std::endl;
    }
    ~mySharedPtr() { // non virtual
        std::cout << "~mySharedPtr()" << std::endl;
        deletePtrIfLast();
    }
    void deletePtrIfLast() {
        if (--counter_ == 0) {
            std::cout << "dtor() delete ptr" << std::endl;
            if (ptr_ != nullptr) {
                delete ptr_;
            }
        }
    }

    Obj* operator->() {
        std::cout << "operator()" << std::endl;
        return ptr_;
    }

    // copy ctor & copy assign
    mySharedPtr(const mySharedPtr& other): ptr_(other.ptr_), counter_(other.counter_) {
        std::cout << "mySharedPtr(const mySharedPtr&)" << std::endl;
        ++counter_;
    }
    mySharedPtr& operator=(const mySharedPtr& other) {
        std::cout << "operator=(const mySharedPtr&)" << std::endl;
        if (this != &other) {
            deletePtrIfLast();
            ptr_ = other.ptr_;
            counter_ = other.counter_;
            ++counter_;
        }
        return *this;
    }

    // move ctor & move assign
};

int main() {
    mySharedPtr<Obj> ptr(new Obj());
    mySharedPtr<Obj> ptr2 = ptr;
    ptr->printLine();
    ptr2->printLine();

    return 0;
}