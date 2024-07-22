#include <condition_variable>
#include <mutex>
#include <queue>
#include <thread>

#include "time_util.h"

// struct data_chunk;
#define data_chunk int

std::mymutex mut;
std::queue<data_chunk> data_queue;
std::condition_variable_any data_cond;
int data_total_num = 2;

bool more_data_to_prepare() { return data_total_num <= 0 ? false : true; }
data_chunk prepare_data() {
    data_total_num--;
    return data_chunk(data_total_num);
}

void process(data_chunk& data_chunk_index) { println("process data: " + std::to_string(data_chunk_index)); }
bool is_last_chunk(data_chunk& data_chunk_index) { return data_chunk_index == 0; }

void data_preparation_thread() {
    while (more_data_to_prepare()) {
        data_chunk const data = prepare_data();
        std::lock_guard<std::mymutex> lk(mut);
        data_queue.push(data);
        data_cond.notify_one();
    }
}

void data_processing_thread() {
    while (true) {
        std::unique_lock<std::mymutex> lk(mut);

        // 先获取到锁，然后判断pred是否满足，满足则wait()返回并继续持有锁，顺利执行；不满足则wait()不返回, 内部会释放锁
        //                              ，否则继续等待
        data_cond.wait(lk, [] {
            auto ret = !data_queue.empty();
            println("get locked, and determinate whether the condition is met, ret: " + std::to_string(ret), "  ");
            return ret;
        });
        data_chunk data = data_queue.front();
        data_queue.pop();
        lk.unlock();
        process(data);
        if (is_last_chunk(data)) break;
    }
}

int main() {
    std::thread t2(data_processing_thread);
    std::thread t1(data_preparation_thread);

    std::this_thread::sleep_for(std::chrono::milliseconds(10));
    t2.join();
    t1.join();
}
