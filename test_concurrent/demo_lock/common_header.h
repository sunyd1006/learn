#ifndef demo_header
#define demo_header

#include <string>
#include <iostream>

class SharedObj
{
    std::string name;
public:
    SharedObj(const std::string &n) : name(n) {}

    void printf() {
        std::cout << "SharedObj: " << name << std::endl;
    }
};

#endif	// demo_header