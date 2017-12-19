package com.yeying.aimi.storage;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Cache implements IStorage {

    //public int type;
    // max alive time 10 mins.
    // set the cache max alive time is 5mins
    private static final long CACHE_ALIVE = 1000 * 60 * 60 * 24;
    public String identifer;
    public String content;
    public long timestamp;


    /**
     * save the cache
     *
     * @param identifer
     * @param content
     */
    public Cache(String identifer, String content) {
        super();
        //	this.type=type;
        this.identifer = identifer;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }


    /**
     * load the cache
     *
     * @param identifer
     */
    public Cache(String identifer) {
        super();
        //this.type=type;
        this.identifer = identifer;
    }


    /**
     * checking if the cache valid
     *
     * @return
     */
    public boolean isValid() {
        boolean ret = System.currentTimeMillis() - this.timestamp < CACHE_ALIVE;
        ret = this.timestamp == 0 ? false : ret;

        return ret;
    }

    @Override
    public void del(SharedPreferences sp) {
        // TODO Auto-generated method stub
        Editor editor = sp.edit();
        //editor.remove("type");
        editor.remove("content");
        editor.remove("timestamp");
        editor.commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return this.identifer;
    }

    @Override
    public void ser(SharedPreferences sp) {
        // TODO Auto-generated method stub
        Editor editor = sp.edit();
        //editor.putInt("type", type);
        editor.putString("content", content);
        editor.putLong("timestamp", timestamp);
        editor.commit();

    }

    @Override
    public void unSer(SharedPreferences sp) {
        // TODO Auto-generated method stub
        //sp.getInt("", defValue)
        //type=sp.getInt("type", -1);
        content = sp.getString("content", null);
        timestamp = sp.getLong("timestamp", 0L);
    }


}
