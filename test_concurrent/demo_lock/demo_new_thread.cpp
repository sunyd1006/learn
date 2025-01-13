#include <iostream>
#include <thread>

void threadFunction() {
    std::cout << "Hello from thread!\n";
}

int main() {
    std::thread t(threadFunction);
    // C++ 标准要求，在主线程退出之前，所有的 std::thread 对象必须被显式地管理（通过 join 或 detach）。
    // 否则主线程在退出时会销毁 std::thread 对象 t，如果 t 是可加入状态，会触发 std::terminate()，程序会触发未定义行为可能导致崩溃或不可预测的错误

    // t.join();
    return 0;
}
    // 如果没有 join 或 detach，会导致未定义行为, libc++abi: terminating
