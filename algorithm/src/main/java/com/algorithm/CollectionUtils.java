package collection_utils;

import java.util.*;

/**
 * 数组与集合常用操作工具类
 */
public class CollectionUtils {
    /**
     * 数组反转
     */
    public static void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * 数组求和
     */
    public static int sumArray(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     * 集合去重
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    /**
     * 判断集合是否包含某元素
     */
    public static <T> boolean containsElement(Collection<T> collection, T element) {
        return collection.contains(element);
    }

    /**
     * 合并两个数组
     */
    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    /**
     * 集合转数组
     */
    public static <T> T[] collectionToArray(Collection<T> collection, T[] a) {
        return collection.toArray(a);
    }

    /**
     * 数组转集合
     */
    public static <T> List<T> arrayToList(T[] array) {
        return Arrays.asList(array);
    }
} 