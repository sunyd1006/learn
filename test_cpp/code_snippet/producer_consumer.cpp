#include <chrono>
#include <condition_variable>
#include <future>
#include <iostream>
#include <mutex>
#include <queue>

// 注意某些调用可能会抛出std::system_error， 为了简单（偷懒），我没有去捕获
std::mutex mutex;
std::condition_variable cv;
std::queue<int> msgQueue;
bool isStopped = false;

void producer(int start, int end)
{
    for (size_t i = start; i < end; i++) {
        if (isStopped) {
            std::cout << "Produce end" << std::endl;
            return;
        }
        {
            std::lock_guard<std::mutex> _(mutex);
            msgQueue.emplace(i);
        }
        std::cout << "Produce " << i << std::endl;
        cv.notify_one();
    }
}

void consumer() {
    while (!isStopped) {
        std::unique_lock<std::mutex> lock(mutex);
        cv.wait(lock, []{
            // isStopped 也需要唤醒consumer，否则consumer会一直无法退出最少main退出时抛terminate
            return !msgQueue.empty() || isStopped;
        });

        if (!isStopped) {
            int msg = msgQueue.front();
            msgQueue.pop();
            std::cout << "Consume " << msg << std::endl;
        }
    }
    std::cout << "Consume end" << std::endl;
}

int main()
{
    std::thread p1(producer, 1, 10);
    std::thread p2(producer, 2, 20);
    std::thread p3(producer, 3, 30);

    std::thread c1(consumer);
    std::thread c2(consumer);

    std::this_thread::sleep_for(std::chrono::seconds(3));

    {
        std::lock_guard<std::mutex> _(mutex);
        isStopped = true;
    }
    cv.notify_all();

    // 所有线程必须被显示的join()或者detach(), 否则抛terminating错误
    p1.join();
    p2.join();
    p3.join();
    c1.join();
    c2.join();
}
