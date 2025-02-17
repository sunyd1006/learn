#include <bits/stdc++.h>
#include "util.h"
using namespace std;


// input: 1.001.2.3
// return: vector<int>{1, 1, 2, 3}
vector<int> splitString(const std::string& input, char delimiter) {
    vector<int> result;
    stringstream ss(input);
    string token;
    while (getline(ss, token, delimiter)) {
        result.push_back(stoi(token));
    }
    return result;
}

int solution() {
    std::string v1, v2;
    cin >> v1 >> v2;

    // v1 = "1.0";
    // v2 = "1.0.0.1";

    std::vector<int> vec1 = splitString(v1, '.');
    std::vector<int> vec2 = splitString(v2, '.');
    int minSize = std::min(vec1.size(), vec2.size());
    int isSameSize = vec1.size() == vec2.size();
    for (int i = 0; i < minSize; i++) {
        if (vec1[i] + vec2[i] > 0) {
            if (vec1[i] > vec2[i]) {
                return 1;
            } else if (vec1[i] < vec2[i]) {
                return -1;
            }
        }
    }

    if (isSameSize) {
        return 0;
    } else {
        bool isVec1Long = vec1.size() > vec2.size() ? true : false;
        std::vector<int> longVec = isVec1Long ? vec1 : vec2;
        for (int i = minSize; i < longVec.size(); i++) {
            if (longVec[i] > 0) {
                return isVec1Long ? 1 : -1;
            }
        }
        return 0;
    }
}

int main() {
    std::cout << solution();
}



void test() {
    println(splitString("1.001.2.3", '.'));
    println(splitString("1.0.2.3", '.'));
    println(splitString("0.1", '.'));
}
