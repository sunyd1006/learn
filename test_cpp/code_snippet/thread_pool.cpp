#include <iostream>
#include <vector>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
#include <functional>

class ThreadPool
{
public:
    ThreadPool(size_t numThreads);
    ~ThreadPool();

    // 提交任务到线程池
    template <class F, class... Args>
    auto enqueue(F&& f, Args&&... args)
        -> std::future<typename std::result_of<F(Args...)>::type>;

private:
    // 线程池中的工作线程
    std::vector<std::thread> workers;

    // 线程同步
    std::mutex mutex;
    std::condition_variable condition;
    std::queue<std::function<void()>> taskQueue; // 任务队列
    bool isStopped;
};

// 线程池构造函数
ThreadPool::ThreadPool(size_t numThreads) : isStopped(false)
{
    for (size_t i = 0; i < numThreads; ++i) {
        workers.emplace_back([this] {
            while (true) {
                std::function<void()> task;
                {
                    std::unique_lock<std::mutex> lock(this->mutex);
                    this->condition.wait(lock, [this] {
                        return this->isStopped || !this->taskQueue.empty();
                    });
                    if (this->isStopped && this->taskQueue.empty()) {
                        return;
                    }
                    task = std::move(this->taskQueue.front());
                    this->taskQueue.pop();
                }
                task();
            }
        });
    }
}

// 线程池析构函数
ThreadPool::~ThreadPool()
{
    {
        std::unique_lock<std::mutex> lock(mutex);
        isStopped = true;
    }
    condition.notify_all();
    for (std::thread& worker : workers) {
        if (worker.joinable()) {
            worker.join();
        }
    }
}

// 任务提交方法（C++11 兼容）
template <class F, class... Args>
auto ThreadPool::enqueue(F&& f, Args&&... args)
    -> std::future<typename std::result_of<F(Args...)>::type>
{
    using returnType = typename std::result_of<F(Args...)>::type;

    auto task = std::make_shared<std::packaged_task<returnType()>>(
        std::bind(std::forward<F>(f), std::forward<Args>(args)...));

    std::future<returnType> res = task->get_future();
    {
        std::unique_lock<std::mutex> lock(mutex);
        if (isStopped) {
            throw std::runtime_error("ThreadPool is stopped!");
        }
        taskQueue.emplace([task]() { (*task)(); });
    }
    condition.notify_one();
    return res;
}

int main()
{
    ThreadPool pool(4); // 创建一个线程池，包含 4 个线程

    auto task1 = pool.enqueue([] {
        return "Hello from task 1";
    });
    auto task2 = pool.enqueue(
        [](int x) {
            return x * x;
        },
        10);
    auto task3 = pool.enqueue([] {
        std::this_thread::sleep_for(std::chrono::seconds(2));
        return "Task 3 finished";
    });

    std::cout << task1.get() << std::endl;
    std::cout << task2.get() << std::endl;
    std::cout << task3.get() << std::endl;

    return 0;
}