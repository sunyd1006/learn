#include <algorithm>
#include <list>
template <typename T>
std::list<T> sequential_quick_sort(std::list<T> input) {
    if (input.empty()) {
        return input;
    }
    std::list<T> result;
    result.splice(result.begin(), input, input.begin());
    T const& pivot = *result.begin();
    auto divide_point =
        std::partition(input.begin(), input.end(), [&](T const& t) { return t < pivot; });
    std::list<T> lower_part;
    lower_part.splice(lower_part.end(), input, input.begin(), divide_point);
    auto new_lower(sequential_quick_sort(std::move(lower_part)));
    auto new_higher(sequential_quick_sort(std::move(input)));
    result.splice(result.end(), new_higher);
    result.splice(result.begin(), new_lower);
    return result;
}

#include <iostream>

int main() {
    std::list<int> l{1, 2, 3, 4, 5, 6, 7, 8, 9};
    auto result = sequential_quick_sort(l);
    for (auto& once: result) {
        std::cout << once << ", ";
    }

    return 0;
}
