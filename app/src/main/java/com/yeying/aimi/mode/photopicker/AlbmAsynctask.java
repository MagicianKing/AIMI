package com.yeying.aimi.mode.photopicker;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by king.
 * on 2017/9/25 22:19
 * King大人!
 */

public class AlbmAsynctask extends AsyncTask<String, String, Map<String, List<String>>> {
    private Context mContext;
    private ProgressDialog mDialog;
    private Map<String, List<String>> mMap;

    public AlbmAsynctask(Context context) {
        mContext = context;
        mMap = new HashMap<>();
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
        mDialog = ProgressDialog.show (mContext, "", "正在加载");
    }

    @Override
    protected void onPostExecute (Map<String, List<String>> s) {
        super.onPostExecute (s);
        mDialog.dismiss ();
        //处理返回数据
        mOnLoadFinishListener.loadFinish (s);

    }


    @Override
    protected Map<String, List<String>> doInBackground (String... strings) {
        ContentResolver contentResolver = mContext.getContentResolver ();
        Cursor cursor = contentResolver.query (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext ()) {
            //获取路径
            String path = cursor.getString (cursor.getColumnIndex (MediaStore.Images.Media.DATA));
            //获取父文件名
            String parentName = new File(path).getParentFile ().getName ();

            //根据父路径名,归类
            if (!mMap.containsKey (parentName)) {
                List<String> list = new ArrayList<>();
                list.add (path);
                mMap.put (parentName, list);
            } else {
                mMap.get (parentName).add (path);
            }

        }
        cursor.close ();
        return mMap;
    }


    private onLoadFinishListener mOnLoadFinishListener;
    public interface onLoadFinishListener{
        void loadFinish(Map<String, List<String>> map);
    }
    public void setOnLoadFinishListner(onLoadFinishListener onLoadFinishListner){
        mOnLoadFinishListener = onLoadFinishListner;
    }
}
