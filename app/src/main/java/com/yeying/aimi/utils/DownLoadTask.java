package com.yeying.aimi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/11/8 上午10:41
 */

public class DownLoadTask extends AsyncTask<String, String, String> {
    private DownLoadListener mDownLoadListener;
    private HttpURLConnection mHttpURLConnection;
    private File mDirectory;
    private File mFile;
    private FileOutputStream mFileOutputStream;
    private Context mContext;

    public DownLoadTask(Context context) {
        //缓存目录
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            PromptUtils.showToast((Activity) context, "存储空间不足");
            return;
        }
        mDirectory = Environment.getExternalStorageDirectory();
        //mAbsolutePath = mDirectory.getAbsolutePath();
        int i = PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (i == PermissionChecker.PERMISSION_DENIED) {
            PromptUtils.showToast((Activity) context, "权限不足!");
            return;
        }
        try {

            File file = new File(mDirectory, "aimi");

            if (!file.exists()) {
                file.mkdir();
            }

            mFile = new File(file, "AIMI.apk");
            if (mFile.exists()) {
                mFile.delete();
            }
            mFile.createNewFile();
            //创建一个输出流
            mFileOutputStream = new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDownLoadListener(DownLoadListener downLoadListener) {

        mDownLoadListener = downLoadListener;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param strings The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... strings) {
        BufferedInputStream inputStream = null;
        try {
            String string = strings[0];
            URL url = new URL(string);
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setReadTimeout(600000);
            mHttpURLConnection.setConnectTimeout(600000);
            mHttpURLConnection.connect();
            int responseCode = mHttpURLConnection.getResponseCode();
            Log.e("TAG", "doInBackground: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int contentLength = mHttpURLConnection.getContentLength();//文件大小
                inputStream = new BufferedInputStream(mHttpURLConnection.getInputStream());
                Log.e("TAG", "doInBackground: " + inputStream.available() + ".." + contentLength);
                int size = 0;
                int len = 0;
                byte[] buf = new byte[1024];
                while ((size = inputStream.read(buf)) != -1) {
                    len += size;
                    mFileOutputStream.write(buf, 0, size);
                    // 打印下载百分比
                    // System.out.println("下载了-------> " + len * 100 / fileLength +
                    // "%\n");
                    publishProgress(string.valueOf(contentLength), string.valueOf(len));
                }


                return mFile.getAbsolutePath();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (mFileOutputStream != null) {
                    mFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mDownLoadListener == null) {
            throw new RuntimeException("请实现 DownLoadListener 接口");
        }
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param s The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            mDownLoadListener.onDownLoadListenerFinish(s);
        }
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String max = values[0];
        String len = values[1];
        int a = Integer.valueOf(max);
        int b = Integer.valueOf(len);

        int f = b * 100 / a;
        mDownLoadListener.onDownLoadListener(f);
    }

    /**
     * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
     * {@link #doInBackground(Object[])} has finished.</p>
     * <p>
     * <p>The default implementation simply invokes {@link #onCancelled()} and
     * ignores the result. If you write your own implementation, do not call
     * <code>super.onCancelled(result)</code>.</p>
     *
     * @param s The result, if any, computed in
     *          {@link #doInBackground(Object[])}, can be null
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        mDownLoadListener.onDownLoadListenerCancel(s);
    }


}
