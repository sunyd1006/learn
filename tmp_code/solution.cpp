#include <bits/stdc++.h>

#include "util.h"

using namespace std;

// class Solution {
// public:
//     bool hasSpecialSubstring(string s, int k) {
//       map<char,int> windowCountMap;
//       int left = 0, right = 0;
//       while (right < s.size()) {
//         char currChar = s[right];
//         right++;
//         windowCountMap[currChar]++;

//         if (right - left == k) {
//           if ( (windowCountMap.size() == 1)
//                && (left  == 0 || s[left - 1] != currChar)
//                && (right == s.size() || s[right] != currChar)) {
//             return true;
//           }
//           char leftChar = s[left];
//           left++;

//           windowCountMap[leftChar]--;
//           if (windowCountMap[leftChar] == 0) {
//             windowCountMap.erase(leftChar);
//           }
//         }
//       }
//       return false;
//     }
// };

// void pro1() {
//     Solution so;
//     vector<std::tuple<string, int, bool>> cases{
//         {"aaabaaa", 3, true},
//         {"aaabbcde", 2, true},
//         {"aaabcccc", 4, true},

//         {"abc", 2, false},
//         {"abbb", 2, false},
//         {"bbb", 2, false},
//         {"abbbc", 2, false},
//     };

//     for (const auto& one : cases) {
//         cout << "expected: " << std::get<2>(one) << " " <<
//         so.hasSpecialSubstring(std::get<0>(one), std::get<1>(one)) << std::endl;
//     }
// }

class Solution {
public:
    long long maxWeight(vector<int>& pizzas) {
        std::sort(pizzas.begin(), pizzas.end());
        //   println(pizzas);

        int days = pizzas.size() / 4;
        long long res = 0;

        int jiDays = 0, outDays = 0;
        if (days % 2 == 1) {
            jiDays = days - jiDays;
            outDays = days / 2;
        } else {
            jiDays = outDays = days / 2;
        }
        for (int i = pizzas.size() - jiDays; i < pizzas.size(); i++) {
            res += pizzas[i];
        }

        int hasOne = 1, start = pizzas.size() - jiDays - 1;
        for (int alreadyOuDays = 0; alreadyOuDays < outDays;) {
            if (hasOne % 2 == 0) {
                res += pizzas[start];
                alreadyOuDays++;
            }
            start--;
            hasOne++;
        }
        return res;
    }
};

int main() {
    Solution so;
    vector<vector<int>> cases{
        {1,2,3,4,5,6,7,8},
        {2,1,1,1,1,1,1,1},
        {2, 5, 2, 3},
    };

    for (auto& one : cases) {
        std::cout << so.maxWeight(one) << std::endl;
    }
}
