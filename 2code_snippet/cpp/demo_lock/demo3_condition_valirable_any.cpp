#include <iostream>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable> // 注意，any 版本也在这里面
#include <chrono>
#include <vector>
#include <shared_mutex>

class BoundedBuffer {
public:
    explicit BoundedBuffer(size_t size) : max_size_(size) {}

    // 生产者: 放入数据 (写操作)
    void produce(int data) {
        std::unique_lock<std::shared_mutex> lock(mutex_);

        // 等待直到队列不满
        // 使用 lambda 表达式作为 wait 的第二个参数，防止伪唤醒
        cv_producer_.wait(lock, [this]{ return queue_.size() < max_size_; });

        std::cout << "Producer producing " << data << "..." << std::endl;
        queue_.push(data);

        // 通知一个可能在等待的消费者
        cv_consumer_.notify_one();
    }

    // 消费者: 取出数据 (写操作)
    int consume() {
        std::unique_lock<std::shared_mutex> lock(mutex_);

        // 等待直到队列不空
        cv_consumer_.wait(lock, [this]{ return !queue_.empty(); });

        int data = queue_.front();
        queue_.pop();
        std::cout << "      Consumer " << std::this_thread::get_id() << " consuming " << data << "..." << std::endl;

        // 通知一个可能在等待的生产者
        cv_producer_.notify_one();
        return data;
    }

    // 观察者: 查看队列大小 (读操作)
    size_t getSize() const {
        // 使用 shared_lock 获取读锁
        std::shared_lock<std::shared_mutex> lock(mutex_);
        return queue_.size();
    }


private:
    const size_t max_size_;
    std::queue<int> queue_;

    // mutable 关键字是核心：允许在 const 成员函数中被修改(加锁/解锁)
    mutable std::shared_mutex mutex_;
    // 必须使用 condition_variable_any 与 shared_mutex 配合
    mutable std::condition_variable_any cv_producer_;
    mutable std::condition_variable_any cv_consumer_;
};

int main() {
    BoundedBuffer buffer(5);

    // 创建一个生产者线程
    std::thread producer_thread([&buffer]() {
        for (int i = 0; i < 10; ++i) {
            buffer.produce(i);
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        }
    });

    // 创建多个消费者线程
    std::vector<std::thread> consumer_threads;
    for (int i = 0; i < 3; ++i) {
        consumer_threads.emplace_back([&buffer]() {
            for (int j = 0; j < 3; ++j) { // 每个消费者消费3个
                buffer.consume();
                std::this_thread::sleep_for(std::chrono::milliseconds(250));
            }
        });
    }

    // 创建一个观察者线程
    std::thread observer_thread([&buffer]() {
        for (int i = 0; i < 20; ++i) {
            std::cout << "[Observer] Current buffer size: " << buffer.getSize() << std::endl;
            std::this_thread::sleep_for(std::chrono::milliseconds(150));
        }
    });

    producer_thread.join();
    for (auto& t : consumer_threads) {
        t.join();
    }
    // 注意：这里的observer可能在其他线程结束后仍在运行，
    // 在一个更鲁棒的系统中，需要一个合适的停止机制。
    // 为了示例简单，我们直接 join 它。
    observer_thread.join();

    return 0;
}
