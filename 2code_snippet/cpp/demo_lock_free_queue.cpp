#include <bits/stdc++.h>


template <typename T>
class SimpleLockFreeQueue {
private:
    struct Node {
        T data;
        std::atomic<Node*> next;

        Node(T val) : data(val), next(nullptr) {}
    };

    std::atomic<Node*> head, tail;

public:
    LockFreeQueue() {
        // assert T is default
        // 创建哨兵节点
        Node* dummy = new Node(T{});
        head.store(dummy);
        tail.store(dummy);
    }

    ~LockFreeQueue() {
        while (Node* node = head.load()) {
            head.store(node->next);
            delete node;
        }
    }

    // 入队
    void enqueue(const T& value) {
        Node* new_node = new Node(value);
        Node* old_tail;

        while (true) {
            old_tail = tail.load();
            Node* next = old_tail->next.load();
            if (next == nullptr) {  // tail 是末尾
                if (old_tail->next.compare_exchange_weak(next, new_node)) {
                    break;
                }
            } else {  // tail 落后了，推进它
                tail.compare_exchange_weak(old_tail, next);
            }
        }

        // 推进 tail
        tail.compare_exchange_weak(old_tail, new_node);
    }

    // 出队
    bool dequeue(T& result) {
        Node* old_head;

        while (true) {
            old_head = head.load();
            Node* next = old_head->next.load();
            if (next == nullptr) {
                return false;  // 队列为空
            }

            if (head.compare_exchange_weak(old_head, next)) {
                result = next->data;
                delete old_head;  // 删除旧的 dummy 节点
                return true;
            }
        }
    }
};

// Lock-Free Queue based on Michael & Scott (1996)
template <typename T>
class LockFreeQueue {
private:
    struct Node {
        T data;
        std::atomic<Node*> next;

        explicit Node(const T& val) : data(val), next(nullptr) {}
    };

    std::atomic<Node*> head, tail;

public:
    LockFreeQueue() {
        static_assert(std::is_default_constructible<T>::value, "T must be default constructible");
        Node* dummy = new Node(T{});
        head.store(dummy, std::memory_order_relaxed);
        tail.store(dummy, std::memory_order_relaxed);
    }

    ~LockFreeQueue() {
        // 注意：析构前必须确保无其他线程在访问队列
        while (Node* node = head.load(std::memory_order_relaxed)) {
            head.store(node->next.load(std::memory_order_relaxed), std::memory_order_relaxed);
            delete node;
        }
    }

    void enqueue(const T& value) {
        Node* new_node = new Node(value);
        Node* old_tail;
        int spin = 0;

        while (true) {
            old_tail = tail.load(std::memory_order_acquire);
            Node* next = old_tail->next.load(std::memory_order_acquire);

            if (next == nullptr) {
                if (old_tail->next.compare_exchange_weak(next, new_node, std::memory_order_release, std::memory_order_relaxed)) {
                    break;
                }
            } else {
                tail.compare_exchange_weak(old_tail, next, std::memory_order_release, std::memory_order_relaxed);
            }

            if (++spin > 100) std::this_thread::yield();
        }

        tail.compare_exchange_weak(old_tail, new_node, std::memory_order_release, std::memory_order_relaxed);
    }

    bool dequeue(T& result) {
        Node* old_head;
        int spin = 0;

        while (true) {
            old_head = head.load(std::memory_order_acquire);
            Node* next = old_head->next.load(std::memory_order_acquire);

            if (next == nullptr) {
                return false; // 空队列
            }

            if (head.compare_exchange_weak(old_head, next, std::memory_order_release, std::memory_order_relaxed)) {
                result = next->data;
                delete old_head; // 删除旧的 dummy 节点
                return true;
            }

            if (++spin > 100) std::this_thread::yield();
        }
    }
};

// // 测试框架
// class TestHarness {
// private:
//     static constexpr int OPS_PER_THREAD = 100000;

//     LockFreeQueue<int> queue;
//     std::atomic<int> enqueue_count{0}, dequeue_count{0};
//     std::vector<int> produced, consumed;

// public:
//     TestHarness() {
//         produced.resize(OPS_PER_THREAD * std::thread::hardware_concurrency());
//         consumed.resize(OPS_PER_THREAD * std::thread::hardware_concurrency());
//     }

//     void producer(int id) { // 生产者线程函数
//         std::mt19937 rng(id);
//         for (int i = 0; i < OPS_PER_THREAD; ++i) {
//             int value = rng();
//             queue.enqueue(value);
//             int index = enqueue_count.fetch_add(1);
//             produced[index] = value;
//         }
//     }

//     void consumer(int id) { // 消费者线程函数
//         int local_sum = 0;
//         int count = 0;
//         int value;
//         for (int i = 0; i < OPS_PER_THREAD; ) {
//             if (queue.dequeue(value)) {
//                 local_sum += value;
//                 int index = dequeue_count.fetch_add(1);
//                 if (index < (int)consumed.size()) {
//                     consumed[index] = value;
//                 }
//                 ++count;

//                 ++i;
//             }
//         }
//     }

//     // 执行测试
//     void run_test(int num_producers, int num_consumers) {
//         std::cout << "Running test with " << num_producers << " producers and "
//                   << num_consumers << " consumers..." << std::endl;

