package com.smartstudy.commonlib.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SelectMyPhotoAdapter extends CommonAdapter<String> {

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private TextView mOKBtn;
    private boolean mChecked;
    /**
     * 用来存储图片的选中情况
     */
    private SparseBooleanArray mSelectMap = new SparseBooleanArray();

    public SelectMyPhotoAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, itemLayoutId, mDatas);
        this.mDirPath = dirPath;
        mOKBtn = (TextView) ((Activity) context).findViewById(R.id.topdefault_righttext);
    }

    @Override
    public void convert(final ViewHolder helper, final String item, final int position) {
        // 设置图片
        String imageUrl = mDirPath + "/" + item;
        if (mDirPath == null) {
            imageUrl = item;
        }
        ImageView imageView = helper.getView(R.id.id_item_image);
        helper.setImageUrl(imageView, imageUrl);
        final CheckBox mCheckBox = helper.getView(R.id.child_checkbox);
        mCheckBox.setChecked(mSelectMap.get(position));
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckBox.isChecked()) {
                    if (mSelectMap.size() < 9) {
                        mChecked = true;
                        mSelectMap.put(position, true);
                        Utils.addAnimation(mCheckBox);
                    } else {
                        mChecked = false;
                        ToastUtils.showToast(mContext.getString(R.string.picture_max));
                        mCheckBox.setChecked(mSelectMap.get(position));
                    }
                } else if (mSelectMap.size() <= 9) {
                    mChecked = false;
                    mSelectMap.delete(position);
                }

                if (mSelectMap.size() > 0) {
                    mOKBtn.setClickable(true);
                    mOKBtn.setText(mContext.getString(R.string.sure) + "(" + mSelectMap.size() + "/" + "9)");
                } else {
                    mOKBtn.setText(mContext.getString(R.string.sure));
                    mOKBtn.setClickable(false);
                }
            }
        });
        LinearLayout mCheckBoxLl = helper.getView(R.id.llyt_check_pic);
        mCheckBoxLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChecked) {
                    if (mSelectMap.size() < 9) {
                        mSelectMap.put(position, true);
                        mCheckBox.setChecked(true);
                        Utils.addAnimation(mCheckBox);
                        mChecked = true;
                    } else {
                        mChecked = false;
                        ToastUtils.showToast(mContext.getString(R.string.picture_max));
                        mCheckBox.setChecked(mSelectMap.get(position));
                    }
                } else if (mSelectMap.size() <= 9) {
                    mSelectMap.delete(position);
                    mChecked = false;
                    mCheckBox.setChecked(false);
                }

                if (mSelectMap.size() > 0) {
                    mOKBtn.setClickable(true);
                    mOKBtn.setText(mContext.getString(R.string.sure) + "(" + mSelectMap.size() + "/" + "9)");
                } else {
                    mOKBtn.setText(mContext.getString(R.string.sure));
                    mOKBtn.setClickable(false);
                }
            }
        });
        final TextView mCapture = helper.getView(R.id.photo_item_one);
        if (mOKBtn.getVisibility() == View.VISIBLE) {
            mCheckBoxLl.setVisibility(View.VISIBLE);
        } else if (mOKBtn.getVisibility() == View.GONE) {
            mCheckBoxLl.setVisibility(View.GONE);
        }
        if (!"".equals(item)) {
            imageView.setVisibility(View.VISIBLE);
            mCapture.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
            mCapture.setVisibility(View.VISIBLE);
            mCheckBoxLl.setVisibility(View.GONE);
        }
    }

    /**
     * 获取选中的Item的position
     *
     * @return 选中的图片路径集合
     */
    public List<Integer> getSelectItems() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mSelectMap.size(); i++) {
            list.add(mSelectMap.keyAt(i));
        }
        return list;
    }

}
