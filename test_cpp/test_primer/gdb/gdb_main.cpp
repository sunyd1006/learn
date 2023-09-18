#include <iostream>

using namespace std;

void sayHello() {
    cout << "hello world" << endl;
    // sayHello endl;
}

int main() {
    int arr[4] = {0, 1, 2, 3};
    int i = 10;
    
    for (i = 0; i < 4; i++) {
        cout << arr[i] << " ";
    }

    cout << endl;

    sayHello();

    return 0;
}