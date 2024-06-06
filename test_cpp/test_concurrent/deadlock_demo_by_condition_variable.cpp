#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

std::mutex mutex1, mutex2;
std::condition_variable cond1, cond2;
bool ready1 = false, ready2 = false;

void ThreadFunc1() {
    std::unique_lock<std::mutex> lock1(mutex1); // 获取第一个互斥锁
    cond1.wait(lock1, [] { return ready1; });   // 等待条件变量1得到满足，即ready1变为true

    {
        std::lock_guard<std::mutex> lock2(mutex2); // 获取第二个互斥锁
        ready2 = true;                             // 设置ready2
        cond2.notify_one();                        // 通知等待cond2的线程
    }

    // 执行额外的操作 (在这里不会进行任何操作)
}

void ThreadFunc2() {
    std::unique_lock<std::mutex> lock2(mutex2); // 获取第二个互斥锁
    cond2.wait(lock2, [] { return ready2; });   // 等待条件变量2得到满足，即ready2变为true

    {
        std::lock_guard<std::mutex> lock1(mutex1); // 获取第一个互斥锁
        ready1 = true;                             // 设置ready1
        cond1.notify_one();                        // 通知等待cond1的线程
    }

    // 执行额外的操作 (在这里不会进行任何操作)
}

int main() {
    std::thread thread1(ThreadFunc1);
    std::thread thread2(ThreadFunc2);

    thread1.join();
    thread2.join();

    std::cout << "程序结束，该输出不会出现，因为程序已经死锁" << std::endl;
    return 0;
}
