package com.tmp;

import com.ListNode;
import com.TreeNode;
import org.junit.jupiter.api.Test;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

import java.io.*;
import java.util.*;

public class SolutionZJ {
	
	public void printRightNode(TreeNode root) {
		if (root == null) return;
		if (root.left == null && root.right == null) {
			System.out.println(root);
		} else {
			System.out.println(root.val);
			dfs(root);
		}
	}
	
	public void dfs(TreeNode root) {
		if (root == null) {
			return;
		}
		
		if (root.left != null) {
			dfs(root.left);
		}
		
		if (root.right != null) {
			System.out.println(root.right.val);
			dfs(root.right);
		}
	}
	
	@Test
	public void test1() {
		TreeNode root = TreeNode.buildTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
		printRightNode(root);
	}
	
	@Test
	public void test2() {
		// ListNode head = ListNode.buildListNode(new int[]{1, 2, 3, 4, 5});
		ListNode head = ListNode.buildListNode(new int[]{1, 2, 3, 4});
		
		ListNode fake = new ListNode(-1, head);
		ListNode fast = fake, low = fake;
		
		// ListNode low = head, fast = head.next;
		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			low = low.next;
		}
		System.out.println(low.val);
	}
	
	@Test
	public void testPermute() {
		List<List<Integer>> res = permute(new int[]{1, 2, 3});
		for (List<Integer> out : res) {
			for (Integer integer : out) {
				System.out.print(integer + " ");
			}
			System.out.println();
		}
	}
	
	public List<List<Integer>> permute(int[] nums) {
		res = new LinkedList<>();
		laTrackSet(nums, new LinkedHashSet<>());
		//    laTrack(nums, new LinkedList<>());
		return res;
	}
	
	static List<List<Integer>> res;
	
	
	/**
	 * ee: Set<Integer> tmp 怎么就不行了
	 * HashSet/TreeSet：都没有元素都是插入无序的，故而遍历出来有问题，都是123
	 * LinkedHashSet: 保持插入的顺序，故而遍历出来正确
	 */
	public void laTrackSet(int[] nums, Set<Integer> tmp) {
		if (tmp.size() == nums.length) {
			// 注意返回 添加对象引用，而不是新对象引用的错误
			res.add(new LinkedList<>(tmp));
			return;
		}
		
		for (int num : nums) {
			if (tmp.contains(num)) {
				continue;
			}
			tmp.add(num);
			laTrackSet(nums, tmp);
			tmp.remove(num);
		}
	}
	
	
	ListNode reverseLoop(ListNode a, ListNode b) {
		ListNode pre = null, cur = a, next;
		while (cur != b) {
			next = cur.next;
			cur.next = pre;
			pre = cur;
			cur = next;
		}
		return pre;
	}
	
	@Test
	public void testReverseLoop() {
		ListNode head = ListNode.buildListNode(new int[]{1, 2, 3});
		ListNode two = head.next;
		ListNode three = two.next;
		ListNode listNode = reverseLoop(head, two);
		ListNode.printListNode(listNode);
		
		// ListNode listNode2 = reverseLoop(head, three);
		// ListNode.printListNode(listNode2);
	}
	
	@Test
	public void test() {
		int[] ints = {3, 4, -1, 1};
		System.out.println(firstMissingPositive(ints));
		
	}
	
	
	public int firstMissingPositive(int[] nums) {
		int sz = nums.length;
		for (int i = 0; i < sz; i++) {
			// EE: if (), case [-1, 1, 3, 4]
			while (0 < nums[i] && nums[i] <= sz && nums[nums[i] - 1] != nums[i]) {
				swap(nums, i, nums[i] - 1);
			}
		}
		for (int i = 0; i < sz; i++) {
			if (nums[i] != i + 1) { // EE: nums[i] != i + 1
				return i + 1;
			}
		}
		return sz + 1;
	}
	
	private void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}
	
	@Test
	public void testK() {
		System.out.println(removeKdigits("10", 2));
	}
	
	// https://blog.csdn.net/qq_29051413/article/details/108576137
	public int minMeetingRooms(int[][] intervals) {
		if (intervals.length == 0) return 0;
		// 最小堆
		PriorityQueue<Integer> allocator = new PriorityQueue<Integer>(intervals.length, (a, b) -> a - b);
		// 对时间表按照开始时间从小到大排序
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		// 添加第一场会议的结束时间
		allocator.add(intervals[0][1]);
		// 遍历除第一场之外的所有会议
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i][0] >= allocator.peek()) {
				// 如果当前会议的开始时间大于前面已经开始的会议中最晚结束的时间
				// 说明有会议室空闲出来了，可以直接重复利用
				// 当前时间已经是 intervals[i][0]，因此把已经结束的会议删除
				allocator.poll();
			}
			// 把当前会议的结束时间加入最小堆中
			allocator.add(intervals[i][1]);
		}
		// 当所有会议遍历完毕，还在最小堆里面的，说明会议还没结束，此时的数量就是会议室的最少数量
		return allocator.size();
	}
	
	public String removeKdigits(String num, int k) {
		if (num == null || num.length() <= k) return "";
		
		Deque<Integer> stack = new LinkedList<>();
		int remain = num.length() - k;
		for (char item : num.toCharArray()) {
			int digit = item - '0';
			while (!stack.isEmpty() && k > 0 && stack.peekLast() > digit) {
				stack.pollLast();
				k--;
			}
			stack.addLast(digit);
		}
		String res = "";
		boolean isFirst = true;
		while (remain-- > 0) {
			int out = stack.pollFirst();
			if (out == 0 && isFirst) continue;
			isFirst = false;
			res += out;
		}
		return res.equals("") ? "0" : res;
	}
	
	@Test
	public void testStringAdd() {
		System.out.println(addStrings("", "321"));
		System.out.println(addStrings(null, "321"));
		System.out.println(addStrings("123", "321"));
	}
	
	public String addStrings(String num1, String num2) {
		if (num1 == null && num2 == null) return "";
		if (num1 == null) return num2;
		if (num2 == null) return num1;
		
		int i = num1.length() - 1, j = num2.length() - 1;
		int over = 0;
		String res = "";
		while (i >= 0 || j >= 0 || over > 0) {
			int x = i >= 0 ? num1.charAt(i) - '0' : 0;
			int y = j >= 0 ? num2.charAt(j) - '0' : 0;
			int tmp = x + y + over;
			over = tmp / 10;
			res += tmp % 10;
			i--;
			j--;
		}
		return new StringBuilder(res).reverse().toString();
	}
	
	
	@Test
	public void testHeap() {
		int[] ints = sortArray(new int[]{5, 1, 1, 2, 0, 0});
		for (int anInt : ints) {
			System.out.print(anInt + " ");
		}
	}
	
	public int[] sortArray(int[] nums) {
		if (nums == null) return nums;
		heapify(nums);
		for (int i = nums.length - 1; i >= 1; i--) {
			swap(nums, 0, i);
			siftDown(nums, 0, i - 1);
		}
		return nums;
	}
	
	// 将数组整理成堆（堆有序）
	private void heapify(int[] nums) {
		int end = nums.length - 1;
		for (int i = (end - 1) / 2; i >= 0; i--) {
			siftDown(nums, i, end);
		}
	}
	
	/**
	 * @param nums
	 * @param k    当前下沉元素的下标
	 * @param end  [0, end] 是 nums 的有效部分
	 */
	private void siftDown(int[] nums, int k, int end) {
		while (2 * k + 1 <= end) {
			int j = 2 * k + 1;
			if (j + 1 <= end && nums[j + 1] > nums[j]) {
				j++;
			}
			if (nums[j] > nums[k]) {
				swap(nums, j, k);
				k = j;
			} else {
				break;
			}
		}
	}
	
	
	public int minArray(int[] numbers) {
		if (numbers == null || numbers.length == 0) return -1;
		int sz = numbers.length - 1;
		int left = 0, right = sz;
		while (left <= right) {
			int mid = (right - left) / 2 + left;
			if (numbers[mid] > numbers[right]) {
				left = mid + 1;
			} else if (numbers[mid] < numbers[right]) {
				right = mid;
			} else {
				right--;
			}
		}
		return numbers[left];
	}
	
	// dp[i][j] 从 0 点出发走 i 步到 j 点的方案数
	@Test
	public void testBackToOrigin() {
		System.out.println(backToOrigin(2)); // 2
		System.out.println(backToOrigin(10));
	}
	
	public int backToOrigin(int n) {
		int len = 10;
		int[][] dp = new int[n + 1][len];
		
		dp[0][0] = 1;
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j < len; j++) {
				dp[i][j] = dp[i - 1][(j - 1 + len) % len] + dp[i - 1][(j + 1) % len];
			}
		}
		return dp[n][0];
	}
}

