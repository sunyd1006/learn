#ifndef LEETCODE_H
#define LEETCODE_H


#include "bits/stdc++.h"
using namespace std;


// Definition for singly-linked list.
struct ListNode
{
	int val;
	ListNode *next;
	ListNode() : val(0), next(nullptr) {}
	ListNode(int x) : val(x), next(nullptr) {}
	ListNode(int x, ListNode *next) : val(x), next(next) {}

    static ListNode* buildListNode(vector<int>& nums) {
        ListNode* dummy = new ListNode(0, nullptr), *p = dummy;
        for(auto num : nums) {
            p->next = new ListNode(num, nullptr);
            p = p->next;
        }
        return dummy->next;
    }

    static void println(ListNode* head) {
        while(head) {
            cout << head->val << " ";
            head = head->next;
        }
        cout << endl;
    }
};

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
};


inline bool assertValid() {
    return true;
}

#endif	// LEETCODE_H