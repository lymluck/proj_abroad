package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.SpecialRankInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SpecialRankContract;
import com.smartstudy.xxd.mvp.presenter.SpecialRankPresenter;

import java.util.ArrayList;
import java.util.List;

public class SpecialRankActivity extends UIActivity implements SpecialRankContract.View {
    private RecyclerView rclv_special;
    private CommonAdapter<SpecialRankInfo> mAdapter;
    private EmptyWrapper<SpecialRankInfo> emptyWrapper;
    private SpecialRankContract.Presenter specialP;
    private List<SpecialRankInfo> specialRankInfos;
    private int flag;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_rank);
        addActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (rclv_special != null) {
            rclv_special.removeAllViews();
            rclv_special = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (specialRankInfos != null) {
            specialRankInfos.clear();
            specialRankInfos = null;
        }
        if (specialP != null) {
            specialP = null;
        }
        super.onDestroy();
        removeActivity(SpecialRankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(data.getStringExtra("title"));
        rclv_special = (RecyclerView) findViewById(R.id.rclv_special);
        rclv_special.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_special.setLayoutManager(mLayoutManager);
        rclv_special.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        specialRankInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<SpecialRankInfo>(this, R.layout.item_special_list, specialRankInfos) {
            @Override
            protected void convert(ViewHolder holder, SpecialRankInfo specialRankInfo, int position) {
                holder.setText(R.id.tv_special_name, specialRankInfo.getAbbr());
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rclv_special.setAdapter(emptyWrapper);
        flag = data.getIntExtra("flag", ParameterUtils.GL_FLAG);
        new SpecialRankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_special, false);
        specialP.showLoading(this, emptyView);
        specialP.getTypeList(ParameterUtils.NETWORK_ELSE_CACHED, flag, specialRankInfos);

    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SpecialRankInfo info = specialRankInfos.get(position);
                Intent toRank = new Intent(SpecialRankActivity.this, RankActivity.class);
                toRank.putExtra("title", info.getAbbr());
                toRank.putExtra("id", info.getId() + "");
                startActivity(toRank);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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

    @Override
    public void setPresenter(SpecialRankContract.Presenter presenter) {
        if (presenter != null) {
            this.specialP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void getTypeSuccess() {
        if (specialP != null) {
            specialP.setEmptyView(this, emptyView);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        specialP.showLoading(this, emptyView);
        specialP.getTypeList(ParameterUtils.CACHED_ELSE_NETWORK, flag, specialRankInfos);
    }
}
