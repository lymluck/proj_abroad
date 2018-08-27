package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HotRankInfo;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

public class HotRankTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_rank_type);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initViewAndData() {
        Intent args = getIntent();
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(args.getStringExtra(TITLE));
        RecyclerView rvHotRank = findViewById(R.id.rv_hot_rank);
        rvHotRank.setHasFixedSize(true);
        rvHotRank.setLayoutManager(new LinearLayoutManager(this));
        rvHotRank.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        rvHotRank.setAdapter(new CommonAdapter<HotRankInfo>(this, R.layout.item_hot_ranktype,
            args.<HotRankInfo>getParcelableArrayListExtra("data")) {

            @Override
            protected void convert(ViewHolder holder, final HotRankInfo info, int position) {
                holder.setText(R.id.tv_name, (TextUtils.isEmpty(info.getYear()) ? "" : info.getYear())
                    + (TextUtils.isEmpty(info.getType()) ? "" : info.getType())
                    + (TextUtils.isEmpty(info.getTitle()) ? "" : info.getTitle()));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent touk = new Intent(HotRankTypeActivity.this, RankActivity.class);
                        touk.putExtra("title", info.getTitle());
                        touk.putExtra("showBtn", info.isWorldRank());
                        touk.putExtra("id", info.getId());
                        startActivity(touk);
                    }
                });
            }
        });

    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }
}
