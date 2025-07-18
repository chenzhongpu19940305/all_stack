package com.algorithm;

import java.util.*;

/**
 * 深度优先搜索算法实现
 * 适用于LeetCode刷题
 */
public class DFS {
    
    /**
     * 岛屿数量
     * LeetCode 200题
     * 时间复杂度: O(m * n)
     * 空间复杂度: O(m * n) 最坏情况下递归栈深度
     */
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private void dfs(char[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        
        // 边界检查
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == '0') {
            return;
        }
        
        // 标记为已访问
        grid[i][j] = '0';
        
        // 四个方向DFS
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
    
    /**
     * 全排列
     * LeetCode 46题
     * 时间复杂度: O(n!)
     * 空间复杂度: O(n)
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                used[i] = true;
                path.add(nums[i]);
                backtrack(nums, used, path, result);
                path.remove(path.size() - 1);
                used[i] = false;
            }
        }
    }
    
    /**
     * 子集
     * LeetCode 78题
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackSubsets(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrackSubsets(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
    
    /**
     * 组合总和
     * LeetCode 39题
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrackCombination(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackCombination(int[] candidates, int target, int start, 
                                    List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > target) break;
            
            path.add(candidates[i]);
            backtrackCombination(candidates, target - candidates[i], i, path, result);
            path.remove(path.size() - 1);
        }
    }
    
    /**
     * 单词搜索
     * LeetCode 79题
     */
    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0) return false;
        
        int m = board.length;
        int n = board[0].length;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfsWord(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean dfsWord(char[][] board, String word, int i, int j, int index) {
        if (index == word.length()) return true;
        
        int m = board.length;
        int n = board[0].length;
        
        if (i < 0 || i >= m || j < 0 || j >= n || 
            board[i][j] != word.charAt(index)) {
            return false;
        }
        
        char temp = board[i][j];
        board[i][j] = '#'; // 标记为已访问
        
        boolean found = dfsWord(board, word, i + 1, j, index + 1) ||
                       dfsWord(board, word, i - 1, j, index + 1) ||
                       dfsWord(board, word, i, j + 1, index + 1) ||
                       dfsWord(board, word, i, j - 1, index + 1);
        
        board[i][j] = temp; // 恢复
        return found;
    }
    
    /**
     * 二叉树的最大深度
     * LeetCode 104题
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 路径总和
     * LeetCode 112题
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        
        if (root.left == null && root.right == null) {
            return targetSum == root.val;
        }
        
        return hasPathSum(root.left, targetSum - root.val) ||
               hasPathSum(root.right, targetSum - root.val);
    }
    
    /**
     * 路径总和 II
     * LeetCode 113题
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        dfsPathSum(root, targetSum, new ArrayList<>(), result);
        return result;
    }
    
    private void dfsPathSum(TreeNode root, int targetSum, List<Integer> path, List<List<Integer>> result) {
        if (root == null) return;
        
        path.add(root.val);
        
        if (root.left == null && root.right == null && targetSum == root.val) {
            result.add(new ArrayList<>(path));
        } else {
            dfsPathSum(root.left, targetSum - root.val, path, result);
            dfsPathSum(root.right, targetSum - root.val, path, result);
        }
        
        path.remove(path.size() - 1);
    }
    
    /**
     * 克隆图
     * LeetCode 133题
     */
    public Node cloneGraph(Node node) {
        if (node == null) return null;
        
        Map<Node, Node> visited = new HashMap<>();
        return dfsClone(node, visited);
    }
    
    private Node dfsClone(Node node, Map<Node, Node> visited) {
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        
        Node cloneNode = new Node(node.val);
        visited.put(node, cloneNode);
        
        for (Node neighbor : node.neighbors) {
            cloneNode.neighbors.add(dfsClone(neighbor, visited));
        }
        
        return cloneNode;
    }
    
    /**
     * 二叉树的中序遍历
     * LeetCode 94题
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfsInorder(root, result);
        return result;
    }
    
    private void dfsInorder(TreeNode root, List<Integer> result) {
        if (root == null) return;
        
        dfsInorder(root.left, result);
        result.add(root.val);
        dfsInorder(root.right, result);
    }
    
    // 测试方法
    public static void main(String[] args) {
        DFS dfs = new DFS();
        
        // 测试全排列
        int[] nums = {1, 2, 3};
        List<List<Integer>> permutations = dfs.permute(nums);
        System.out.println("全排列: " + permutations);
        
        // 测试子集
        List<List<Integer>> subsets = dfs.subsets(nums);
        System.out.println("子集: " + subsets);
        
        // 测试组合总和
        int[] candidates = {2, 3, 6, 7};
        List<List<Integer>> combinations = dfs.combinationSum(candidates, 7);
        System.out.println("组合总和: " + combinations);
    }
}

// 辅助类定义
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
} 