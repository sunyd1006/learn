#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <chrono>
#include <stdexcept>
#include <condition_variable>

#include "common_header.h"

std::once_flag resource_flag;     // 1
std::shared_ptr<SharedObj> resource_ptr;

void init_resource() {
    resource_ptr.reset(new SharedObj("abc"));
}

int main(int argc, char** argv) {
    std::call_once(resource_flag,init_resource);
    resource_ptr->printf();
}