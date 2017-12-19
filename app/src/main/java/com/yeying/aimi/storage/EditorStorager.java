/**
 *
 */
package com.yeying.aimi.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author sparrow
 */
public class EditorStorager {

    Context mContext;

    private EditorStorager(Context mContext) {
        this.mContext = mContext;
    }

    public static EditorStorager getInstance(Context ctx) {
        return new EditorStorager(ctx);
    }

    public void clear() {

    }

    @SuppressLint("InlinedApi")
    public void loadStorage(IStorage obj) {
        if (obj != null && obj.getIdentifer() != null && mContext != null) {
            SharedPreferences sp = mContext.getSharedPreferences(obj.getIdentifer(),
                    Context.MODE_MULTI_PROCESS);

            obj.unSer(sp);
        }
    }

    public void updateStorage(IStorage obj) {
        if (obj != null && obj.getIdentifer() != null && mContext != null) {
            SharedPreferences sp = mContext.getSharedPreferences(obj.getIdentifer(),
                    Context.MODE_MULTI_PROCESS);
            obj.ser(sp);
        }
    }

    public void deleteStroage(IStorage obj) {
        if (obj != null && obj.getIdentifer() != null) {
            SharedPreferences sp = mContext.getSharedPreferences(obj.getIdentifer(),
                    Context.MODE_MULTI_PROCESS);

            obj.del(sp);
        }
    }


}
