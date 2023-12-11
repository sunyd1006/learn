#include <iostream>

using namespace std;

void sayHello() {
    cout << "hello world" << endl;
    // sayHello endl;
}

int main() {
    sayHello();

    int* ptr = 0;

    *ptr = 20;

    return 0;
}