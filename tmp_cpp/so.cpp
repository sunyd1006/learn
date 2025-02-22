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
    // vector  10yi，long
    // 1. 内存大小, 1b * 4Bytes =40 0000 0000 = 4GB
    // int num = 10 * 10000 * 10000;
    // vector<int> nums(num, 0);

    // 2. 4GB  can be in stack

    // 3. 4GB/4k = 1024M 能运行吗

    // 32 bit CPU 能 4GB 存吗 => false
    // 2^32 = 4294967296

    // 1. nums.reserve()
    // 2. 压缩：10@100,


    // float ieee754

    // 0.xxx, 0.yyy    =>     c.zzz
    // 1. bigFloat     =>  bigFloat


    // std::string s = "123.456";
    // auto it = s.find('.');
    // if (it != std::string::npos) {
    //     auto idx = it - s.begin();
    // }
    // std::cout << s.find('.') << std::endl;


    std::string str = "hello....world";
    std::string str2 = "hello....world";
    char ch = '.';
    std::remove(str2.begin(), str2.end(), ch);
    assert(str2 == "helloworldorld");

    str.erase(std::remove(str.begin(), str.end(), ch), str.end());
    assert(str == "helloworld");

    return 0;
}
