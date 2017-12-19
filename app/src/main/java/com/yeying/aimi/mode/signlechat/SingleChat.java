package com.yeying.aimi.mode.signlechat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.ExpressionAdapter;
import com.yeying.aimi.adapter.MessageAdapter;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.FollBean;
import com.yeying.aimi.bean.JsonGiftMsg;
import com.yeying.aimi.bean.JsonUserInfo;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.huanxin.EaseConstant;
import com.yeying.aimi.huanxin.ExpressionPagerAdapter;
import com.yeying.aimi.huanxin.PasteEditText;
import com.yeying.aimi.huanxin.SmileUtils;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.bar_info.ChatActivity;
import com.yeying.aimi.mode.photopicker.AlbmActivity;
import com.yeying.aimi.storage.AttendCache;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.EaseCommonUtils;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.SystemMsgUtil;
import com.yeying.aimi.views.ExpandGridView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.easemob.EMNotifierEvent.Event.EventDeliveryAck;
import static com.easemob.EMNotifierEvent.Event.EventNewCMDMessage;
import static com.easemob.EMNotifierEvent.Event.EventOfflineMessage;
import static com.easemob.EMNotifierEvent.Event.EventReadAck;
import static com.yeying.aimi.utils.SystemMsgUtil.dealTime;

/**
 * Created by tanchengkeji on 2017/9/20.
 */

public class SingleChat extends BaseActivity implements View.OnClickListener, EMEventListener {

    public static int resendPos;

    private ImageView mBackTitle;
    private ImageButton mMenuTitle;
    private TextView mNameTitle;
    private RecyclerView mRecyclerSingle;
    private PasteEditText mContentSingle;
    private ImageButton mIconSingle;
    private ImageButton mMoreSingle;
    private TextView mSendSingle;
    private ViewPager mFaceVPagerSingle;
    private LinearLayout mFaceContainerSingle;
    private LinearLayout mPhotoSingle;
    private LinearLayout mBpSingle;
    private LinearLayout mMoreContainerSingle;
    private LinearLayout mEditSingle;

    private SessionCache mSessionCache;
    private String chatName;
    private String toChatUserId;
    private int chatType;
    private EMConversation conversation;
    private String chatHeadImg;
    //默认加载聊天数量
    private final int pagesize = 20;
    private MessageAdapter mMessageAdapter;
    private InputMethodManager mInputMethodManager;

    private boolean show_more = true;
    private boolean show_emoji = true;
    //表情
    private List<String> emoji = new ArrayList<>();
    //粘贴板
    private ClipboardManager clipboard;
    //删除消息
    private PopupWindow mPopupWindow;
    private View delete_message;
    private LayoutInflater inflater;
    private TextView delete_cancle,delete_yes,delete_msg;
    private String delMsgId;
    private int delIndex;
    //刷新加载
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean haveMoreData = true;

