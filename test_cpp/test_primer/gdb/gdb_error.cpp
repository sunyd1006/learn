#include <iostream>

using namespace std;

void sayHello() {
    cout << "hello world" << endl;
    // sayHello endl;
}

int main() {
    int arr[4] = {0, 1, 2, 3};
    int var = 10;

    for (int i = 0; i < 4; i++) {
        cout << arr[i] << " ";
    }

    cout << endl;

    while (true) {
        sayHello();
    }

    return 0;
}

/**
 
g++ -g gdb_error.cpp -o gdb_main.out
lldb -p pid 
b 20
n
 */