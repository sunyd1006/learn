#include <bits/stdc++.h>

#include <climits>
#include <iostream>
#include <map>
#include <sstream>
#include <string>
#include <vector>

#include "util.h"
using namespace std;

class Solution {
public:
    int maxProfit(vector<int>& prices) {
        int n = prices.size();
        vector<vector<int> > dp(n, vector<int>(2, 0));
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                dp[i][0] = 0;
                // dp[i][0] = max(dp[i-1][0], dp[i-1][1]+prices[i]);
                // dp[i][1] = max(dp[i-1][1], dp[i-1][0]-prices[i]);
                dp[i][1] = -prices[i];
                continue;
            }
            if (i == 1) {
                dp[i][0] = 0;
                dp[i][1] = std::max(prices[i];
                continue;
            }
            dp[i][0] = std::max(dp[i - 1][0], dp[i - 1][1] + prices[i]);

            // dp[i - 1][0] = std::max(dp[i - 2[0], dp[i - 2][1] + prices[i-1]);
            // 但是由于右侧 dp[i - 2][1] + prices[i-1] 是 i-1 的卖行为, 所以算dp[i-1][1]不应该考虑这种情况，否则dp[i][1]的交易不会发生
            // 所以 dp[i - 1][0] 只能= dp[i - 2][0]
            dp[i][1] = std::max(dp[i - 1][1], dp[i - 2][0] - prices[i]);
        }
        return dp[n - 1][0];
    }
};

int main() {
    Solution so;
    string input = "1,2,3,0,2";
    auto res = splitString(input, ',');
    std::cout << so.maxProfit(res);
}
