#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <chrono>
#include <stdexcept>
#include <condition_variable>

std::mutex mtx;
std::condition_variable cv;
bool data_ready = false;

void worker_thread() {
    std::unique_lock<std::mutex> lock(mtx); // 使用unique_lock管理互斥量的锁
    cv.wait(lock, []{ return data_ready; }); // 等待时自动解锁，被唤醒后自动加锁
    // 条件满足，继续执行
    std::cout << "Worker thread: Data is ready!" << std::endl;
}

// void increase_wrapper(int time, int id) {
//     try {
//         // increase_with_unlock(time, id);
//         increase_with_lock_guard(time, id);
//     } catch (const std::exception& e){
//         std::cout << "id:" << id << ", " << e.what() << std::endl;
//     }
// }

int main(int argc, char** argv) {
    std::thread t(worker_thread);

    // 模拟数据准备过程
    std::this_thread::sleep_for(std::chrono::seconds(2));
    {
        std::lock_guard<std::mutex> lock(mtx); // 加锁
        data_ready = true; // 设置条件变量为true
    }
    cv.notify_one(); // 通知等待线程条件已满足
    t.join();

    return 0;
}
