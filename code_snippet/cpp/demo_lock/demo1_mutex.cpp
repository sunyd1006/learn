#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <chrono>
#include <stdexcept>

int counter = 0;
std::mutex mtx; // 保护counter

void increase_with_lock(int time) {
    for (int i = 0; i < time; i++) {
        mtx.lock();
        // 当前线程休眠1毫秒
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
        counter++;
        mtx.unlock();
    }
}

void increase_with_no_lock(int time) {
    for (int i = 0; i < time; i++) {
        // 当前线程休眠1毫秒
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
        counter++;
    }
}

void increase_wrapper(int times) {
    increase_with_lock(times);
    // increase_with_no_lock(times);
}

int main(int argc, char** argv) {
    std::thread t1(increase_wrapper, 10000);
    std::thread t2(increase_wrapper, 10000);

    t1.join();
    t2.join();
    std::cout << "counter:" << counter << std::endl;
    return 0;
}