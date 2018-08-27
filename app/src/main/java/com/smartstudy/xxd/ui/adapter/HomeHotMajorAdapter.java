package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;
import com.smartstudy.xxd.ui.activity.MajorInfoActivity;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 热门专业选校适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class HomeHotMajorAdapter extends CommonAdapter<HomeHotProgramInfo> {

    private int margin5;
    private int margin16;

    public HomeHotMajorAdapter(Context context, int layoutId, List<HomeHotProgramInfo> datas) {
        super(context, layoutId, datas);
        margin5 = DensityUtils.dip2px(5f);
        margin16 = DensityUtils.dip2px(16f);
    }

    @Override
    protected void convert(ViewHolder holder, final HomeHotProgramInfo homeHotInfo, int position) {
        //调整布局,3为列数
        int index = position % 3;
        if (index == 0) {
            holder.getView(R.id.ll_tab).setPadding(margin16, 0, margin5, margin16);
        } else if (index == 2) {
            holder.getView(R.id.ll_tab).setPadding(margin5, 0, margin16, margin16);
        } else {
            holder.getView(R.id.ll_tab).setPadding(margin5, 0, margin5, margin16);
        }
        holder.setText(R.id.tv_major_name, homeHotInfo.getChineseName());
        holder.getView(R.id.tv_major_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMajorInfo = new Intent(mContext, MajorInfoActivity.class);
                toMajorInfo.putExtra("majorId", homeHotInfo.getId());
                toMajorInfo.putExtra(TITLE, homeHotInfo.getChineseName());
                mContext.startActivity(toMajorInfo);
            }
        });
    }
}
