#include <bits/stdc++.h>

#include "util.h"

using namespace std;

class Solution {
public:
    int inventoryManagement(vector<int>& stock) {
        int left = 0, right = stock.size() - 1;
        int res = INT_MAX;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (stock[mid] >= stock[left] ) {
                // left part
                res = min(res, stock[mid]);
                left = mid + 1;
            } else {
                // right part
                res = min(res, stock[mid]);
                right = mid;
            }
        }
        return res;
    }
};


int main() {
    Solution so;
    vector<vector<int>> cases{
        {4,5,8,3,4},
    };

    for (auto& one : cases) {
        std::cout << so.inventoryManagement(one) << std::endl;
    }
}
