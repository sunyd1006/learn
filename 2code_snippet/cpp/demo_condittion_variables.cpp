//
// Created by sunyindong.syd on 2025/7/31.
//

#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

std::atomic<bool> isRunning(true);

std::mutex g_mtx;
std::condition_variable g_cv;
bool g_done = false; // 条件标志

void producerByReady()
{
    while (isRunning)
    {
        std::this_thread::sleep_for(std::chrono::seconds(2));
        {
            std::lock_guard<std::mutex> lk(g_mtx);
            g_done = true;
            g_cv.notify_one(); // 主动唤醒,
            // 不要求或许锁后再通知。
            // 但是获取锁后通知，能确保获取锁---通知语句之间的信息被看到，避免
        }
    }
}

void consumerLoop()
{
    while (isRunning)
    {
        std::unique_lock<std::mutex> lk(g_mtx);
        bool predRet = g_cv.wait_for(lk, std::chrono::seconds(4), [] { return g_done; });

        if (predRet)
        {
            std::cout << "Received notify (work finished)!\n";
        }
        else
        {
            std::cout << "Timeout, still waiting...\n";
        }
        g_done = false;
    }
}

// 1. produer定期设置ready条件
// 2. consumer循环等待ready条件
int main()
{
    std::thread t1(producerByReady);
    std::thread t3(consumerLoop);

    std::this_thread::sleep_for(std::chrono::seconds(10));
    t1.join();
    t3.join();
    return 0;
}
