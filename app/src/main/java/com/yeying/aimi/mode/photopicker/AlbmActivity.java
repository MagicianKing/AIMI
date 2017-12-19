package com.yeying.aimi.mode.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.otherdetails.PreViewShowActivity;
import com.yeying.aimi.utils.FileUtils;
import com.yeying.aimi.utils.PromptUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 读取相册
 */
public class AlbmActivity extends BaseActivity implements AlbmAsynctask.onLoadFinishListener, View.OnClickListener, AdapterView.OnItemClickListener, ParentAdapter.ItemClick {

    private AlbmAsynctask mAlbmAsynctask;
    private List<ImageBean> mList;
    private Map<String, List<String>> map;
    private List<ImgBean> mchilids;
    private ParentAdapter mAdapter;
    private RecyclerView mGroupGrid;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mNames;
    private final int REQUEST_CAMERA = 88;

    /**
     * 显示所有文件夹
     */
    private TextView mText;
    private ListView mListView;
    private PopupWindow mPopupWindow;

    private Uri photoUri;
    private File mTmpFile;
    private String selectFilePath = null;

    private boolean isNext = false;//是否进行下一步
    private boolean isjiancai = false;//是否需要裁剪
    private boolean isFilter = false;
    public static final String filePath = "filePath";

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_albm);
        mList = new ArrayList<>();
        mchilids = new ArrayList<>();
        mNames = new ArrayList<>();
        initIntent();
        initView ();
        initPopWindow ();
        mAlbmAsynctask = new AlbmAsynctask (this);
        mAlbmAsynctask.execute ("");

    }

    private void initIntent() {
        isNext = getIntent().getBooleanExtra("isNext",false);
        isjiancai = getIntent().getBooleanExtra("isjiancai",false);
        isFilter = getIntent().getBooleanExtra("isFilter",false);
    }

    private void initPopWindow () {
        View view = LayoutInflater.from (this).inflate (R.layout.pop_list, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView = (ListView) view.findViewById (R.id.listview);
        mListView.setOnItemClickListener (this);
        mPopupWindow.setFocusable (true);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.test_list_item, mNames);
        mListView.setAdapter (mArrayAdapter);
    }


    @Override
    protected void onResume () {
        super.onResume ();
        mAlbmAsynctask.setOnLoadFinishListner (this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadFinish (Map<String, List<String>> map) {
        this.map = map;
        Log.e ("TAG", "loadFinish: " + map.size ());
        for (String s : map.keySet ()) {
            ImageBean imageBean = new ImageBean ();
            imageBean.setFolderName (s);
            imageBean.setImageCounts (map.get (s).size ());
            imageBean.setToImagePath (map.get (s).get (0));
            //父文件夹
            mList.add (imageBean);
            mNames.add (s);
        }
        //mArrayAdapter.notifyDataSetChanged ();
        List<String> allPath = new ArrayList<>();
        for (int j = 0 ; j < mList.size() ; j ++){
            ImageBean imageBean = mList.get (j);
            List<String> list = map.get (imageBean.getFolderName ());
            Collections.reverse(list);
            allPath.addAll(list);
        }
        Collections.reverse(allPath);
        mchilids.clear ();
        for (int i = 0 ; i < allPath.size() ; i ++){
            ImgBean imgBean = new ImgBean();
            imgBean.setFilePath(allPath.get(i));
            imgBean.setSelectFlag(false);
            imgBean.setFirst(false);
            mchilids.add(imgBean);
        }
        addFirst();
        mAdapter.notifyDataSetChanged ();
    }


    private void initView () {
        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_left_view.setImageResource(R.drawable.close_light);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_right_tv.setText("下一步");
        title_right_tv.setBackgroundResource(R.drawable.round_yellow_bg);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setOnClickListener(this);
        title_left.setOnClickListener(this);
        title_center_view.setText("相册");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);
        mGroupGrid = (RecyclerView) findViewById (R.id.albm_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        mGroupGrid.setLayoutManager(gridLayoutManager);
        mAdapter = new ParentAdapter (this, mchilids);
        mAdapter.setItemClick(this);
        mGroupGrid.setAdapter (mAdapter);

    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right_tv:
                if (!TextUtils.isEmpty(selectFilePath)){
                    if (isNext){
                        if (isFilter){
                            CropImageActivity.toCropImg(AlbmActivity.this,selectFilePath,isjiancai,isFilter);
                        }else {
                            CropImageActivity.toCropImgWithResylt(AlbmActivity.this,selectFilePath,CropImageActivity.REQUEST_CODE,isjiancai,isFilter);
                        }
                    }else {
                        Intent intent = new Intent();
                        intent.putExtra(filePath,selectFilePath);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }else {
                    PromptUtils.showToast(AlbmActivity.this,"请选择一张图片");
                }


                break;
        }
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

        ImageBean imageBean = mList.get (position);
        List<String> list = map.get (imageBean.getFolderName ());
        mchilids.clear ();
        for (int i = 0 ; i < list.size() ; i ++){
            ImgBean imgBean = new ImgBean();
            imgBean.setFilePath(list.get(i));
            imgBean.setSelectFlag(false);
            imgBean.setFirst(false);
            mchilids.add(imgBean);
        }
        addFirst();
        mAdapter.notifyDataSetChanged ();
        mPopupWindow.dismiss ();
    }

    @Override
    public void onSelectClick(int position) {
        for (int i = 0 ; i < mchilids.size() ; i++){
            if (i == position){
                if (mchilids.get(i).isSelectFlag()){
                    mchilids.get(i).setSelectFlag(false);
                }else {
                    mchilids.get(i).setSelectFlag(true);
                }
            }else {
                mchilids.get(i).setSelectFlag(false);
            }
        }
        selectFilePath = mchilids.get(position).getFilePath();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onImgClick(int position) {
        PreViewShowActivity.toPreView(AlbmActivity.this,mchilids.get(position).getFilePath());
    }

    @Override
    public void onCameraClick(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                @Override
                public void onAllGranted() {
                    showCamera();
                }

                @Override
                public void onGranted(List<String> grantPermissions) {

                }

                @Override
                public void onDenied(List<String> deniedPermissions) {

                }
            });
        } else {
            showCamera();
        }
    }

    private void showCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTmpFile = FileUtils.createTmpFile(AlbmActivity.this,String.valueOf(System.currentTimeMillis()),".jpeg");
        photoUri = Uri.fromFile(mTmpFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }


    private void addFirst(){
        ImgBean imgBean = new ImgBean();
        imgBean.setFirst(true);
        imgBean.setFilePath("");
        imgBean.setSelectFlag(false);
        mchilids.add(0,imgBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                AlbmActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,photoUri));
                setUnSelect(mchilids);
                ImgBean imgBean = new ImgBean();
                imgBean.setFilePath(mTmpFile.getPath());
                imgBean.setFirst(false);
                imgBean.setSelectFlag(true);
                mchilids.add(1,imgBean);
                mAdapter.notifyDataSetChanged ();
                selectFilePath = mTmpFile.getPath();
            }
            if (requestCode == CropImageActivity.REQUEST_CODE){
                Intent intent = new Intent();
                intent.putExtra(filePath,data.getStringExtra(CropImageActivity.REQUEST_STR));
                Log.e(TAG, "onActivityResult: "+data.getStringExtra(CropImageActivity.REQUEST_STR));
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    private void setUnSelect(List<ImgBean> list){
        for (ImgBean imgbean : list) {
            imgbean.setSelectFlag(false);
        }
    }

    public static void toAlbm(Context context,boolean isNext,boolean isjiancai,boolean isFilter){
        Intent intent = new Intent(context,AlbmActivity.class);
        intent.putExtra("isNext",isNext);
        intent.putExtra("isjiancai",isjiancai);
        intent.putExtra("isFilter",isFilter);
        context.startActivity(intent);
    }

    public static void toAlbmWithResult(Activity activity,int requestCode , boolean isNext , boolean isjiancai , boolean isFilter){
        Intent intent = new Intent(activity,AlbmActivity.class);
        intent.putExtra("isNext",isNext);//是否进行下一步
        intent.putExtra("isjiancai",isjiancai);//是否需要裁剪
        intent.putExtra("isFilter",isFilter);//是否需要滤镜
        activity.startActivityForResult(intent,requestCode);
    }
}
