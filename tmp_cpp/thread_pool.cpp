#include <bits/stdc++.h>

#include "util.h"

class ThreadPool {
    std::vector<std::thread> workers;
    std::atomic_bool isRunning_;

    using Task = std::function<void()>;
    std::mutex mutex_;
    std::condition_variable cv_;
    std::list<Task> tasks_;

public:
    ThreadPool(int threads) : isRunning_(true) {
        for (size_t i = 0; i < threads; i++) {
            auto execFunc = [&]() {
                while (isRunning_.load()) {
                    Task task;
                    {
                        std::unique_lock<std::mutex> lock(mutex_);
                        cv_.wait(lock, [&] { return !isRunning_.load() || !tasks_.empty(); });

                        if (!isRunning_.load()) {
                            break;
                        }
                        task = tasks_.front();
                        tasks_.pop_front();
                    }
                    task();
                }
            };
            workers.emplace_back(std::thread(execFunc));
        }
    }

    ~ThreadPool() {
        {
            isRunning_.store(false);
        }

        // join all thread
        for (size_t i = 0; i < workers.size(); i++) {
            if (workers[i].joinable()) {
                workers[i].join();
            }
        }
    }

    void addTask(std::function<void()>&& task) {
        {
            std::lock_guard<std::mutex> lock(mutex_);
            tasks_.emplace_back(std::move(task));
        }
        cv_.notify_one();
    }
};

int main() {
    ThreadPool pool(10);

    for (size_t i = 0; i < 100; i++) {
        pool.addTask([i] { printLog("Starting task" + std::to_string(i)); });
    }
    return 0;
}