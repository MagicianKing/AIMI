package com.yeying.aimi.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yeying.aimi.R;
import com.yeying.aimi.huanxin.EaseConstant;

import java.util.List;

public class EaseCommonUtils {
    private static final String TAG = "CommonUtils";



    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }





    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取栈顶的activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }

    /**
     * 发送一条撤回消息的透传，这里需要和接收方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param message  需要撤回的消息
     * @param callBack 发送消息的回调，通知调用方发送撤回消息的结果
     */
    public static void sendRevokeMessage(final Context context, final EMMessage message, final EMCallBack callBack) {
        if (message.status != EMMessage.Status.SUCCESS) {
            callBack.onError(0, "sending");
            return;
        }
        // 获取当前时间，用来判断后边撤回消息的时间点是否合法，这个判断不需要在接收方做，
        // 因为如果接收方之前不在线，很久之后才收到消息，将导致撤回失败
        long currTime = System.currentTimeMillis();
        long msgTime = message.getMsgTime();
        if (currTime < msgTime || (currTime - msgTime) > 120000) {
            callBack.onError(1, "maxtime");
            return;
        }
        String msgId = message.getMsgId();
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
            cmdMessage.setChatType(EMMessage.ChatType.ChatRoom);
        }
        cmdMessage.setReceipt(message.getTo());
        // 创建CMD 消息的消息体 并设置 action 为 revoke
        CmdMessageBody body = new CmdMessageBody(EaseConstant.EASE_ATTR_REVOKE);
        cmdMessage.addBody(body);
        cmdMessage.setAttribute(EaseConstant.EASE_ATTR_REVOKE_MSG_ID, msgId);
        // 确认无误，开始发送撤回消息的透传
        EMChatManager.getInstance().sendMessage(cmdMessage, new EMCallBack() {
            @Override
            public void onSuccess() {
                // 更改要撤销的消息的内容，替换为消息已经撤销的提示内容
                TextMessageBody body = new TextMessageBody("您撤回了一条消息");
                message.addBody(body);
                // 这里需要把消息类型改为 TXT 类型
                message.setType(EMMessage.Type.TXT);
                // 设置扩展为撤回消息类型，是为了区分消息的显示
                message.setAttribute(EaseConstant.EASE_ATTR_REVOKE, true);
                // 返回修改消息结果
                EMChatManager.getInstance().updateMessageBody(message);
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 收到撤回消息，这里需要和发送方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param revokeMsg 收到的透传消息，包含需要撤回的消息的 msgId
     * @return 返回撤回结果是否成功
     */
    public static boolean receiveRevokeMessage(Context context, EMMessage revokeMsg) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(revokeMsg.getFrom());
        boolean result = false;
        // 从cmd扩展中获取要撤回消息的id
        String msgId = revokeMsg.getStringAttribute(EaseConstant.EASE_ATTR_REVOKE_MSG_ID, null);
        if (msgId == null) {
            return result;
        }
        // 根据得到的msgId 去本地查找这条消息，如果本地已经没有这条消息了，就不用撤回
        // 这里为了防止消息没有加载到内存中，使用Conversation的loadMessage方法加载消息
        EMMessage message = EMChatManager.getInstance().getMessage(msgId);
        if (message == null) {
            message = conversation.loadMessage(msgId);
        }
        // 更改要撤销的消息的内容，替换为消息已经撤销的提示内容
        TextMessageBody body = new TextMessageBody(String.format(context.getString(R.string.revoke_message_by_user), message.getFrom()));
        message.addBody(body);
        // 这里需要把消息类型改为 TXT 类型
        message.setType(EMMessage.Type.TXT);
        // 设置扩展为撤回消息类型，是为了区分消息的显示
        message.setAttribute(EaseConstant.EASE_ATTR_REVOKE, true);
        // 返回修改消息结果
        result = EMChatManager.getInstance().updateMessageBody(message);
        // 因为Android这边没有修改消息未读数的方法，这里只能通过conversation的getMessage方法来实现未读数减一
        conversation.getMessage(msgId);
        message.isAcked = true;
        return result;
    }






}