    View.OnTouchListener mOnTouchListener= new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mRecyclerSingle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerSingle.getAdapter().getItemCount()>1){
                        mRecyclerSingle.smoothScrollToPosition(mRecyclerSingle.getAdapter().getItemCount()-1);
                    }
                }
            },100);
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechat);
        mSessionCache = SessionCache.getInstance(this);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initIntent();
        initView();
        dealHuanXin();
        inflater = LayoutInflater.from(this);
        initPop();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void initPop() {
        mPopupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        delete_message = inflater.inflate(R.layout.del_msg_window,null,false);
        mPopupWindow.setFocusable(true);
        initDeleteView(delete_message);
    }

    private void initDeleteView(View delete_baping) {
        delete_msg = (TextView) delete_baping.findViewById(R.id.tv_msg);
        delete_cancle = (TextView) delete_baping.findViewById(R.id.bt_close);
        delete_yes = (TextView) delete_baping.findViewById(R.id.bt_sure);
        delete_msg.setText("是否删除该条消息？");
        delete_cancle.setOnClickListener(this);
        delete_yes.setOnClickListener(this);

    }

    private void initIntent() {
        Intent intent = getIntent();
        chatName = intent.getStringExtra("chatName");
        toChatUserId = intent.getStringExtra("toChatUserId");
        chatType = intent.getIntExtra("chatType",0);
        chatHeadImg = intent.getStringExtra("chatHeadImg");
    }

    private void initView() {
        mBackTitle = (ImageView) findViewById(R.id.title_back);
        mBackTitle.setOnClickListener(this);
        mMenuTitle = (ImageButton) findViewById(R.id.title_menu);
        mMenuTitle.setVisibility(View.GONE);
        mMenuTitle.setOnClickListener(this);
        mNameTitle = (TextView) findViewById(R.id.title_name);
        mRecyclerSingle = (RecyclerView) findViewById(R.id.single_recycler);
        mRecyclerSingle.setLayoutManager(new LinearLayoutManager(SingleChat.this));
        mContentSingle = (PasteEditText) findViewById(R.id.single_content);
        mIconSingle = (ImageButton) findViewById(R.id.single_icon);
        mIconSingle.setOnClickListener(this);
        mIconSingle.setOnTouchListener(mOnTouchListener);
        mMoreSingle = (ImageButton) findViewById(R.id.single_more);
        mMoreSingle.setOnClickListener(this);
        mMoreSingle.setOnTouchListener(mOnTouchListener);
        mSendSingle = (TextView) findViewById(R.id.single_send);
        mSendSingle.setOnClickListener(this);
        mFaceVPagerSingle = (ViewPager) findViewById(R.id.single_face_vPager);
        mFaceContainerSingle = (LinearLayout) findViewById(R.id.single_face_container);
        initEmoji();
        mPhotoSingle = (LinearLayout) findViewById(R.id.single_photo);
        mPhotoSingle.setOnClickListener(this);
        mBpSingle = (LinearLayout) findViewById(R.id.single_bp);
        mBpSingle.setVisibility(View.GONE);
        mMoreContainerSingle = (LinearLayout) findViewById(R.id.single_more_container);
        mEditSingle = (LinearLayout) findViewById(R.id.single_edit);
        //设置名字
        mNameTitle.setText(chatName+"");
        dealEditText(mContentSingle);
        mRecyclerSingle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideAll();
                return false;
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == API.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(mMessageAdapter.getItem(0).getMsgId(), pagesize);
                                } else {
                                    messages = conversation.loadMoreGroupMsgFromDB(mMessageAdapter.getItem(0).getMsgId(), pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }

                            if (messages.size() > 0) {
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageAdapter.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }
                        } else {
                            PromptUtils.showToast(SingleChat.this,getResources().getString(R.string.no_more_messages));
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_menu:
                //TODO
                break;
            case R.id.single_icon:
                //表情
                hideSoftKeyBoard();
                show_more = true;
                if (show_emoji) {
                    mFaceContainerSingle.setVisibility(View.VISIBLE);
                    mMoreContainerSingle.setVisibility(View.GONE);
                    show_emoji = false;
                } else {
                    mFaceContainerSingle.setVisibility(View.GONE);
                    show_emoji = true;
                }
                break;
            case R.id.single_more:
                //相册
                hideSoftKeyBoard();
                show_emoji = true;
                if (show_more) {
                    mMoreContainerSingle.setVisibility(View.VISIBLE);
                    mFaceContainerSingle.setVisibility(View.GONE);
                    show_more = false;
                } else {
                    mMoreContainerSingle.setVisibility(View.GONE);
                    show_more = true;
                }
                break;
            case R.id.single_photo:
                //进入相册
                /*PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .start(SingleChat.this, API.CHAT_SINGLE_PHOTO);*/
                AlbmActivity.toAlbmWithResult(SingleChat.this,API.CHAT_SINGLE_PHOTO,false,false,false);
                break;
            case R.id.single_send:
                //发送聊天信息
                hideAll();
                sendTxtMessage(mContentSingle.getText().toString());
                mContentSingle.setText("");
                //滑动到最底部
                if (mMessageAdapter.getItemCount()>1){
                    mRecyclerSingle.smoothScrollToPosition(mMessageAdapter.getItemCount()-1);
                }
                mMessageAdapter.refreshSelectLast();
                break;
            case R.id.bt_close:
                //取消删除
                if (mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.bt_sure:
                //确定删除
                if (mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
                //删除聊天信息
                conversation.removeMessage(delMsgId);
                mMessageAdapter.refreshSeekTo(delIndex);
                break;
            default:
                break;
        }
    }

    /**
     * 处理环信
     */
    private void dealHuanXin() {
        if (toChatUserId == null) {
            toChatUserId = "";
        }
        onConversationInit();
        onListViewCreation();
    }

    /**
     * 会话
     */
    protected void onConversationInit() {

        if (chatType == API.CHATTYPE_SINGLE) {
            Log.i("toChatUserId", "toChatUserId = " + toChatUserId);
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.Chat);
        } else if (chatType == API.CHATTYPE_GROUP) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.GroupChat);
        } else if (chatType == API.CHATTYPE_CHATROOM) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.ChatRoom);
        }
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == API.CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
    }

    /**
     * 聊天内容
     */
    protected void onListViewCreation() {
        mMessageAdapter = new MessageAdapter(SingleChat.this, toChatUserId, mRecyclerSingle, mSessionCache.headimgUrl, chatHeadImg);
        // 显示消息
        mRecyclerSingle.setAdapter(mMessageAdapter);
        //mMessageAdapter.refreshSelectLast();
        if (mRecyclerSingle.getAdapter().getItemCount()>1){
            mRecyclerSingle.smoothScrollToPosition(mRecyclerSingle.getAdapter().getItemCount()-1);
        }
    }

    /**
     * 输入框
     *
     * @param editText
     */
    private void dealEditText(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerSingle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecyclerSingle.getAdapter().getItemCount()>1){
                            mRecyclerSingle.smoothScrollToPosition(mRecyclerSingle.getAdapter().getItemCount()-1);
                        }
                    }
                },200);
                mMoreContainerSingle.setVisibility(View.GONE);
                mFaceContainerSingle.setVisibility(View.GONE);
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    mSendSingle.setVisibility(View.GONE);
                    mMoreSingle.setVisibility(View.VISIBLE);
                } else {
                    mSendSingle.setVisibility(View.VISIBLE);
                    mMoreSingle.setVisibility(View.GONE);
                }
            }
        });
    }

    private void hideAll(){
        hideSoftKeyBoard();
        mMoreContainerSingle.setVisibility(View.GONE);
        mFaceContainerSingle.setVisibility(View.GONE);
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftKeyBoard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 表情包
     */
    private void initEmoji() {
        emoji = getExpressionRes(35);
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        views.add(gv1);
        mFaceVPagerSingle.setAdapter(new ExpressionPagerAdapter(views));
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "face_" + x;

            reslist.add(filename);

        }
        return reslist;
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = emoji.subList(0, emoji.size());
            list.addAll(list1);
        }
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("com.yeying.aimi.huanxin.SmileUtils");

                        Field field = clz.getField(filename);
                        mContentSingle.append(SmileUtils.getSmiledText(SingleChat.this, field.get(null).toString()));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(mContentSingle.getText())) {

                            int selectionStart = mContentSingle.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = mContentSingle.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        mContentSingle.getEditableText().delete(i, selectionStart);
                                    else
                                        mContentSingle.getEditableText().delete(selectionStart - 1,
                                                selectionStart);
                                } else {
                                    mContentSingle.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: " + e.getMessage());
                }

            }
        });
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == API.CHAT_SINGLE_PHOTO )){
            if (data != null) {
                String photo_front = data.getStringExtra(AlbmActivity.filePath);
                hideAll();
                Log.e(TAG, "onActivityResult: " + photo_front);
                if (!TextUtils.isEmpty(photo_front)){
                    sendPicture(photo_front);
                }else {
                    PromptUtils.showToast(SingleChat.this,"请选择图片");
                }
            }
        }
        if (requestCode == API.REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case API.RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = mMessageAdapter.getItem(data.getIntExtra("position", -1));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
                    Toast.makeText(mActivity, "复制成功", Toast.LENGTH_SHORT).show();
                    break;
                case API.RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = mMessageAdapter.getItem(data.getIntExtra("position", -1));
                    mPopupWindow.setContentView(delete_message);
                    mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
                    delete_msg.setText("是否删除该条消息？");
                    delMsgId = deleteMsg.getMsgId();
                    delIndex = data.getIntExtra("position", mMessageAdapter.getItemCount()) - 1;
                    break;

                case API.RESULT_CODE_FORWARD: // 转发消息
                    EMMessage forwardMsg = mMessageAdapter.getItem(data.getIntExtra("position", 0));
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    intent.putExtra("is_forward",true);
                    startActivity(intent);
                    break;
                case API.RESULT_CODE_BACK_DEL://消息撤回
                    EMMessage backDelMsg = mMessageAdapter.getItem(data.getIntExtra("position", 0));
                    if (dealTime(backDelMsg.getMsgTime()).equals("已过期")) {
                        PromptUtils.showToast(mActivity, "消息超过2分钟无法撤回");
                    } else {
                        // 显示撤回消息操作的 dialog
                        final ProgressDialog pd = new ProgressDialog(mActivity);
                        pd.setMessage("正在撤回消息");
                        pd.show();
                        EaseCommonUtils.sendRevokeMessage(mActivity, backDelMsg, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                pd.dismiss();
                                refreshUI();
                            }

                            @Override
                            public void onError(final int i, final String s) {
                                pd.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (s.equals("maxtime")) {
                                            Toast.makeText(mActivity, "超时无法撤回消息", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(mActivity, "撤回消息失败" + i + " " + s + "", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }

                    break;
                default:
                    break;
            }
        }
        //消息重发
        if (resultCode == RESULT_OK && (requestCode == API.REQUEST_CODE_TEXT || requestCode == API.REQUEST_CODE_VOICE
                || requestCode == API.REQUEST_CODE_PICTURE || requestCode == API.REQUEST_CODE_LOCATION
                || requestCode == API.REQUEST_CODE_VIDEO || requestCode == API.REQUEST_CODE_FILE)){
            resendMessage();
        }
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        msg = conversation.getMessage(resendPos);
        // msg.setBackSend(true);
        msg.status = EMMessage.Status.CREATE;
        mMessageAdapter.refreshSeekTo(resendPos);
    }

    /**
     * 发送文字
     *
     * @param content
     */
    private void sendTxtMessage(String content) {
        if (TextUtils.isEmpty(content)) {
            PromptUtils.showToast(SingleChat.this, "请输入内容");
        } else {
            EMMessage sendMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 设置为群聊
            sendMessage.setChatType(EMMessage.ChatType.Chat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            sendMessage.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            sendMessage.setReceipt(toChatUserId);
            String chatInfo = dealChatInfo();
            //扩展消息
            sendMessage.setAttribute("userInfo", chatInfo);
            sendMessage.setAttribute("transCode", "cc0001");
            sendMessage.setAttribute("em_ignore_notification",false);
            sendMessage.setAttribute("isSystemMsg",false);
            conversation.addMessage(sendMessage);
            mMessageAdapter.refreshSelectLast();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        if (filePath != null){
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
            message.setChatType(EMMessage.ChatType.Chat);
            message.setReceipt(toChatUserId);
            ImageMessageBody body = new ImageMessageBody(new File(filePath));
            // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
            body.setSendOriginalImage(true);
            message.addBody(body);
            String chatInfo = dealChatInfo();
            //消息扩展
            message.setAttribute("userInfo", chatInfo);
            message.setAttribute("transCode", "cc0001");
            conversation.addMessage(message);
            mRecyclerSingle.setAdapter(mMessageAdapter);
            mMessageAdapter.refreshSelectLast();
        }
    }

    /**
     * 用户聊天信息
     *
     * @return
     */
    private String dealChatInfo() {
        JsonUserInfo jsonUserInfo = new JsonUserInfo();
        jsonUserInfo.setUserId(mSessionCache.userId);
        jsonUserInfo.setUserName(mSessionCache.nickname);
        jsonUserInfo.setHeadImg(mSessionCache.headimgUrl);
        jsonUserInfo.setUserId2(toChatUserId);
        jsonUserInfo.setUserName2(chatName);
        jsonUserInfo.setHeadImg2(chatHeadImg);
        return JSON.toJSONString(jsonUserInfo);
    }

    public RecyclerView getListView() {
        return mRecyclerSingle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EventOfflineMessage,
                EventDeliveryAck, EventReadAck, EventNewCMDMessage});
        EMChat.getInstance().setAppInited();
    }

    public static void toSingleChat(Context context, String chatName, String toChatUserId, int chatType, String chatHeadImg){
        Intent intent = new Intent(context,SingleChat.class);
        intent.putExtra("chatName",chatName);
        intent.putExtra("toChatUserId",toChatUserId);
        intent.putExtra("chatType",chatType);
        intent.putExtra("chatHeadImg",chatHeadImg);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMChatManager.getInstance().unregisterEventListener(this);
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        switch (emNotifierEvent.getEvent()) {
            case EventNewCMDMessage: {// 接收透传新消息
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                String action = cmdMsgBody.action;//获取自定义action
                if (action.equals(EaseConstant.EASE_ATTR_REVOKE)) {
                    //撤回消息
                    try {
                        String msgId = message.getStringAttribute(EaseConstant.EASE_ATTR_REVOKE_MSG_ID);
                        String converUser = message.getFrom();
                        if (chatType == API.CHATTYPE_SINGLE) {
                            converUser = toChatUserId;
                            Log.i("", "聊天室消息撤回 converUser = " + converUser);
                        }
                        EMConversation conversation = EMChatManager.getInstance().getConversation(converUser);
                        EMMessage revokeMsg = conversation.getMessage(msgId);
                        String userInfo = revokeMsg.getStringAttribute("userInfo", "");
                        JSONObject userObject = JSONObject.parseObject(userInfo);
                        TextMessageBody body = new TextMessageBody("对方" + " 撤回了一条消息");
                        revokeMsg.addBody(body);
                        // 这里需要把消息类型改为 TXT 类型
                        revokeMsg.setType(EMMessage.Type.TXT);
                        // 设置扩展为撤回消息类型，是为了区分消息的显示
                        revokeMsg.setAttribute(EaseConstant.EASE_ATTR_REVOKE, true);
                        // 返回修改消息结果
                        EMChatManager.getInstance().updateMessageBody(revokeMsg);
                        refreshUI();
                        if (HomeActivity.activityInstance != null) {
                            HomeActivity.activityInstance.toRefresh();
                        }
                    } catch (EaseMobException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            }
            case EventNewMessage: {//接受新消息
                SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(mActivity);
                //获取到message
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                Log.e("chat", "onEvent: " + message.toString());
                //刷新ListView 将新发送的消息更新出来
                mMessageAdapter.refreshSelectLast();
                String username = null;
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    //群组消息
                    username = message.getTo();
                } else {
                    //单聊消息
                    username = message.getFrom();
                }
                if (HomeActivity.activityInstance != null) {
                    HomeActivity.activityInstance.toRefresh();
                }
                //如果是系统消息的扩展
                boolean isSystemMsg = message.getBooleanAttribute("isSystemMsg", false);
                if (isSystemMsg) {
                    Log.i("", "是系统消息");
                    EMConversation commentCon = EMChatManager.getInstance().getConversationByType(username, EMConversation.EMConversationType.Chat);
                    try {
                        String transCode = message.getStringAttribute("transCode", "");

                        if (transCode.equals("send10231")) {
                            //我的动态通知
                            String para = message.getStringAttribute("para");
                            JSONObject paraObject = JSONObject.parseObject(para);
                            String pUserId = paraObject.getString("pUserId");    //评论人
                            String pName = paraObject.getString("pName");    //名称
                            String pImg = paraObject.getString("pImg");        //头像
                            int type = paraObject.getIntValue("type");        //类型
                            String content = paraObject.getString("content");    //内容
                            String id = paraObject.getString("id");            //动态id
                            Date pTime = paraObject.getDate("pTime");        //时间
                            String pic_img = paraObject.getString("pic_img");    //动态图片
                            int pic_type = paraObject.getIntValue("pic_type");    //动态类型

                            ReplyCacheBean bean = new ReplyCacheBean();
                            SessionCache session = SessionCache.getInstance(mActivity);
                            bean.setMyUserId(session.userId);
                            bean.setUserId(pUserId);
                            bean.setName(pName);
                            bean.setImg(pImg);
                            bean.setType(type);
                            bean.setContent(content);
                            bean.setId(id);
                            bean.setTime(pTime);
                            bean.setPic_img(pic_img);
                            bean.setPic_type(pic_type);
                            bean.setMsgId(message.getMsgId());

                            if (bean.getType() == 1 || bean.getType() == 2) {
                                ReplyCache cache = ReplyCache.getCommentCache(mActivity);
                                boolean isHaveData = false;
                                for (ReplyCacheBean rbean : cache.getReplyList()) {
                                    if (rbean.getMsgId() != null && rbean.getMsgId().equals(bean.getMsgId())) {
                                        isHaveData = true;
                                    }
                                }
                                if (!isHaveData) {
                                    cache.getReplyList().add(bean);
                                    cache.save();
                                }

                                Log.i("", "动态有新消息，放入缓存");
                                simpleCache.isNewMy = true;
                                simpleCache.save();
                            }

                            //设为已读
                            commentCon.markMessageAsRead(message.getMsgId());
                            //清除消息
                            commentCon.removeMessage(message.getMsgId());
                        }else if (transCode.equals("cc0001")) {
                            SystemMsgUtil.dealSystemMessage(message, mActivity);
                        } else if (transCode.equals("cc0006")) {
                        } else {
                            simpleCache.isNewMsg = true;
                            simpleCache.save();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("", "聊天中的系统消息transCode解析失败！");
                    }
//        		}
                }

                //transCode扩展消息
                try {
                    String transCode = message.getStringAttribute("transCode");
                    if (transCode.equals("cc0003") && username.equals(toChatUserId)) {
                        String content = message.getStringAttribute("para");
                        JsonGiftMsg ext = JSON.parseObject(content, JsonGiftMsg.class);
                        if (ext.getMsg_type() == 1) {
                            conversation.removeMessage(message.getMsgId());
                            return;
                        } else {
                            simpleCache.isNewMsg = true;
                            simpleCache.save();
                        }
                    }

                    boolean isMyFriend = true;//判断是否走招呼   请到nearbyActivity里面的isMyFriend也变成true
                    AttendCache cache = AttendCache.getAttendCache(mActivity);
                    List<FollBean> frlist = cache.getFoll();
                    //遍历关注缓存
                    for (int j = 0; j < frlist.size(); j++) {
                        FollBean abean = frlist.get(j);
                        if (username == null || abean.getUserId() == null) {
                            continue;
                        }
                    }
                    boolean isInMianDaRao = SystemMsgUtil.isInMianDaRao(message.getFrom(), mActivity);
                    if (isInMianDaRao && isMyFriend && transCode.equals("cc0001") && !isSystemMsg && message.getChatType() == EMMessage.ChatType.Chat) {
                        simpleCache.isNewMsg = true;
                        simpleCache.save();
                    }

                } catch (Exception e) {
                }
                if (username.equals(toChatUserId) && message.getChatType() == EMMessage.ChatType.Chat) {
                    //声音和震动提示有新消息
                    refreshUIWithNewMessage();
                    //HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
                } else {
                    refreshUIWithNewMessage();
                    //如果消息不是和当前聊天ID的消息
                    if (message.getChatType() == EMMessage.ChatType.Chat) {
                        //HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                    }
                }
                break;
            }
            case EventDeliveryAck: {//接收已发送回执
                break;
            }
            case EventReadAck: {//接收已读回执
                break;
            }
            case EventOfflineMessage: {//接收离线消息
                break;
            }
            default:
                break;
        }
    }

    /**
     * 适配器刷新
     */
    private void refreshUI() {
        if (mMessageAdapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                if (mMessageAdapter != null) {
                    mMessageAdapter.refresh();
                }
            }
        });
    }
    private void refreshUIWithNewMessage() {
        if (mMessageAdapter == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                Log.e("ksp", "9");
                mMessageAdapter.refreshSelectLast();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
