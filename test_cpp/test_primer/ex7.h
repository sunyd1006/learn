//
// Created by sunyindong on 2022/1/3.
//

#ifndef TEST_CPP_EX7_H
#define TEST_CPP_EX7_H

class Account {
public:
    void calculate() { amount += amount * interestRate; }
    static double rate() { return interestRate; }
    static void rate(double newRate) { interestRate = newRate; }

private:
    std::string owner;
    double amount;
    static double interestRate;
    static constexpr double todayRate = 42.42;
    static double initRate() { return todayRate; }
};

double Account::interestRate = initRate();


// -----------------------

class Person {
    friend std::istream &read(std::istream &is, Person &person);
    friend std::ostream &print(std::ostream &os, const Person &person);
public:
    void printString() {
        std::cout << " name: " << name << " address: " << address << std::endl;
    }
    std::string getName() const { return name; }
    std::string getAddress() const { return address; }

public:
    Person() = default;
    Person(std::string n, std::string addr) :name(n), address(addr) {}
    Person(std::istream &is){ read(is, *this); }
private:
    std::string name;
    std::string address;
};

std::istream &read(std::istream &is, Person &person){
    is >> person.name >> person.address;
    return is;
}

std::ostream &print(std::ostream &os, const Person &person){
    os << person.name << " " << person.address;
    return os;
}

#endif //TEST_CPP_EX7_H
