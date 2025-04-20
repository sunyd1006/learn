#include <bits/stdc++.h>
#include "leetcode.h"
#include "util.h"

using namespace std;


// xxxx.xxx     yyy.xxx
//

// class RecycleNumber {
//     string unRecyclePart;
//     string recyclePart;
//     int recyclePrecision;
// };



// struct BigFloat {
//     std::string val;  // "xxx.yyy"
// public:
//     BigFloat(const std::string& val) : val(val) {}

//     static BigFloat multiply(const BigFloat& a, const BigFloat& b) {
//         int totalBotNum = findIdx(a.val, '.') + findIdx(b.val, '.');
//         string tmpl = removeDot(a.val, '.');
//         string tmpr = removeDot(b.val, '.');

//         string top = tmpl.size() > tmpr.size()? tmpl : tmpr;
//         string bottom = tmpl.size() > tmpr.size()? tmpr : tmpl;
//         string res = "0";
//         for (int i = bottom.size() - 1, suffixZeros = 0; i >= 0; --i) {
//             addIntegerStr(res, multipleOneChar(top, bottom[i], suffixZeros));
//             suffixZeros++;
//         }
//         // put dot into valid position
//         res.insert(res.begin() + totalBotNum, '.');
//         reverse(res.begin(), res.end());
//         return BigFloat(res)  ;
//     }

//     // 123 * 5 = 615
//     // 123 * 5, suffix = 1 => 6150
//     static string multipleOneChar(const std::string& input, char one, int suffixZeros) {
//         string res = "";
//         int value, overflow = 0;
//         for (int i = input.size() - 1; i >= 0 ; --i) {
//             // remove leading zeros
//             value = ((input[i] - '0') * (one - '0') + overflow) / 10;
//             overflow = ((input[i] - '0') * (one - '0') + overflow) % 10;
//             res = res + std::to_string(value);
//         }
//         if (overflow > 0) {
//             res = res + std::to_string(overflow);
//         }
//         reverse(res.begin(), res.end());
//         return res;
//     }

//     static string addIntegerStr(const std::string& tmpl, const std::string& tmpr) {
//         string res = "";
//         int value, overflow = 0;
//         int i = tmpl.size() - 1, j = tmpr.size() - 1;
//         for (; i >= 0 && j >= 0; --i, --j) {
//             // remove leading zeros
//             value = ((tmpl[i] - '0') * (tmpr[j] - '0') + overflow) / 10;
//             overflow = ((tmpl[i] - '0') * (tmpr[j] - '0') + overflow) % 10;
//             res = res + std::to_string(value);
//         }
//         auto addUnusedPartFunc = [&](const std::string& input, int start) {
//             for (int k = start; k >=0; --k) {
//                 value = (input[i] - '0') + overflow;
//                 overflow = (input[i] - '0' ) % 10;
//                 res = res + std::to_string(overflow);
//             }
//         };
//         addUnusedPartFunc(tmpl, i);
//         addUnusedPartFunc(tmpl, j);
//         reverse(res.begin(), res.end());
//         return res;
//     }

//     static int findIdx(const std::string& str, char c) {
//         return 0;
//     }

//     static string removeDot(std::string str, char c) {
//         return "";
//     }
// };


int main() {



    return 0;
}
