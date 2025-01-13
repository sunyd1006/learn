#ifndef TIME_UTIL_H
#define TIME_UTIL_H

#include <chrono>
#include <iostream>
#include <string>

void println(std::string desc, std::string empty_prefix="") {
    std::cout << empty_prefix << desc << std::endl;
}

// 获取当前时间（以毫秒为单位）
long long getCurrentTimeMs() {
    auto now = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(now.time_since_epoch());
    return duration.count();
}


namespace std {
    class mymutex {
        std::mutex m;
    public:
        mymutex() = default;
        mymutex(const mymutex&) = delete;
        mymutex& operator=(const mymutex&) = delete;

        void lock() {
            m.lock();
            std::cout << "lock: " << std::endl;
        }
        void unlock() {
            std::cout << "      unlock: " << std::endl;
            m.unlock();
        }
        void trylock() {
            m.try_lock();
            std::cout << "try_lock: " << std::endl;
        }
    };
}

class Timer {
public:
    Timer() {
        reset();
    }
    // 记录开始时间
    void reset() {
        startTime = std::chrono::high_resolution_clock::now();
    }

    // 获取当前时间到上次调用 start() 之间的间隔（以毫秒为单位）
    long long getIntervalMs() {
        auto now = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(now - startTime);
        startTime = now;
        return duration.count();
    }

    // 获取当前时间到上次调用 start() 之间的间隔（以纳秒为单位）
    long long getIntervalNs() {
        auto now = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(now - startTime);
        startTime = now;
        return duration.count();
    }

private:
    std::chrono::high_resolution_clock::time_point startTime;
};

#endif // TIME_UTIL_H