//         std::vector<std::thread> producers, consumers;

//         // 启动生产者线程
//         auto start = std::chrono::high_resolution_clock::now();
//         for (int i = 0; i < num_producers; ++i) {
//             producers.emplace_back(&TestHarness::producer, this, i);
//         }

//         // 启动消费者线程
//         for (int i = 0; i < num_consumers; ++i) {
//             consumers.emplace_back(&TestHarness::consumer, this, i);
//         }

//         // 等待所有线程完成
//         for (auto& t : producers) t.join();
//         for (auto& t : consumers) t.join();
//         auto end = std::chrono::high_resolution_clock::now();

//         // 计算总操作数和吞吐量
//         int total_ops = enqueue_count.load();
//         auto duration =
//             std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
//         double throughput = total_ops / (duration / 1000.0) / 1000000.0;

//         // 验证结果
//         std::sort(produced.begin(), produced.begin() + total_ops);
//         std::sort(consumed.begin(), consumed.begin() + total_ops);

//         bool correct = (produced == consumed);
//         std::cout << "Test " << (correct ? "PASSED" : "FAILED") << std::endl;
//         std::cout << "Enqueued: " << enqueue_count.load()
//                   << ", Dequeued: " << dequeue_count.load() << std::endl;
//         std::cout << "Throughput: " << throughput << " M ops/sec" << std::endl;
//         std::cout << "----------------------------------------" << std::endl;
//     }
// };


// int main() {
//     TestHarness harness;

//     // 测试1: 1生产者1消费者
//     harness.run_test(1, 1);

//     // 测试2: 多生产者单消费者
//     harness.run_test(std::thread::hardware_concurrency(), 1);

//     // 测试3: 单生产者多消费者
//     harness.run_test(1, std::thread::hardware_concurrency());

//     // 测试4: 多生产者多消费者
//     harness.run_test(std::thread::hardware_concurrency(), std::thread::hardware_concurrency());

//     return 0;
// }


std::mutex log_mutex;
void test_single_thread_correctness() {
    LockFreeQueue<int> q;
    for (int i = 0; i < 10; ++i) q.enqueue(i);
    for (int i = 0; i < 10; ++i) {
        int val;
        bool success = q.dequeue(val);
        assert(success);
        assert(val == i);
    }
    int dummy;
    assert(!q.dequeue(dummy));  // 空队列
    std::lock_guard<std::mutex> lk(log_mutex);
    std::cout << "[PASS] Single thread correctness test" << std::endl;
}

void test_multi_thread_enqueue_dequeue() {
    LockFreeQueue<int> q;
    constexpr int threadNum = 4;
    constexpr int msgNum = 10000;
    std::vector<std::thread> producers, consumers;
    std::atomic<int> produced{0}, consumed{0};
    std::unordered_set<int> result;
    std::mutex result_mutex;

    for (int i = 0; i < threadNum; ++i) { // 多线程入队
        producers.emplace_back([&q, &produced, i]() {
            for (int j = 0; j < msgNum; ++j) {
                q.enqueue(i * msgNum + j);
                produced.fetch_add(1);
            }
        });
    }

    for (int i = 0; i < threadNum; ++i) { // 多线程出队
        consumers.emplace_back([&]() {
            int val;
            while (consumed.load() < threadNum * msgNum) {
                if (q.dequeue(val)) {
                    std::lock_guard<std::mutex> lg(result_mutex);
                    result.insert(val);
                    consumed.fetch_add(1);
                }
            }
        });
    }

    for (auto& t : producers) t.join();
    for (auto& t : consumers) t.join();

    std::ostringstream oss;
    oss << "expect: " << result.size() << " real: " << threadNum * msgNum << std::endl;
    std::cout << oss.str();
    assert(result.size() == threadNum * msgNum);
    std::lock_guard<std::mutex> lk(log_mutex);
    std::cout << "[PASS] Multi-thread enqueue/dequeue test" << std::endl;
}

void test_empty_queue_concurrent_dequeue() {
    LockFreeQueue<int> q;
    constexpr int threadNum = 8;
    std::vector<std::thread> threads;

    for (int i = 0; i < threadNum; ++i) {
        threads.emplace_back([&]() {
            int val;
            for (int j = 0; j < 1000; ++j) {
                q.dequeue(val);  // 尝试出队空队列
            }
        });
    }

    for (auto& t : threads) t.join();
    std::lock_guard<std::mutex> lk(log_mutex);
    std::cout << "[PASS] Empty queue concurrent dequeue test" << std::endl;
}

void test_pressure() {
    LockFreeQueue<int> q;
    std::atomic<bool> running{true};
    std::atomic<int> counter{0};
    std::thread producer([&]() {
        while (running.load()) {
            q.enqueue(counter++);
        }
    });

    std::thread consumer([&]() {
        int val;
        while (running.load()) {
            q.dequeue(val);
        }
    });

    std::this_thread::sleep_for(std::chrono::seconds(2));
    running.store(false);
    producer.join();
    consumer.join();
    std::lock_guard<std::mutex> lk(log_mutex);
    std::cout << "[PASS] Pressure/stress test" << std::endl;
}

int main() {
    test_single_thread_correctness();
    test_multi_thread_enqueue_dequeue();
    test_empty_queue_concurrent_dequeue();
    test_pressure();
    return 0;
}