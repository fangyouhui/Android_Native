package com.pai8.ke.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合操作工具类
 * Created by gh on 2020/11/2.
 */
public class CollectionUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private CollectionUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 判断集合是否为空
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection c) {
        return (c == null || c.isEmpty());
    }

    /**
     * 判断集合是否不为空
     *
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection c) {
        return !isEmpty(c);
    }

    /**
     * 两个集合并集
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 两个集合交集
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<>();
        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * 去除两个集合相同的部分
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<>(a);
        for (T element : b) {
            list.remove(element);
        }
        return list;
    }

    public static List<Integer> getNewList(List<Integer> values, int from, int to) {
        List<Integer> newList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            newList.add(values.get(i));
        }
        return newList;
    }

}

