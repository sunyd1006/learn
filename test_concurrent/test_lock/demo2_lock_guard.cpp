#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <chrono>
#include <stdexcept>

int counter = 0;
std::mutex mtx; // 保护counter

void increase_with_unlock(int time, int id) {
    for (int i = 0; i < time; i++) {
        mtx.lock();
        // 线程1上锁成功后，抛出异常：未释放锁。会导致其他线程无法获取锁而死等
        if (id == 1) {
            throw std::runtime_error("throw excption....");
        }
        // 当前线程休眠1毫秒
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
        counter++;
        mtx.unlock();
    }
}

void increase_with_lock_guard(int time, int id) {
    for (int i = 0; i < time; i++) {
        // std::lock_guard对象构造时，自动调用mtx.lock()进行上锁
        // std::lock_guard对象析构时，自动调用mtx.unlock()释放锁
        std::lock_guard<std::mutex> lk(mtx);
        // 线程1上锁成功后，抛出异常：未释放锁, 但lock_guard对象析构时会自动释放锁
        if (id == 1) {
            throw std::runtime_error("throw exception....");
        }
        // 当前线程休眠1毫秒
        std::this_thread::sleep_for(std::chrono::milliseconds(1));
        counter++;
    }
}

void increase_wrapper(int time, int id) {
    try {
        // increase_with_unlock(time, id);
        increase_with_lock_guard(time, id);
    } catch (const std::exception& e){
        std::cout << "id:" << id << ", " << e.what() << std::endl;
    }
}

int main(int argc, char** argv) {
    std::thread t1(increase_wrapper, 10000, 1);
    std::thread t2(increase_wrapper, 10000, 2);
    t1.join();
    t2.join();
    std::cout << "counter:" << counter << std::endl;
    return 0;
}
