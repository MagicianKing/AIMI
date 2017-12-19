package com.yeying.aimi.utils;

import java.util.List;

/**
 * 提示框帮助类
 */
public class WindowUtil {

    public static boolean isCanShow(List<String> list, String key) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(key)) {
                    list.remove(i);
                    return true;
                }
            }
            list.add(key);
            return false;
        }
        list.add(key);
        return true;
    }

    public static String dealShow(List<String> list) {
        if (list == null) {
            return null;
        }
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;

    }

}
