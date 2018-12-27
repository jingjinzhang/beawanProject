package com.beawan.OcrJavaTest;

import java.util.*;

public class Util {

    /**
     * 找出最大值
     *
     * @param array
     * @return
     */
    public static int findMaxValue(int[] array) {
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (temp <= array[i]) {
                temp = array[i];
            }
        }
        return temp;
    }

    /**
     * 找出最小值
     *
     * @param array
     * @return
     */
    public static int findMinValue(int[] array) {
        int temp = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (temp >= array[i]) {
                temp = array[i];
            }
        }
        return temp;
    }

    /**
     * 找出最多的值
     *
     * @param array
     * @return
     */
    public static int findMostNumber(int[] array) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < array.length; i++) {
            if (map.containsKey(array[i])) {
                map.put(array[i], map.get(array[i]) + 1);
            } else {
                map.put(array[i], 1);
            }
        }

        int count = -1;
        int number = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > count || (entry.getValue() == count && entry.getKey() > number)) {
                count = entry.getValue();
                number = entry.getKey();
            }
        }
        return number;
    }

    /**
     * 关键词模糊匹配
     *
     * @param keyword 关键词
     */
    public static boolean compareKeyword(String keyword, String word) {

        char[] a = keyword.toCharArray();
        char[] b = word.toCharArray();
        int distance = edit_distance(b,a);
        if(distance < (b.length/2 + 5)){
            return true;
        }
        return false;
    }

    private static int edit_distance(char[] a, char[] b) {
        int lena = a.length;
        int lenb = b.length;
        int[] d = new int[lenb + 1];
        int i, j, old, temp;

        for (j = 0; j <= lenb; j++) {
            d[j] = j;
        }
        for (i = 1; i <= lena; i++) {
            old = i - 1;
            d[0] = i;
            for (j = 1; j <= lenb; j++) {
                temp = d[j];
                if (a[i - 1] == b[j - 1]) {
                    d[j] = old;
                } else {
                    d[j] = minOfThree(d[j] + 1, d[j - 1] + 1, old + 1);
                }
                old = temp;
            }
        }

        return d[lenb];
    }

    private static int minOfThree(int a, int b, int c){
        int min;
        if (a < b) {
            min = a;
        } else {
            min = b;
        }

        if (min > c) {
            min = c;
        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(compareKeyword("(5).按欠款方归集的期末余额前五名的其他应收款情况:","按欠款方归集的期末余额前五名的应收账款"));

    }
}
