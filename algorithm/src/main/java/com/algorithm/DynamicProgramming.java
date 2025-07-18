package com.algorithm;

import java.util.*;

/**
 * 动态规划算法实现
 * 适用于LeetCode刷题
 */
public class DynamicProgramming {
    
    /**
     * 爬楼梯
     * LeetCode 70题
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public int climbStairs(int n) {
        if (n <= 2) return n;
        
        int prev1 = 1;
        int prev2 = 2;
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
    
    /**
     * 斐波那契数列
     * LeetCode 509题
     */
    public int fib(int n) {
        if (n <= 1) return n;
        
        int prev1 = 0;
        int prev2 = 1;
        
        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
    
    /**
     * 零钱兑换
     * LeetCode 322题
     * 时间复杂度: O(amount * coins.length)
     * 空间复杂度: O(amount)
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 零钱兑换 II
     * LeetCode 518题
     */
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 最长递增子序列
     * LeetCode 300题
     * 时间复杂度: O(n²)
     * 空间复杂度: O(n)
     */
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) return 0;
        
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        
        int maxLength = 1;
        
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * 最长公共子序列
     * LeetCode 1143题
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 编辑距离
     * LeetCode 72题
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化第一行和第一列
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], 
                                       Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 打家劫舍
     * LeetCode 198题
     */
    public int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int prev1 = nums[0];
        int prev2 = Math.max(nums[0], nums[1]);
        
        for (int i = 2; i < nums.length; i++) {
            int current = Math.max(prev1 + nums[i], prev2);
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
    
    /**
     * 打家劫舍 II
     * LeetCode 213题
     */
    public int rob2(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        if (nums.length == 2) return Math.max(nums[0], nums[1]);
        
        // 不偷第一个房子
        int result1 = robRange(nums, 1, nums.length - 1);
        // 不偷最后一个房子
        int result2 = robRange(nums, 0, nums.length - 2);
        
        return Math.max(result1, result2);
    }
    
    private int robRange(int[] nums, int start, int end) {
        int prev1 = nums[start];
        int prev2 = Math.max(nums[start], nums[start + 1]);
        
        for (int i = start + 2; i <= end; i++) {
            int current = Math.max(prev1 + nums[i], prev2);
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }
    
    /**
     * 买卖股票的最佳时机
     * LeetCode 121题
     */
    public int maxProfit(int[] prices) {
        if (prices.length == 0) return 0;
        
        int minPrice = prices[0];
        int maxProfit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        
        return maxProfit;
    }
    
    /**
     * 买卖股票的最佳时机 II
     * LeetCode 122题
     */
    public int maxProfit2(int[] prices) {
        int maxProfit = 0;
        
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        
        return maxProfit;
    }
    
    /**
     * 不同路径
     * LeetCode 62题
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        
        // 初始化第一行和第一列
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        
        return dp[m - 1][n - 1];
    }
    
    /**
     * 不同路径 II
     * LeetCode 63题
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        
        int[][] dp = new int[m][n];
        
        // 初始化第一行
        for (int j = 0; j < n; j++) {
            if (obstacleGrid[0][j] == 1) break;
            dp[0][j] = 1;
        }
        
        // 初始化第一列
        for (int i = 0; i < m; i++) {
            if (obstacleGrid[i][0] == 1) break;
            dp[i][0] = 1;
        }
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        
        return dp[m - 1][n - 1];
    }
    
    /**
     * 最小路径和
     * LeetCode 64题
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        
        // 初始化第一行
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        
        // 初始化第一列
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        
        return dp[m - 1][n - 1];
    }
    
    // 测试方法
    public static void main(String[] args) {
        DynamicProgramming dp = new DynamicProgramming();
        
        // 测试爬楼梯
        System.out.println("爬楼梯方法数: " + dp.climbStairs(5));
        
        // 测试斐波那契
        System.out.println("斐波那契数列: " + dp.fib(10));
        
        // 测试零钱兑换
        int[] coins = {1, 2, 5};
        System.out.println("零钱兑换: " + dp.coinChange(coins, 11));
        
        // 测试最长递增子序列
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("最长递增子序列: " + dp.lengthOfLIS(nums));
        
        // 测试打家劫舍
        int[] houses = {2, 7, 9, 3, 1};
        System.out.println("打家劫舍: " + dp.rob(houses));
        
        // 测试买卖股票
        int[] prices = {7, 1, 5, 3, 6, 4};
        System.out.println("买卖股票最佳时机: " + dp.maxProfit(prices));
    }
} 