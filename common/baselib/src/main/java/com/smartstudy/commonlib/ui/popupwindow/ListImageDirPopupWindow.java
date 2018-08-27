package com.smartstudy.commonlib.ui.popupwindow;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.entity.ImageFloderInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;

import java.util.List;


public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloderInfo> {
    private RecyclerView mListDir;
    private String DirName;
    private CommonAdapter<ImageFloderInfo> mAdapter;

    public String getDirName() {
        return DirName;
    }

    public void setDirName(String dirName) {
        DirName = dirName;
    }

    public ListImageDirPopupWindow(int width, int height, List<ImageFloderInfo> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (RecyclerView) findViewById(R.id.id_list_dir);
        mListDir.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mListDir.setLayoutManager(mLayoutManager);
        mAdapter = new CommonAdapter<ImageFloderInfo>(context, R.layout.list_dir_item, mDatas) {
            @Override
            public void convert(ViewHolder helper, ImageFloderInfo item, int position) {
                helper.setText(R.id.id_dir_item_name, item.getName());
                ImageView imageView = helper.getView(R.id.iv_select_dirs);
                if (item.isSelected()) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                helper.setImageUrl(R.id.id_dir_item_image, item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        };
        mListDir.setAdapter(mAdapter);
    }

    public interface OnImageDirSelected {
        void selected(ImageFloderInfo floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                    refreshListView(position);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void refreshListView(int position) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (i == position) {
                mDatas.get(i).setIsSelected(true);
            } else {
                mDatas.get(i).setIsSelected(false);
            }
        }
    }

    @Override
    public void init() {
    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
    }

}
