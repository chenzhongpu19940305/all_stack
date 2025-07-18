package com.algorithm;

import java.util.*;

/**
 * 滑动窗口算法实现
 * 适用于LeetCode刷题
 */
public class SlidingWindow {
    
    /**
     * 无重复字符的最长子串
     * LeetCode 3题
     * 时间复杂度: O(n)
     * 空间复杂度: O(min(m, n)) m是字符集大小
     */
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int left = 0;
        int maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            // 如果字符已存在，移动左指针
            while (set.contains(c)) {
                set.remove(s.charAt(left));
                left++;
            }
            
            set.add(c);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 最小覆盖子串
     * LeetCode 76题
     * 时间复杂度: O(n)
     * 空间复杂度: O(k) k是字符集大小
     */
    public String minWindow(String s, String t) {
        if (s.length() == 0 || t.length() == 0) return "";
        
        // 统计目标字符串的字符频率
        Map<Character, Integer> targetCount = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetCount.put(c, targetCount.getOrDefault(c, 0) + 1);
        }
        
        int required = targetCount.size();
        int formed = 0;
        int left = 0;
        int minLength = Integer.MAX_VALUE;
        int minLeft = 0;
        
        Map<Character, Integer> windowCount = new HashMap<>();
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowCount.put(c, windowCount.getOrDefault(c, 0) + 1);
            
            if (targetCount.containsKey(c) && 
                windowCount.get(c).intValue() == targetCount.get(c).intValue()) {
                formed++;
            }
            
            // 尝试收缩窗口
            while (left <= right && formed == required) {
                c = s.charAt(left);
                
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    minLeft = left;
                }
                
                windowCount.put(c, windowCount.get(c) - 1);
                if (targetCount.containsKey(c) && 
                    windowCount.get(c).intValue() < targetCount.get(c).intValue()) {
                    formed--;
                }
                
                left++;
            }
        }
        
        return minLength == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLength);
    }
    
    /**
     * 字符串的排列
     * LeetCode 567题
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;
        
        int[] count = new int[26];
        int[] window = new int[26];
        
        // 统计s1的字符频率
        for (char c : s1.toCharArray()) {
            count[c - 'a']++;
        }
        
        // 初始化窗口
        for (int i = 0; i < s1.length(); i++) {
            window[s2.charAt(i) - 'a']++;
        }
        
        // 检查第一个窗口
        if (Arrays.equals(count, window)) return true;
        
        // 滑动窗口
        for (int i = s1.length(); i < s2.length(); i++) {
            window[s2.charAt(i) - 'a']++;
            window[s2.charAt(i - s1.length()) - 'a']--;
            
            if (Arrays.equals(count, window)) return true;
        }
        
        return false;
    }
    
    /**
     * 找到字符串中所有字母异位词
     * LeetCode 438题
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;
        
        int[] count = new int[26];
        int[] window = new int[26];
        
        // 统计p的字符频率
        for (char c : p.toCharArray()) {
            count[c - 'a']++;
        }
        
        // 初始化窗口
        for (int i = 0; i < p.length(); i++) {
            window[s.charAt(i) - 'a']++;
        }
        
        // 检查第一个窗口
        if (Arrays.equals(count, window)) {
            result.add(0);
        }
        
        // 滑动窗口
        for (int i = p.length(); i < s.length(); i++) {
            window[s.charAt(i) - 'a']++;
            window[s.charAt(i - p.length()) - 'a']--;
            
            if (Arrays.equals(count, window)) {
                result.add(i - p.length() + 1);
            }
        }
        
        return result;
    }
    
    /**
     * 最大连续1的个数 III
     * LeetCode 1004题
     */
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int zeros = 0;
        int maxLength = 0;
        
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeros++;
            }
            
            // 如果0的个数超过k，收缩窗口
            while (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 水果成篮
     * LeetCode 904题
     */
    public int totalFruit(int[] fruits) {
        Map<Integer, Integer> basket = new HashMap<>();
        int left = 0;
        int maxFruits = 0;
        
        for (int right = 0; right < fruits.length; right++) {
            basket.put(fruits[right], basket.getOrDefault(fruits[right], 0) + 1);
            
            // 如果篮子里的水果种类超过2，收缩窗口
            while (basket.size() > 2) {
                basket.put(fruits[left], basket.get(fruits[left]) - 1);
                if (basket.get(fruits[left]) == 0) {
                    basket.remove(fruits[left]);
                }
                left++;
            }
            
            maxFruits = Math.max(maxFruits, right - left + 1);
        }
        
        return maxFruits;
    }
    
    /**
     * 长度最小的子数组
     * LeetCode 209题
     */
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int sum = 0;
        int minLength = Integer.MAX_VALUE;
        
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            
            // 当和大于等于目标值时，尝试收缩窗口
            while (sum >= target) {
                minLength = Math.min(minLength, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        
        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }
    
    /**
     * 滑动窗口最大值
     * LeetCode 239题
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];
        
        Deque<Integer> deque = new LinkedList<>();
        int[] result = new int[nums.length - k + 1];
        
        for (int i = 0; i < nums.length; i++) {
            // 移除窗口外的元素
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 移除小于当前元素的元素
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            // 记录窗口最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        SlidingWindow sw = new SlidingWindow();
        
        // 测试无重复字符的最长子串
        System.out.println("最长无重复子串: " + sw.lengthOfLongestSubstring("abcabcbb"));
        
        // 测试最小覆盖子串
        System.out.println("最小覆盖子串: " + sw.minWindow("ADOBECODEBANC", "ABC"));
        
        // 测试字符串的排列
        System.out.println("字符串排列: " + sw.checkInclusion("ab", "eidbaooo"));
        
        // 测试长度最小的子数组
        int[] nums = {2, 3, 1, 2, 4, 3};
        System.out.println("最小子数组长度: " + sw.minSubArrayLen(7, nums));
        
        // 测试滑动窗口最大值
        int[] nums2 = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] result = sw.maxSlidingWindow(nums2, 3);
        System.out.println("滑动窗口最大值: " + Arrays.toString(result));
    }
} 