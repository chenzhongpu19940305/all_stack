package com.algorithm;

import java.util.*;

/**
 * 双指针算法实现
 * 适用于LeetCode刷题
 */
public class TwoPointers {
    
    /**
     * 两数之和 II - 输入有序数组
     * LeetCode 167题
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;
        
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            
            if (sum == target) {
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        
        return new int[]{-1, -1};
    }
    
    /**
     * 盛最多水的容器
     * LeetCode 11题
     */
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            int width = right - left;
            int h = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, width * h);
            
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    /**
     * 三数之和
     * LeetCode 15题
     * 时间复杂度: O(n²)
     * 空间复杂度: O(1)
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 跳过重复元素
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // 跳过重复元素
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 最接近的三数之和
     * LeetCode 16题
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closestSum = nums[0] + nums[1] + nums[2];
        
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (Math.abs(sum - target) < Math.abs(closestSum - target)) {
                    closestSum = sum;
                }
                
                if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return closestSum;
    }
    
    /**
     * 删除有序数组中的重复项
     * LeetCode 26题
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        
        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        
        return slow + 1;
    }
    
    /**
     * 移动零
     * LeetCode 283题
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        
        // 将非零元素移到前面
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        // 将剩余位置填零
        while (slow < nums.length) {
            nums[slow] = 0;
            slow++;
        }
    }
    
    /**
     * 颜色分类
     * LeetCode 75题 - 荷兰国旗问题
     */
    public void sortColors(int[] nums) {
        int left = 0;  // 指向0的右边界
        int right = nums.length - 1;  // 指向2的左边界
        int current = 0;  // 当前指针
        
        while (current <= right) {
            if (nums[current] == 0) {
                // 交换到左边
                swap(nums, left, current);
                left++;
                current++;
            } else if (nums[current] == 2) {
                // 交换到右边
                swap(nums, current, right);
                right--;
            } else {
                // 1保持不变
                current++;
            }
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * 验证回文串
     * LeetCode 125题
     */
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // 跳过非字母数字字符
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    // 测试方法
    public static void main(String[] args) {
        TwoPointers tp = new TwoPointers();
        
        // 测试两数之和
        int[] numbers = {2, 7, 11, 15};
        int[] result = tp.twoSum(numbers, 9);
        System.out.println("两数之和: [" + result[0] + ", " + result[1] + "]");
        
        // 测试盛最多水的容器
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("最大面积: " + tp.maxArea(height));
        
        // 测试删除重复项
        int[] nums = {1, 1, 2, 2, 3, 4, 4, 5};
        int newLength = tp.removeDuplicates(nums);
        System.out.println("删除重复项后长度: " + newLength);
        
        // 测试移动零
        int[] zeros = {0, 1, 0, 3, 12};
        tp.moveZeroes(zeros);
        System.out.println("移动零后: " + Arrays.toString(zeros));
    }
} 