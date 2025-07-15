package com.example.demo.config;

import com.example.demo.entity.Algorithm;
import com.example.demo.repository.AlgorithmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AlgorithmRepository algorithmRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (algorithmRepository.findAll().isEmpty()) {
            // 创建示例算法数据
            createSampleAlgorithms();
        }
    }
    
    private void createSampleAlgorithms() {
        // 快速排序算法
        Algorithm quickSort = new Algorithm();
        quickSort.setName("快速排序");
        quickSort.setCategory("排序算法");
        quickSort.setDescription("快速排序是一种高效的排序算法，使用分治策略。平均时间复杂度为O(nlogn)。");
        quickSort.setCode("function quickSort(arr) {\n" +
                "  if (arr.length <= 1) return arr;\n" +
                "  \n" +
                "  const pivot = arr[Math.floor(arr.length / 2)];\n" +
                "  const left = arr.filter(x => x < pivot);\n" +
                "  const right = arr.filter(x => x > pivot);\n" +
                "  \n" +
                "  return [...quickSort(left), pivot, ...quickSort(right)];\n" +
                "}");
        quickSort.onCreate();
        algorithmRepository.insert(quickSort);
        
        // 二分查找算法
        Algorithm binarySearch = new Algorithm();
        binarySearch.setName("二分查找");
        binarySearch.setCategory("搜索算法");
        binarySearch.setDescription("二分查找是一种在有序数组中查找特定元素的搜索算法，时间复杂度为O(logn)。");
        binarySearch.setCode("function binarySearch(arr, target) {\n" +
                "  let left = 0;\n" +
                "  let right = arr.length - 1;\n" +
                "  \n" +
                "  while (left <= right) {\n" +
                "    const mid = Math.floor((left + right) / 2);\n" +
                "    if (arr[mid] === target) return mid;\n" +
                "    if (arr[mid] < target) left = mid + 1;\n" +
                "    else right = mid - 1;\n" +
                "  }\n" +
                "  return -1;\n" +
                "}");
        binarySearch.onCreate();
        algorithmRepository.insert(binarySearch);
        
        // 冒泡排序算法
        Algorithm bubbleSort = new Algorithm();
        bubbleSort.setName("冒泡排序");
        bubbleSort.setCategory("排序算法");
        bubbleSort.setDescription("冒泡排序是一种简单的排序算法，通过重复遍历要排序的数列，比较相邻元素并交换。");
        bubbleSort.setCode("function bubbleSort(arr) {\n" +
                "  const n = arr.length;\n" +
                "  for (let i = 0; i < n - 1; i++) {\n" +
                "    for (let j = 0; j < n - i - 1; j++) {\n" +
                "      if (arr[j] > arr[j + 1]) {\n" +
                "        [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  return arr;\n" +
                "}");
        bubbleSort.onCreate();
        algorithmRepository.insert(bubbleSort);
        
        System.out.println("示例算法数据初始化完成！");
    }
} 