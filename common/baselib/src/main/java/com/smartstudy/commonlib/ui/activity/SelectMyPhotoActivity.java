package com.smartstudy.commonlib.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.ImageFloderInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.SelectMyPhotoAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.customview.clipimagelayout.ClipImageLayout;
import com.smartstudy.commonlib.ui.customview.decoration.DividerGridItemDecoration;
import com.smartstudy.commonlib.ui.popupwindow.ListImageDirPopupWindow;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class SelectMyPhotoActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {

    private TextView topdefault_centertitle;
    private RelativeLayout top_select_myphoto;
    private TextView topdefault_righttext;
    private RecyclerView mGirdView;
    private TextView mChooseDir;
    private TextView mImageCount;
    private RelativeLayout id_bottom_ly;

    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;

    /**
     * 当前选择的文件夹
     */
    private File mImgDir;

    /**
     * 所有的图片
     */
    private LinkedList<String> mImgs = null;
    /**
     * 当前文件夹中的所有图片
     */
    private List<String> mDirImgs = new ArrayList<String>();

    private SelectMyPhotoAdapter mAdapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloderInfo> mImageFloders = new ArrayList<ImageFloderInfo>();

    int totalCount;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    private String firstImage = null;
    private Uri imageUri = null;
    private WeakHandler myHandler = null;
    private File photoSaveFile;// 保存文件夹
    private String photoSaveName = null;// 图片名
    private boolean needClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_select_my_photo);
    }

    @Override
    protected void onDestroy() {
        if (mGirdView != null) {
            mGirdView.removeAllViews();
            mGirdView = null;
        }
        if (mImgs != null) {
            mImgs.clear();
            mImgs = null;
        }
        if (mDirImgs != null) {
            mDirImgs.clear();
            mDirImgs = null;
        }
        if (mDirPaths != null) {
            mDirPaths.clear();
            mDirPaths = null;
        }
        if (mImageFloders != null) {
            mImageFloders.clear();
            mImageFloders = null;
        }
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
    }

    @Override
    protected void initViewAndData() {
        needClip = getIntent().getBooleanExtra("neeclip", false);
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        top_select_myphoto = (RelativeLayout) findViewById(R.id.top_select_myphoto);
        topdefault_righttext = (TextView) findViewById(R.id.topdefault_righttext);
        mGirdView = (RecyclerView) findViewById(R.id.id_gridView);
        mGirdView.setHasFixedSize(true);
        int spanCount = 3;
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, spanCount);
        mGirdView.setLayoutManager(mLayoutManager);
        mGirdView.addItemDecoration(new DividerGridItemDecoration(spanCount, DensityUtils.dip2px(8), true));
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total);
        id_bottom_ly = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        myHandler = new WeakHandler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                mProgressDialog.dismiss();
                // 为View绑定数据
                dataToView();
                // 初始化展示文件夹的popupWindw
                initListDirPopupWindw();
                return false;
            }
        });
        top_select_myphoto.getBackground().setAlpha(255);
        topdefault_centertitle.setText(getResources().getString(R.string.choose_pic));
        mScreenHeight = ScreenUtils.getScreenHeight();
        getImages();
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.topdefault_righttext).setOnClickListener(this);
        findViewById(R.id.id_choose_dir).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.id_choose_dir) {
            mListImageDirPopupWindow.showAsDropDown(id_bottom_ly, 0, 0);
            Utils.convertActivityFromTranslucent(SelectMyPhotoActivity.this);
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = .3f;
            getWindow().setAttributes(lp);
        } else if (id == R.id.topdefault_righttext) {
            //存放选中图片的路径
            ArrayList<String> mPickedList = new ArrayList<>();
            //存放选中的图片的position
            List<Integer> positionList = mAdapter.getSelectItems();
            //拿到选中图片的路径
            for (int i = 0; i < positionList.size(); i++) {
                if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                    mPickedList.add(mImgs.get(positionList.get(i)));
                } else {
                    mPickedList.add(mDirImgs.get(positionList.get(i)));
                }
            }
            if (mPickedList.size() < 1) {
                return;
            } else {
                //照片多选
                Intent toClipImage = new Intent();
                toClipImage.putStringArrayListExtra("pathList", mPickedList);
                toClipImage.putExtra("flag_from", "from_album");
                setResult(RESULT_OK, toClipImage);
                finish();
            }
        }
    }

    private void initItemClick() {
        if (getIntent().getBooleanExtra("singlePic", true)) {
            topdefault_righttext.setVisibility(View.GONE);
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                        if (position == 0 && "".equals(mImgs.get(position))) {
                            if (PermissionUtil.hasPermissions(SelectMyPhotoActivity.this, Permission.CAMERA)) {
                                toCamera();
                            } else {
                                PermissionUtil.requestPermissions(SelectMyPhotoActivity.this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                                    ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
                            }
                        } else {
                            String photo_path = mImgs.get(position);
                            if (!TextUtils.isEmpty(photo_path)) {
                                if (needClip) {
                                    Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
                                    toClipImage.putExtra("path", photo_path);
                                    toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                                    startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                                } else {
                                    Intent intent = new Intent();
                                    intent.putExtra("path", photo_path);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            } else {
                                ToastUtils.showToast(getString(R.string.picture_load_failure));
                            }
                        }
                    } else {
                        if (position < mDirImgs.size()) {
                            String photoPath = mDirImgs.get(position);
                            Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
                            toClipImage.putExtra("path", photoPath);
                            toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                            startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            topdefault_righttext.setText(getString(R.string.sure));
            topdefault_righttext.setVisibility(View.VISIBLE);
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (ParameterUtils.ALL_PICS.equals(mChooseDir.getText().toString())) {
                        if (position == 0 && "".equals(mImgs.get(position))) {
                            if (PermissionUtil.hasPermissions(SelectMyPhotoActivity.this, Permission.CAMERA)) {
                                toCamera();
                            } else {
                                PermissionUtil.requestPermissions(SelectMyPhotoActivity.this, Permission.getPermissionContent(Arrays.asList(Permission.CAMERA)),
                                    ParameterUtils.REQUEST_CODE_CAMERA, Permission.CAMERA);
                            }
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getImages() {
        totalCount = 0;
        mChooseDir.setText(ParameterUtils.ALL_PICS);
        mImgs = new LinkedList<>();
        mImgs.addFirst("");
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showToast(getString(R.string.no_external_storage));
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.loading));

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectMyPhotoActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                ImageFloderInfo mAllPics = new ImageFloderInfo();
                mAllPics.setIsSelected(true);
                mAllPics.setDir(null);
                if (mCursor != null) {
                    mAllPics.setCount(mCursor.getCount());
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        mImgs.add(path);
                        // 拿到第一张图片的路径
                        if (firstImage == null) {
                            firstImage = path;
                            mAllPics.setFirstImagePath(firstImage);
                            mImageFloders.add(mAllPics);
                        }

                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null) {
                            continue;
                        }

                        String dirPath = parentFile.getAbsolutePath();

                        ImageFloderInfo imageFloder = null;
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new ImageFloderInfo();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                            imageFloder.setIsSelected(false);
                        }
                        String[] file_filter = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg");
                            }
                        });
                        int picSize = 0;
                        if (file_filter != null) {
                            picSize = file_filter.length;
                        }
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);

                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                            mImgDir = parentFile;
                        }
                    }
                    mCursor.close();
                }

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                myHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    /**
     * 为View绑定数据
     */
    private void dataToView() {
        if (mImgDir == null) {
            ToastUtils.showToast(getString(R.string.scan_no_picture));
//            return;
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mImageCount.setText(totalCount + getString(R.string.picture_unit));
        mAdapter = new SelectMyPhotoAdapter(SelectMyPhotoActivity.this, mImgs, R.layout.item_my_select_pic_grid,
            null);
        initItemClick();
        mGirdView.setAdapter(mAdapter);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.6),
            mImageFloders, LayoutInflater.from(getApplicationContext())
            .inflate(R.layout.popupwindow_pic_list, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                Utils.convertActivityToTranslucent(SelectMyPhotoActivity.this);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */


    @Override
    public void selected(ImageFloderInfo floder) {
        if (topdefault_righttext.getVisibility() == View.VISIBLE) {
            topdefault_righttext.setText(getString(R.string.sure));
        }
        if (floder.getDir() != null) {
            mImgDir = new File(floder.getDir());
            List<String> Imgs_name = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg");
                }
            }));
            /**
             * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
             */
            mImageCount.setText(floder.getCount() + getString(R.string.picture_unit));
            mDirImgs.clear();
            for (String name : Imgs_name) {
                mDirImgs.add(mImgDir + "/" + name);
            }
            mAdapter = new SelectMyPhotoAdapter(SelectMyPhotoActivity.this, Imgs_name, R.layout.item_my_select_pic_grid,
                mImgDir.getAbsolutePath());
            initItemClick();
            mGirdView.setAdapter(mAdapter);
            // mAdapter.notifyDataSetChanged();

            mChooseDir.setText(floder.getName());
        } else {
            mDirPaths = new HashSet<>();
            dataToView();
        }
        mListImageDirPopupWindow.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CAMERA:
                if (imageUri != null) {
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
                    sendBroadcast(localIntent);
                    mImgs.removeFirst();
                    mImgs.addFirst(imageUri.getPath());
                    mImgs.addFirst("");
                    mAdapter.notifyDataSetChanged();
                    imageUri = null;
                } else {
                    String path_capture = photoSaveFile.getAbsolutePath() + "/" + photoSaveName;
                    if (needClip) {
                        Intent toClipImage = new Intent(SelectMyPhotoActivity.this, ClipPictureActivity.class);
                        toClipImage.putExtra("path", path_capture);
                        toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                        startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("path", path_capture);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                break;
            case ParameterUtils.REQUEST_CODE_CLIP_OVER:
                setResult(RESULT_OK, data);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CAMERA:
                toCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //拍照权限申请被拒绝
        verifyPermission(perms);
    }

    private void toCamera() {
        if (getIntent().getBooleanExtra("singlePic", true)) {
            photoSaveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            photoSaveFile = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures");
            Utils.startActionCapture(SelectMyPhotoActivity.this, new File(photoSaveFile.getAbsolutePath(), photoSaveName), ParameterUtils.REQUEST_CODE_CAMERA);
        } else {
            photoSaveName = System.currentTimeMillis() + ".png";
            // 存放照片的文件夹
            photoSaveFile = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures");
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (photoSaveFile != null) {
                imageUri = Uri.fromFile(new File(photoSaveFile.getAbsolutePath(), photoSaveName));
            }
            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, ParameterUtils.REQUEST_CODE_CAMERA);
        }
    }
}
