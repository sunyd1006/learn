#include <bits/stdc++.h>

#include "leetcode.h"
#include "util.h"

using namespace std;

#include <iostream>
#include <vector>
using namespace std;

void printDuplicatedNum(std::vector<int>& inputs) {
    for (int i = 0; i < inputs.size(); i++) {
        int cur = inputs[i] ;  // normal value or value has been increased
        while (cur >= 10 * 10000) {
          cur -= 10 * 10000;
        }
        // cur = normal value
        inputs[cur] += 10 * 10000;
    }

    for (int i = 0; i < inputs.size(); i++) {
        if (inputs[i] >= 20 * 10000) {
            cout << i << ", " ;
        }
    }
    cout << endl;
}

int main() {
    std::vector<int> inputs = {1,1,2,3,4,5,5};
    printDuplicatedNum(inputs); // 1, 5

    std::vector<int> inputs2 = {2, 2, 3, 3, 4, 5, 5};
    printDuplicatedNum(inputs2);

    std::vector<int> inputs3 = {0, 0, 2, 2, 3, 3, 4, 5, 5};
    printDuplicatedNum(inputs3);
}
