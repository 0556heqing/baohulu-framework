package com.baohulu.framework.common.utils;

import java.util.*;

/**
 * @author heqing
 * @date 2022/11/02 09:57
 */
public class ListUtils {

    /**
     * 数组转list
     *
     * @param array 数组
     * @return
     */
    public static <T> List<T> arrayToList(T[] array) {
        ArrayList<T> list = new ArrayList<T>(array.length);
        Collections.addAll(list, array);
        return list;
    }

    /**
     * list转数组
     *
     * @param list
     * @return
     */
    public static <T> T[] listToArray(List<T> list) {
        return (T[])list.toArray();
    }

    /**
     * list转set
     *
     * @param list
     * @return
     */
    public static <T> Set<T> listToSet(List<T> list) {
        Set<T> set = new HashSet<T>();
        set.addAll(list);
        return set;
    }

    /**
     * set转List
     *
     * @param set
     * @return
     */
    public static <T> List<T> setToList(Set<T> set) {
       return new ArrayList<T>(set);
    }

    /**
     * 求ls对ls2的差集,即ls中有，但ls2中没有的
     *
     * @param ls
     * @param ls2
     * @return
     */
    public static <T> List<T> diff(List<T> ls, List<T> ls2) {
        if (ls == null || ls2 == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList(ls.size());
        result.addAll(ls);
        result.removeAll(ls2);
        return result;
    }

    /**
     * 求2个集合的交集
     *
     * @param ls
     * @param ls2
     * @return
     */
    public static <T> List<T> intersect(List<T> ls, List<T> ls2) {
        if (ls == null || ls2 == null) {
            return new ArrayList<>();
        }
        List result = new ArrayList(ls.size());
        result.addAll(ls);
        result.retainAll(ls2);
        return result;
    }

    /**
     * 求2个集合的并集
     *
     * @param ls
     * @param ls2
     * @return
     */
    public static <T> List<T> union(List<T> ls, List<T> ls2) {
        if (ls == null || ls2 == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList(ls.size());
        result.addAll(ls);
        result.removeAll(ls2);
        result.addAll(ls2);
        return result;
    }
}
