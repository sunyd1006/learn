#include <bits/stdc++.h>
#include "util.h"




#include <climits>
#include <iostream>
#include <string>
#include <map>
using namespace std;

class StockPrice {
    map<int, int> store;

public:
    StockPrice() {}

    void update(int timestamp, int price) { store[timestamp] = price; }

    int current() { return store.rbegin()->second; }

    int maximum() {
        int max = INT_MIN;
        for (const auto& kv : store) {
            if (kv.second > max) {
                max = kv.second;
            }
        }
        return max;
    }

    int minimum() {
        int min = INT_MAX;
        for (const auto& kv : store) {
            if (kv.second < min) {
                min = kv.second;
            }
        }
        return min;
    }
};


int main() {
    int n;
    cin >> n;
    auto stock = new StockPrice();
    while (n-- > 0) {  // 注意 while 处理多个 case
        std::string func;
        cin >> func;
        if (func == "update") {
            int timestamp, price;
            cin >> timestamp >> price;
            stock->update(timestamp, price);
            cout << "null" << std::endl;
        } else if (func == "current") {
            cout << stock->current() << std::endl;
        } else if (func == "maximum") {
            cout << stock->maximum() << std::endl;
        } else if (func == "minimum") {
            cout << stock->minimum() << std::endl;
        }
    }

    delete stock;
}




