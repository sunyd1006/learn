#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

class Solution {
public:
    int numSubmatrixSumTarget(std::vector<std::vector<int>>& matrix, int target) {
        int n = matrix.size(), m = matrix[0].size();
        std::vector<std::vector<int>> sum(n + 1, std::vector<int>(m + 1));
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                sum[i][j] =
                    sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1] + matrix[i - 1][j - 1];
            }
        }
        int ans = 0;
        for (int top = 1; top <= n; top++) {
            for (int bot = top; bot <= n; bot++) {
                int cur = 0;
                std::unordered_map<int, int> map;
                for (int r = 1; r <= m; r++) {
                    cur = sum[bot][r] - sum[top - 1][r];
                    if (cur <= target) ans++;
                    if (map.count(cur - target)) ans += map[cur - target];
                    map[cur]++;
                }
            }
        }
        return ans;
    }
};

int main() {
    int N, M, k;
    ifstream fileIn("./leetcode/numSubmatrixSumTarget.txt");
    // ifstream fileIn("./numSubmatrixSumTarget.txt");
    string firstLine;
    std::getline(fileIn, firstLine);
    istringstream in_tmp(firstLine);
    in_tmp >> N >> M >> k;
    vector<vector<int>> martix;
    while (N--) {
        string line_tmp;
        std::getline(fileIn, line_tmp);
        istringstream row(line_tmp);
        int m = M;
        vector<int> line;
        while (m--) {
            int col;
            row >> col;
            line.push_back(col);
        }
        martix.push_back(line);
    }

    int ret = Solution().numSubmatrixSumTarget(martix, k);
    cout << ret << " ";
    return 0;
};

/*

3 3 0
0 1 0
1 1 1
0 1 0


*/