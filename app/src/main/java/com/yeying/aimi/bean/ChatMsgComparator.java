package com.yeying.aimi.bean;

import java.util.Comparator;

/**
 * 聊天消息比较器，按theMsgTime (long)从高到低排序
 */
public class ChatMsgComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ChatBean lhs = (ChatBean) o1;
        ChatBean rhs = (ChatBean) o2;
        if (lhs.getTheMsgTime() > rhs.getTheMsgTime()) {
            return -1;
        }
        if (lhs.getTheMsgTime() == rhs.getTheMsgTime()) {
            return 0;
        }
        if (lhs.getTheMsgTime() < rhs.getTheMsgTime()) {
            return 1;
        }
        return 0;

    }


}