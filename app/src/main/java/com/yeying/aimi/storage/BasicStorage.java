/**
 *
 */
package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author sparrow
 */
public abstract class BasicStorage implements IStorage {

    Context mCtx;

    /**
     *
     */
    public BasicStorage(Context ctx) {
        // TODO Auto-generated constructor stub
        mCtx = ctx;
    }

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.IStorage#ser(android.content.SharedPreferences)
     */
    @Override
    public abstract void ser(SharedPreferences sp);

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.IStorage#unSer(android.content.SharedPreferences)
     */
    @Override
    public abstract void unSer(SharedPreferences sp);

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.IStorage#del(android.content.SharedPreferences)
     */
    @Override
    public abstract void del(SharedPreferences sp);

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.IStorage#getIdentifer()
     */
    @Override
    public abstract String getIdentifer();

    public void load() {
        EditorStorager.getInstance(mCtx).loadStorage(this);

    }

    public void save() {
        EditorStorager.getInstance(mCtx).updateStorage(this);
    }

    public void del() {
        EditorStorager.getInstance(mCtx).deleteStroage(this);
    }

}
