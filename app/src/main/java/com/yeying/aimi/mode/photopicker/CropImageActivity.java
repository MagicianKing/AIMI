package com.yeying.aimi.mode.photopicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.picture.ImageFilterActivity;
import com.yeying.aimi.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressLint("CutPasteId")
public class CropImageActivity extends BaseActivity implements OnTouchListener, OnClickListener {
    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    private final String IMAGE_CAPTURE_NAME = "temp.jpg";
    int kspwidth;
    int kspheight;
    private ImageView srcPic;
    private CroppView clipview;
    private RelativeLayout cogbtn;
    private RelativeLayout bgview;
    private RelativeLayout canclebtn;
    private Bitmap kspBitmap;
    private String filePath;
    private File mTempDir;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    /**
     * 初始化动作标志
     */
    private int mode = NONE;
    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private Bitmap bitmap;
    private boolean isFromMyActivity;
    private byte[] bitmapByte;
    private byte[] btmByte;
    private ClipImageLayout clip_layout = null;
    //ksp新加
    private ClipImageLayoutJiancai clip_jiancai = null;
    private File mFile;
    private Bitmap btm;
    private boolean isFromSmallMain;
    private boolean isjiancai;
    private boolean isFilter;
    private Bitmap clipBitmap;

    public static int REQUEST_CODE = 99;
    public static String REQUEST_STR = "request_str";

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_head);
        getmyWindow();
        initIntent();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initIntent() {
        filePath = getIntent().getStringExtra("imgUri");
        isjiancai = getIntent().getBooleanExtra("isjiancai", false);
        isFilter = getIntent().getBooleanExtra("isFilter",false);
    }

    private void initView() {
        cogbtn = (RelativeLayout) findViewById(R.id.cpp_cogis);
        cogbtn.setOnClickListener(this);
        canclebtn = (RelativeLayout) findViewById(R.id.cpp_cancle);
        canclebtn.setOnClickListener(this);
        bgview = (RelativeLayout) findViewById(R.id.cpp_bglayout);
        clip_layout = (ClipImageLayout) findViewById(R.id.cpp_bglayout);
        clip_jiancai = (ClipImageLayoutJiancai) findViewById(R.id.cpp_bglayout_jiacai);
        mFile = FileUtils.createTmpFile(getApplicationContext(), null);

        Intent intent = getIntent();
        isFromMyActivity = intent.getBooleanExtra("isFromMyActivity", false);
        String imagePath = intent.getStringExtra("imagePath");
        isFromSmallMain = intent.getBooleanExtra("isFromSmallMain", false);
        if (isjiancai) {
            if (getIntent().getBooleanExtra("isDongTai",false)){
                clip_jiancai.setVisibility(View.GONE);
                clip_layout.setVisibility(View.VISIBLE);
            }else{
                clip_jiancai.setVisibility(View.VISIBLE);
                clip_layout.setVisibility(View.GONE);
            }
        } else {
            clip_jiancai.setVisibility(View.GONE);
            clip_layout.setVisibility(View.VISIBLE);

        }

        try {
            if (imagePath != null && !"".equals(imagePath)) {
                bitmap = createBitmap(imagePath);
                kspBitmap = bitmap;
            } else {
                bitmap = createBitmap(filePath);
            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
            baos.close();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=false;
            if (bitmap.getByteCount()/(1024*1024)>6){
                options.inSampleSize=6;
                bitmap=BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length,options);
            }else if (bitmap.getByteCount()/(1024*1024)>3){
                options.inSampleSize=3;
                bitmap=BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length,options);
            }else if (bitmap.getByteCount()/(1024*1024)>1){
                options.inSampleSize=2;
                bitmap=BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length,options);
            }else {
                options.inSampleSize=1;
                bitmap=BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length,options);
            }

            if (this.bitmap != null) {
                //ksp新加
                if (isjiancai) {
                    if (getIntent().getBooleanExtra("isDongTai",false)){
                        clip_layout.setImageBitmap(this.bitmap);
                    }else{
                        clip_jiancai.setImageBitmap(this.bitmap);
                    }

                } else {
                    clip_layout.setImageBitmap(this.bitmap);
                }
                Log.e("真实宽", this.bitmap.getWidth() + "");
                Log.e("真实高", this.bitmap.getHeight() + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v.getId() == cogbtn.getId()) {
            String filePath = null;
            if (isjiancai) {
                clipBitmap = clip_jiancai.clip(kspBitmap, kspwidth, kspheight);
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yeying/imgs/"+System.currentTimeMillis()+".jpeg";
            } else {
                //当不是裁剪的时候 不对图片做处理
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yeying/imgs/aimi.jpeg";
            }
            if (clipBitmap == null) {
                clipBitmap = bitmap;
            }
            btm = clipBitmap;

            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            } else {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                btm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String path = file.getPath();
            if (isFilter){
                ImageFilterActivity.toImageFilter(CropImageActivity.this,path);
            }else {
                Intent intent = new Intent();
                intent.putExtra(REQUEST_STR,path);
                Log.e(TAG, "onClick: "+path);
                setResult(RESULT_OK,intent);
                finish();
            }
        } else if (v.getId() == canclebtn.getId()) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取裁剪框内截图
     *
     * @return
     */
    private Bitmap getBitmap() {
        // 获取截屏
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
                clipview.getClipLeftMargin(), clipview.getClipTopMargin()
                        + statusBarHeight, clipview.getClipWidth(),
                clipview.getClipHeight());

        // 释放资源
        view.destroyDrawingCache();
        return finalBitmap;
    }


    private Bitmap createBitmap(String path) {
        if (path == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    //图片uri转变为路径
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void getmyWindow() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        kspwidth = metric.widthPixels;     // 屏幕宽度（像素）
        kspheight = metric.heightPixels;   // 屏幕高度（像素）
    }

    public static void toCropImg(Context context , String filePath , boolean isjiancai , boolean isFilter){
        Intent intent = new Intent(context,CropImageActivity.class);
        intent.putExtra("imgUri",filePath);
        intent.putExtra("isjiancai",isjiancai);
        intent.putExtra("isFilter",isFilter);
        context.startActivity(intent);
    }

    public static void toCropImgWithResylt(Activity context , String filePath ,int requestCode, boolean isjiancai , boolean isFilter){
        Intent intent = new Intent(context,CropImageActivity.class);
        intent.putExtra("imgUri",filePath);
        intent.putExtra("isjiancai",isjiancai);
        intent.putExtra("isFilter",isFilter);
        context.startActivityForResult(intent,requestCode);
    }

}
