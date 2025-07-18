package com.algorithm.fenzu;

import java.util.*;

/**
 * 返回所有的分组方式
 */
public class GroupPartitions {
 
    public static void main(String[] args) {
        // 示例：处理列表 [1, 2, 3]
        List<Integer> input = Arrays.asList(1,  2, 3);
        List<List<List<Integer>>> allPartitions = partition(input);
        
        // 打印所有分组结果 
        System.out.println(" 输入列表: " + input);
        System.out.println(" 分组数量: " + allPartitions.size()  + "（Bell数）");
        System.out.println(" 所有分组方式:");
        for (int i = 0; i < allPartitions.size();  i++) {
            System.out.println((i  + 1) + ". " + allPartitions.get(i)); 
        }
    }
 
    /**
     * 主函数：生成列表所有分组方式 
     * @param list 输入列表 
     * @return 所有可能的分组结果（List of List of List结构）
     */
    public static List<List<List<Integer>>> partition(List<Integer> list) {
        List<List<List<Integer>>> result = new ArrayList<>();
        // 处理空列表特殊情况 
        if (list == null || list.isEmpty())  {
            result.add(new  ArrayList<>());
            return result;
        }
        // 递归生成分组 
        backtrack(list, 0, new ArrayList<>(), result);
        return result;
    }
 
    /**
     * 递归回溯生成分组 
     * @param list 原始列表 
     * @param index 当前处理元素索引 
     * @param current 当前分组状态 
     * @param result 存储所有分组结果 
     */
    private static void backtrack(List<Integer> list, int index, 
                                  List<List<Integer>> current, 
                                  List<List<List<Integer>>> result) {
        // 终止条件：处理完所有元素 
        if (index == list.size())  {
            result.add(deepCopy(current)); 
            return;
        }
 
        int element = list.get(index); 
        boolean addedToExisting = false;
        
        // 选择1：将元素加入现有分组 
        for (List<Integer> group : current) {
            // 确保分组内元素有序（避免重复）
            if (group.isEmpty()  || element > group.get(group.size()  - 1)) {
                group.add(element); 
                backtrack(list, index + 1, current, result);
                group.remove(group.size()  - 1); // 回溯 
                addedToExisting = true;
            }
        }
 
        // 选择2：创建新分组 
        List<Integer> newGroup = new ArrayList<>();
        newGroup.add(element); 
        current.add(newGroup); 
        backtrack(list, index + 1, current, result);
        current.remove(current.size()  - 1); // 回溯 
    }
 
    /**
     * 深拷贝分组结果（避免回溯修改）
     * @param original 原始分组 
     * @return 深度复制后的分组 
     */
    private static List<List<Integer>> deepCopy(List<List<Integer>> original) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> group : original) {
            copy.add(new  ArrayList<>(group));
        }
        // 分组间排序（按首元素升序）
        copy.sort((a,  b) -> a.get(0)  - b.get(0)); 
        return copy;
    }
}