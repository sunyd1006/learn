#include <iostream>
#include <thread>

void threadFunction() {
    std::cout << "Hello from thread!\n";
}

int main() {
    std::thread t(threadFunction);
    // 如果没有 join 或 detach，会导致未定义行为
    t.join();
    return 0;
}
