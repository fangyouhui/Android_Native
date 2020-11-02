package com.pai8.ke.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 字符串操作工具类
 * Created by gh on 2020/11/2.
 */
public class StringUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private StringUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 空显示默认
     *
     * @param str
     * @return
     */
    public static String isEmptyDefaultValue(String str, String strDefaultValue) {
        return isEmpty(str) ? strDefaultValue : StringUtils.strSafe(str);
    }

    /**
     * 去掉空格
     *
     * @param str
     * @return
     */
    public static String strTrim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 防止空指针
     *
     * @param str
     * @return
     */
    public static String strSafe(String str) {
        return (str == null || str.equalsIgnoreCase("null")) ? "" : str;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
    }

    /**
     * 判断字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        return TextUtils.equals(StringUtils.strSafe(str1), StringUtils.strSafe(str2));
    }

    /**
     * 获取文本框的值
     *
     * @param et
     * @return
     */
    public static String getEditText(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 获取文本框的值
     *
     * @param tv
     * @return
     */
    public static String getTextView(TextView tv) {
        return tv.getText().toString().trim();
    }

    /**
     * 截取前几位
     *
     * @param str      截取的字符串
     * @param endIndex 末位
     * @return
     */
    public static String subString(String str, int endIndex) {
        return str.substring(0, endIndex);
    }

    /**
     * 截取从第几位到第几位
     *
     * @param str        截取的字符串
     * @param startIndex 起始位
     * @param endIndex   末尾
     * @return
     */
    public static String subString(String str, int startIndex, int endIndex) {
        return str.substring(startIndex, endIndex);
    }

    /**
     * 截取字符串
     *
     * @param strSearch    待搜索的字符串
     * @param strStart     起始字符串 例如：<title>
     * @param strEnd       结束字符串 例如：</title>
     * @param defaultValue
     * @return
     */
    public static String substring(String strSearch, String strStart, String strEnd, String defaultValue) {
        int start_len = strStart.length();
        int start_pos = isEmpty(strStart) ? 0 : strSearch.indexOf(strStart);
        if (start_pos > -1) {
            int end_pos = isEmpty(strEnd) ? -1 : strSearch.indexOf(strEnd, start_pos + start_len);
            if (end_pos > -1)
                return strSearch.substring(start_pos + strStart.length(), end_pos);
            else
                return strSearch.substring(start_pos + strStart.length());
        }
        return defaultValue;
    }

    /**
     * 截取字符串
     *
     * @param search 待搜索的字符串
     * @param start  起始字符串 例如：<title>
     * @param end    结束字符串 例如：</title>
     * @return
     */
    public static String substring(String search, String start, String end) {
        return substring(search, start, end, "");
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return
     */
    public static String strConcat(String... strs) {
        StringBuffer result = new StringBuffer();
        if (strs != null) {
            for (String str : strs) {
                if (str != null)
                    result.append(str);
            }
        }
        return result.toString();
    }

    /**
     * 集合转数组
     *
     * @param lists     List集合
     * @param separator 分隔符
     * @return
     */
    public static String strJoin(final ArrayList<String> lists, final String separator) {
        StringBuffer result = new StringBuffer();
        if (lists != null && !lists.isEmpty()) {
            for (String str : lists) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    public static String strJoinInteger(final ArrayList<Integer> lists, final String separator) {
        StringBuffer result = new StringBuffer();
        if (lists != null && !lists.isEmpty()) {
            for (Integer str : lists) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 集合转数组
     *
     * @param iter      Iterator集合
     * @param separator 分隔符
     * @return
     */
    public static String strJoin(final Iterator<String> iter, final String separator) {
        StringBuffer result = new StringBuffer();
        if (iter != null) {
            while (iter.hasNext()) {
                String key = iter.next();
                result.append(key);
                result.append(separator);
            }
            if (result.length() > 0)
                result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 得到大括号之间的内容
     *
     * @param str
     * @return
     */
    public static String getBrackets(String str) {
        int a = str.indexOf("{");
        int c = str.indexOf("}");
        if (a >= 0 && c >= 0 & c > a) {
            return (str.substring(a + 1, c));
        } else {
            return str;
        }
    }

    /**
     * 替换字符串中的值
     *
     * @param str
     * @param strContains 需替换的值
     * @param strReplace  替换的值
     * @return
     */
    public static String getReplace(String str, String strContains, String strReplace) {
        if (isNotEmpty(str) && str.contains(strContains)) {
            return str.replaceAll(strContains, strReplace);
        } else {
            return "";
        }
    }

    public static ArrayList<String> strToList(String str) {
        ArrayList<String> a = new ArrayList<>();
        if (str == null) {
            return a;
        }
        String[] strArray = str.split(",");
        for (String s : strArray) {
            a.add(s);
        }
        return a;
    }

    public static ArrayList<String> strToList(String str, String format) {
        ArrayList<String> a = new ArrayList<>();
        if (str == null) {
            return a;
        }
        String[] strArray = str.split(format);
        for (String s : strArray) {
            a.add(s);
        }
        return a;
    }

    public static String intJoin(final List<Integer> lists, final String separator) {
        StringBuffer result = new StringBuffer();
        if (lists != null && !lists.isEmpty()) {
            for (Integer str : lists) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }
}
