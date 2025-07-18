package com.algorithm;

/**
 * 二分查找算法实现
 * 适用于LeetCode刷题
 */
public class BinarySearch {
    
    /**
     * 标准二分查找 - 查找目标值
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    public int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // 未找到
    }
    
    /**
     * 查找第一个等于目标值的位置
     * 适用于有重复元素的数组
     */
    public int findFirst(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;
                right = mid - 1; // 继续向左查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找最后一个等于目标值的位置
     */
    public int findLast(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                result = mid;
                left = mid + 1; // 继续向右查找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找插入位置
     * 返回第一个大于等于target的位置
     */
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * 在旋转排序数组中查找目标值
     * 适用于LeetCode 33题
     */
    public int searchInRotatedArray(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断左半部分是否有序
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 查找峰值元素
     * 适用于LeetCode 162题
     */
    public int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    // 测试方法
    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();
        
        int[] nums = {1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println("标准二分查找: " + bs.binarySearch(nums, 7)); // 应该返回3
        
        int[] nums2 = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("查找第一个2: " + bs.findFirst(nums2, 2)); // 应该返回1
        System.out.println("查找最后一个2: " + bs.findLast(nums2, 2)); // 应该返回3
        
        int[] nums3 = {1, 3, 5, 6};
        System.out.println("插入位置: " + bs.searchInsert(nums3, 5)); // 应该返回2
        System.out.println("插入位置: " + bs.searchInsert(nums3, 2)); // 应该返回1
    }
} 