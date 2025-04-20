#include <bits/stdc++.h>

#include <iomanip>

#include "util.h"

using namespace std;

void vector_demo() {
    std::vector<int> res{1};
    std::vector<int> another{2, 3};

    // 在尾部插入
    res.insert(res.end(), another.begin(), another.end());
    println(res);  // 1, 2, 3
    // 在头部插入
    res.insert(res.begin(), another.begin(), another.end());
    println(res);  // 2, 3, 1, 2, 3

    // 按降序排序
    std::vector<int> vec = {5, 2, 8, 1, 3};
    std::sort(vec.begin(), vec.end(), [](int a, int b) {
        return a < b;  // a < b → 升序（a 小，排前）
    });
    println(vec, "升序排列: ");
    std::sort(vec.begin(), vec.end(), [](int a, int b) {
        return a > b;  // a > b → 降序（a 大，排前）
    });
    println(vec, "降序排列: ");

    // resize() 会改变size，相当于含参数n的构造函数；
    // 但是reserve() 不会改变size, 它只改变了capacity()
    //   1. vector<vector<int>> sortedInput(maxIdx + 1);
    //   2. vector<vector<int>> sortedInput;  sortedInput.resize(maxIdx+1);
    std::vector<int> v;
    v.reserve(10);
    assert(v.capacity() == 10);
    assert(v.size() == 0);
    v.resize(1);
    assert(v.capacity() == 10);
    assert(
        v.size() ==
        1);  // 改变 vector 中实际元素的数量（size）。
             // 如果新大小大于当前大小，则新增元素（默认构造或指定值）。如果新大小小于当前大小，则销毁多余的元素。
}

void set_demo() {
    // 有序集合：自动排序，基于红黑树（log n 操作）
    set<int> s;
    s.insert(5);
    s.insert(2);
    s.insert(8);
    s.insert(5);  // 重复元素自动忽略

    cout << "set中元素（自动排序）: ";
    for (int x : s) cout << x << " ";
    cout << endl;

    s.erase(2);                                  // 删除元素
    cout << "count(5): " << s.count(5) << endl;  // 是否存在，存在返回 1，否则 0

    auto it = s.find(5);  // 查找元素
    if (it != s.end()) cout << "找到元素: " << *it << endl;

    // lower_bound: 返回 >= x 的第一个元素
    it = s.lower_bound(6);
    if (it != s.end()) cout << ">=6 的最小元素是: " << *it << endl;
}

void string_demo() {
    std::string str = "hello....world";
    char ch = '.';

    std::string s = "hello";
    s += " world";                               // 拼接
    cout << s.substr(0, 5) << endl;              // 子串, 返回 [pos, pos + count) 的字符串
    s.insert(5, "123");                          // 插入，最终为：hello123 world
    s.erase(5, 3);                               // 删除，最终为：hello world
    reverse(s.begin(), s.end());                 // 翻转, 最终为dlrow olle
    cout << "ll idx: " << s.find("ll") << endl;  // 查找
    sort(s.begin(), s.end());                    // 排序

    // find char idx
    auto findIdxFunc = [](const std::string &str, char ch) -> int {
        auto it = std::find(str.begin(), str.end(), ch);
        if (it == str.end()) {
            return -1;
        }
        return it - str.begin();
    };
    assert(findIdxFunc(str, ch) == 5);

    // 删除一个字符的函数
    auto removeCharFunc = [](const std::string &raw, char one) -> std::string {
        std::string res = raw;
        // std::remove one item.
        res.erase(std::remove(res.begin(), res.end(), one), res.end());
        return res;
    };
    assert(removeCharFunc(std::string("hello_world"), '_') == std::string("helloworld"));

    // 解释
    std::string str2 = "hello....world";
    std::remove(str2.begin(), str2.end(), ch);
    assert(str2 == "helloworldorld");
    str.erase(std::remove(str.begin(), str.end(), ch), str.end());
    assert(str == "helloworld");
}

// ------------------ list 常用函数（双向链表） ------------------
void list_demo() {
    list<int> l = {1, 2, 3};
    l.push_back(4);
    l.push_front(0);
    l.pop_back();
    l.pop_front();

    l.insert(next(l.begin()), 99);  // 在第2个位置插入
    println(l, "after insert");
    l.erase(prev(l.end()));  // 删除最后一个元素
    println(l, "after erase");

    cout << endl;
}

// ------------------ 优先队列（堆） ------------------
void heap_demo() {
    // 默认是大顶堆（最大值优先）
    priority_queue<int> max_heap;
    max_heap.push(3);
    max_heap.push(1);
    max_heap.push(5);
    while (!max_heap.empty()) {
        cout << max_heap.top() << " ";
        max_heap.pop();
    }
    cout << endl;

    // 小顶堆（最小值优先）
    priority_queue<int, vector<int>, greater<int>> min_heap;
    min_heap.push(3);
    min_heap.push(1);
    min_heap.push(5);
    while (!min_heap.empty()) {
        cout << min_heap.top() << " ";
        min_heap.pop();
    }
    cout << endl;
}

void atomic_demo() {
    // 如果你在循环中反复尝试 CAS，应该使用 compare_exchange_weak，效率更高；
	// 如果你只打算 CAS 一次，或者不能接受伪失败的干扰，使用 compare_exchange_strong 更合适。
	// 语义完全一致，区别仅在于是否允许伪失败，性能和稳定性有所权衡。


    // compare_exchange_weak - 建议循环重试
    std::atomic<int> x = 0;
    int expected = 0;
    while (!x.compare_exchange_weak(expected, 100)) {
        // retry 直到成功
    }

    // compare_exchange_strong - 一次尝试
    std::atomic<int> x = 0;
    int expected = 0;
    if (x.compare_exchange_strong(expected, 100)) {
        std::cout << "成功设置\n";
    } else {
        std::cout << "失败，当前值是: " << expected << '\n';
    }
}

// ------------------ 排序 sort 自定义 ------------------
struct Person {
    string name;
    int age;
};

bool cmp(const Person &a, const Person &b) {
    return a.age < b.age;  // 按年龄升序
}

void sort_demo() {
    vector<Person> people = {{"Tom", 20}, {"Jerry", 18}, {"Bob", 25}};
    sort(people.begin(), people.end(), cmp);

    for (auto &p : people) cout << p.name << " " << p.age << endl;
}

int main() {
    vector_demo();
    set_demo();
    list_demo();
    heap_demo();
    string_demo();

    return 0;
}