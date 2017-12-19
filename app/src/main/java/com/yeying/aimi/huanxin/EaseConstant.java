/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yeying.aimi.huanxin;

public class EaseConstant {

    // EaseUI 中使用SharedPreference 保存信息的文件名
    public static final String EASEUI_SHARED_NAME = "ease_shared";

    /**
     *  设置自己扩展的消息类型的 key
     */
    // 通用的 msg_id key
    public static final String EASE_ATTR_MSG_ID = "em_msg_id";
    // 需要撤销的消息的 msg_id key
    public static final String EASE_ATTR_REVOKE_MSG_ID = "msgId";
    // 撤回消息的 key
    public static final String EASE_ATTR_REVOKE = "REVOKE_FLAG";
    // 因好友关系而发送失败消息的 key
    public static final String EASE_ATTR_FRIEND_FAIL = "em_friend_fail";
    // 因被加入黑名单而发送失败消息的 key
    public static final String EASE_ATTR_BLACK_FAIL = "em_black_fail";
    // 阅后即焚的 key
    public static final String EASE_ATTR_READFIRE = "em_readFire";

    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
//    /**
//     * app自带的动态表情，直接去resource里获取图片
//     */
//    public static final String MESSAGE_ATTR_BIG_EXPRESSION_ICON = "em_big_expression_icon";
//    /**
//     * 动态下载的表情图片，需要知道表情url
//     */
//    public static final String MESSAGE_ATTR_BIG_EXPRESSION_URL = "em_big_expression_url";

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
}
