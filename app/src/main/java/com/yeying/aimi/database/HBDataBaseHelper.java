package com.yeying.aimi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tanchengkeji on 2017/9/28.
 */

public class HBDataBaseHelper  {

    private static final String DB_NAME = "aimi.db";
    private static final int DB_VERSION = 1;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL("create table hi8_barrage_info(id integer primary key autoincrement,barrageId varchar UNIQUE,userId not null,bar_id not null,userName,imgUrl,content,type," +
              //      "ext_id,stus integer not null,create_time,ext_info,target_user_id,target_user_name,target_img_url);");

            db.execSQL("create table aimi_red_pack(id integer primary key autoincrement,red_pack_id varchar UNIQUE,userId not null,barId,remark,user_name,imgUrl,type integer not null," +
                    "limit_sex,stus integer not null,create_time,status integer not null,times integer not null,redpack_choose integer not null,invalid_time);");

            //db.execSQL("create table miw_group_hb(id integer primary key autoincrement,red_pack_id varchar UNIQUE,userId not null,remark,user_name,imgUrl,type integer not null," +
              //      "limit_sex,create_time,status integer not null,redpack_choose integer not null,invalid_time,bar_id);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("drop table if exists hi8_barrage_info");
            db.execSQL("drop table if exists aimi_red_pack");
            onCreate(db);
        }
    }
}
