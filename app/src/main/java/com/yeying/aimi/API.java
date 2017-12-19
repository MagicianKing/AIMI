package com.yeying.aimi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanchengkeji on 2017/9/12.
 */

public class API {
    public static final String APP_QQ_ID = "1106437509";
    public static final String APP_QQ_KEY = "GuOTLXHddqm5tTNh";
    public static final String APP_WEIBO_ID = "616079365";
    public static final String APP_WEIBO_SECRET = "e2d1ff1a413007e52ec1d8215efa7ace";
    public static final String APP_WEIBO_URL="https://api.weibo.com/oauth2/default.html";
    //三方登录用户信息
    public static final String USER_THIRD_TYPE = "user_third_type";
    public static final String USER_OPEN_ID = "user_open_id";
    public static final String USER_NICK_NAME = "user_nick_name";
    public static final String USER_HEAD_URL = "user_head_imgurl";
    public static final String USER_GENDER = "user_gender";
    //存储用户手机号
    public static final String USER_INFO = "user_info";
    public static final String USER_ACCOUNT = "user_account";
    public static final String USER_PASSWORD = "user_password";
    //微信ID
    public static final String APP_ID = "wx64832da15ed5c69c";
    public static final String APP_SECRET = "2ce5099051f4f9ac7b827150e6b88c2c";
    public static final String APP_SCOPE = "snsapi_userinfo";
    //app是否首次进入
    public static final String IS_FIRST="is_first";
    public static final String TO_CHAT_USERID="to_chat_userid";
    public static final String CHAT_TYPE="chat_type";
    public static final String BAR_ID="bar_id";
    public static final String BAR_NAME="bar_name";
    public static final int CHAT_GROUP_PHOTO=80;
    public static final int DONGTAI_PHOTO=81;
    public static final int BAPING_PHOTO=82;
    public static final int CHAT_SINGLE_PHOTO=83;
    // 撤回消息的 key
    public static final String EASE_ATTR_REVOKE = "REVOKE_FLAG";
    // 因好友关系而发送失败消息的 key
    public static final String EASE_ATTR_FRIEND_FAIL = "em_friend_fail";
    // 因被加入黑名单而发送失败消息的 key
    public static final String EASE_ATTR_BLACK_FAIL = "em_black_fail";

    //吧台见 请求码
    private boolean is_send_invation=false;
    public static final int REQUEST_CODE_BATAI_INVATION=66;
    //同意吧台见邀请
    public static final int REQUEST_CODE_INVATION_AGREE=67;
    private boolean is_invation_agree=false;
    public static final int REQUEST_CODE_JIETOUANYU=68;
    public static final int LOGIN_REQUEST = 69;

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;
    public static final int RESULT_CODE_BACK_DEL = 8;
    public static final int RESULT_CODE_LISTEN_TYPE = 9;


    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    private static final int REQUEST_IMAGE = 233;
    private static final int SEND_BAP = 234;//收费霸屏
    private static final int TYPE = 1;
    private static final int FREEBP = 225;//免费霸屏
    public static final int BP = 999;

    /**
     * 加工timeStr
     */
    public static String getDateStr(Date date5) {
        String str = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date date = null;
        Date date1 = date5;
        String str1 = sdf.format(date1);
        try {
            date = sdf.parse(str1);
            now = sdf.parse(sdf.format(now));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long sl = date.getTime();
        long el = now.getTime();
        long ei = sl - el;
        int value = (int) (ei / (1000 * 60 * 60 * 24));
        if (value == 0) {
            //一天内的显示时间
            str = sdf3.format(date1);
        } else if (value >= -365) {
            str = sdf2.format(date1);
        } else{
            str = sdf1.format(date1);
        }
        return str;
    }

    /**
     * 加工timeStr
     */
    public static String getDateStrSimple(Date date5) {
        String str = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date date = null;
        Date date1 = date5;
        String str1 = sdf.format(date1);
        try {
            date = sdf.parse(str1);
            now = sdf.parse(sdf.format(now));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long sl = date.getTime();
        long el = now.getTime();
        long ei = sl - el;
        int value = (int) (ei / (1000 * 60 * 60 * 24));
        if (value == 0) {
            //一天内的显示时间
            str = sdf2.format(date1);
        } else if (value >= -365) {
            str = sdf2.format(date1);
        } else{
            str = sdf1.format(date1);
        }
        return str;
    }

}
