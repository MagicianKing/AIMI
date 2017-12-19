/**
 *
 */
package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author sparrow
 */
public class ImageSaved extends BasicStorage {

    public boolean isSaved;
    private String id;

    /**
     * @param ctx
     */
    public ImageSaved(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    public ImageSaved(Context ctx, String id) {
        super(ctx);
        // TODO Auto-generated constructor stub
        this.id = id;
    }

    public static ImageSaved newInstance(Context ctx, String id) {
        ImageSaved instance = new ImageSaved(ctx, id);
        instance.load();
        return instance;
    }

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.BasicStorage#ser(android.content.SharedPreferences)
     */
    @Override
    public void ser(SharedPreferences sp) {
        // TODO Auto-generated method stub
        sp.edit()
                .putBoolean("isSaved", isSaved)
                .commit();

    }

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.BasicStorage#unSer(android.content.SharedPreferences)
     */
    @Override
    public void unSer(SharedPreferences sp) {
        // TODO Auto-generated method stub
        isSaved = sp.getBoolean("isSaved", false);

    }

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.BasicStorage#del(android.content.SharedPreferences)
     */
    @Override
    public void del(SharedPreferences sp) {
        // TODO Auto-generated method stub
        sp.edit()
                .remove("isSaved")
                .commit();

    }

    /* (non-Javadoc)
     * @see com.palm.commerce.lotteryapplication.storage.BasicStorage#getIdentifer()
     */
    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return this.id;
    }

}
