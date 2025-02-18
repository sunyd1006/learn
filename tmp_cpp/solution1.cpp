#include <bits/stdc++.h>
#include "util.h"




#include <climits>
#include <iostream>
#include <string>
#include <vector>
#include <map>
using namespace std;


vector<vector<int> > parseIt(int n) {
    if (n == 1) {
        return vector<vector<int> >(1, vector<int>(1, 1));
    }
    vector<vector<int> > res(n, vector<int>(n, 0));

    int row = 0, col = (n - 1) / 2;
    int currentNum = 0, maxNum = n * n ;

    while (currentNum++ < maxNum) {
        if (currentNum == 1) {
            res[row][col] = currentNum;
            row = n - 1;
            col = col + 1;
            continue;
        } else if (row == 0 && col == n - 1) {
            res[row][col] = currentNum;
            row = row + 1;
            col = col;
            continue;
        } else if (row == 0 && col < n - 1) {
            res[row][col] = currentNum;
            row = n - 1;
            col = col + 1;
            continue;
        } else if (row > 0 && col == n - 1) {
            res[row][col] = currentNum;
            row = row - 1;
            col = 0;
            continue;
        } else {
            res[row][col] = currentNum;

            int oldRow = row, oldCol = col;
            // plan 5
            row = (row - 1 + n ) % n ;
            col = (col + 1 + n ) % n ;

            if (res[row][col] != 0) {
                // plan 6
                row = (oldRow + 1 + n ) % n ;
                col = oldCol;
            }
            continue;
        }
    }
    return res;
}


int main() {
    int n;

    cin >> n;
    // n = 3;
    auto res = parseIt(n);
    for (auto row : res) {
        for (auto num : row) {
            cout << num << std::endl;
        }
    }


}




