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
package com.yeying.aimi.mode.bar_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.easemob.chat.EMMessage;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContextMenu extends BaseActivity {

    private int position;
    private long msgTime;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        position = getIntent().getIntExtra("position", -1);
        int txtValue = EMMessage.Type.TXT.ordinal();
        int type = getIntent().getIntExtra("type", -1);
        int[] localData = getIntent().getIntArrayExtra("localData");
        int msgType = getIntent().getIntExtra("msgType", 0);
        int chatType = getIntent().getIntExtra("chatType",0);
        msgTime = getIntent().getLongExtra("msgTime", 0);

        if (type == EMMessage.Type.TXT.ordinal()) {
            boolean isbatai=getIntent().getBooleanExtra("batai_invation",false);
            boolean invation_agree=getIntent().getBooleanExtra("invation_agree",false);
            boolean jietouanyu=getIntent().getBooleanExtra("jietouanyu",false);
            Intent intent=new Intent();
            if (isbatai){
                //发送吧台见邀请
                intent.putExtra("fateCardId",getIntent().getStringExtra("fateCardId"));
                intent.putExtra("barId",getIntent().getStringExtra("barId"));
                setResult(API.REQUEST_CODE_BATAI_INVATION, intent);
                finish();
            }else if (invation_agree){
                //接收吧台见邀请
                intent.putExtra("fateCardId",getIntent().getStringExtra("fateCardId"));
                intent.putExtra("barId",getIntent().getStringExtra("barId"));
                setResult(API.REQUEST_CODE_INVATION_AGREE,intent);
                finish();
            }else if (jietouanyu){
                intent.putExtra("fateCardId",getIntent().getStringExtra("fateCardId"));
                intent.putExtra("barId",getIntent().getStringExtra("barId"));
                setResult(API.REQUEST_CODE_JIETOUANYU,intent);
                finish();
            } else{
                if (msgType == 0) {
                    if (dealTime(msgTime).equals("已过期")) {
                        setContentView(R.layout.context_menu_for_text2);
                    } else {
                        setContentView(R.layout.context_menu_for_text2_backcancle);
                    }
                } else {
                    setContentView(R.layout.context_menu_for_text2_left);
                }
            }
        } else if (type == EMMessage.Type.LOCATION.ordinal()) {
            setContentView(R.layout.context_menu_for_location);
        } else if (type == EMMessage.Type.IMAGE.ordinal()) {
            if (msgType == 0) {
                if (dealTime(msgTime).equals("已过期")) {
                    setContentView(R.layout.context_menu_for_image2);
                } else {
                    setContentView(R.layout.context_menu_for_image2_backcancle);
                }
            } else {
                setContentView(R.layout.context_menu_for_image2_left);
            }
        } /*else if (type == EMMessage.Type.VOICE.ordinal()) {
            if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.voiceType == 0) {
                if (msgType == 0) {
                    if (dealTime(msgTime).equals("已过期")) {
                        setContentView(R.layout.context_menu_for_voice2);
                    } else {
                        setContentView(R.layout.context_menu_for_voice2_backcancle);
                    }
                } else {
                    setContentView(R.layout.context_menu_for_voice2_left);
                }
            } else {
                if (msgType == 0) {
                    if (dealTime(msgTime).equals("已过期")) {
                        setContentView(R.layout.context_menu_for_voice3);
                    } else {
                        setContentView(R.layout.context_menu_for_voice3_backcancle);
                    }
                } else {
                    setContentView(R.layout.context_menu_for_voice3_left);
                }
            }
        } */else if (type == EMMessage.Type.VIDEO.ordinal()) {
            if (msgType == 0) {
                if (dealTime(msgTime).equals("已过期")) {
                    setContentView(R.layout.context_menu_for_video2);
                } else {
                    setContentView(R.layout.context_menu_for_video2_backcancle);
                }
            } else {
                setContentView(R.layout.context_menu_for_video2_left);
            }
        }

        if (localData != null) {
            LinearLayout ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_menu.getLayoutParams();
            params.topMargin = localData[1] - (params.height * 8 / 5);
            if (chatType == 1){//群聊 弹框都在左侧
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = Utils.convertDipOrPx(mActivity, 40);
            }else {//单聊 分左右
                if (msgType == 1){//接收方在左侧
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.leftMargin = Utils.convertDipOrPx(mActivity, 40);
                }
            }
            ll_menu.setLayoutParams(params);
        }

		
		
		/*    
		switch (getIntent().getIntExtra("type", -1)) {
		case txtValue:
			setContentView(R.layout.context_menu_for_text);
			break;
		case EMMessage.Type.LOCATION.ordinal():
			setContentView(R.layout.context_menu_for_location);
			break;
		case EMMessage.Type.IMAGE.ordinal():
			setContentView(R.layout.context_menu_for_image);
			break;
		case EMMessage.Type.VOICE.ordinal():
			setContentView(R.layout.context_menu_for_voice);
			break;
			//need to support netdisk and send netsdk?
		case Message.TYPE_NETDISK:
		    setContentView(R.layout.context_menu_for_netdisk);
		    break;
		case Message.TYPE_SENT_NETDISK:
		    setContentView(R.layout.context_menu_for_sent_netdisk);
		    break;
		default:
			break;
		}
		*/

    }

    public String dealTime(long timeStr) {
        Date msgDate = new Date(timeStr);
        Date date = new Date();
        long between = date.getTime() - timeStr;
        if (between > 120000) {
            return "已过期";
        } else if (between > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("mm分ss秒");
            Date rDate = new Date(120000 - between);
            return "剩余" + sdf.format(rDate);
        } else {
            return "已过期";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void copy(View view) {
        setResult(API.RESULT_CODE_COPY, new Intent().putExtra("position", position));
        finish();
    }

    public void delete(View view) {
        setResult(API.RESULT_CODE_DELETE, new Intent().putExtra("position", position));
        finish();
    }

    public void forward(View view) {
        setResult(API.RESULT_CODE_FORWARD, new Intent().putExtra("position", position));
        finish();
    }

    public void open(View v) {
        setResult(API.RESULT_CODE_OPEN, new Intent().putExtra("position", position));
        finish();
    }

    public void download(View v) {
        setResult(API.RESULT_CODE_DWONLOAD, new Intent().putExtra("position", position));
        finish();
    }

    public void toCloud(View v) {
        setResult(API.RESULT_CODE_TO_CLOUD, new Intent().putExtra("position", position));
        finish();
    }

    public void back_del(View v) {
        setResult(API.RESULT_CODE_BACK_DEL, new Intent().putExtra("position", position));
        finish();
    }

    public void listen_type(View v) {
        setResult(API.RESULT_CODE_LISTEN_TYPE, new Intent().putExtra("position", position));
        finish();
    }

}
