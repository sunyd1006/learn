#include <iostream>
#include <iterator>
#include <vector>
#include <cstring>

#include <stdexcept>
#include "tool.h"

using namespace  std;

void un_declare() {
    int i, j;
    std::cin >> i >> j;
    if (j == 0) {
        // libc++abi: terminating with uncaught exception of type std::runtime_error: divisor is 0
        throw std::runtime_error("divisor is 0");
    }
    std::cout << i / j << std::endl;
}

void declare() {
    int i, j;
    while (std::cin >> i >> j && i != 0 && j != 0) {
        try {
            if (j == 0) {
                // libc++abi: terminating with uncaught exception of type std::runtime_error: divisor is 0
                throw std::runtime_error("divisor is 0");
            }
        } catch (runtime_error err) {
            cout << err.what() << " Try again ? yes or no " << endl;
            string com;
            do {
                cin >> com;
            } while (com == "yes" || com == "no");
            if (com == "no") break;
        }
        std::cout << i / j << std::endl;
    }
}

int main() {

    lnPrintln("\n习题 ------ ex5_25: ");
    un_declare();
    declare();

    cout << endl << "================= tmp ============" << endl;
    return 0;
};
