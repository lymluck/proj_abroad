package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HighSchoolRankInfo;
import com.smartstudy.xxd.mvp.contract.HighSchoolRankContract;
import com.smartstudy.xxd.mvp.presenter.HighSchoolRankPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.CATEGORY_ID;
import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe 高中学校排名界面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolRankActivity extends BaseActivity implements HighSchoolRankContract.View {

    private RecyclerView rclvRanks;
    private HighSchoolRankContract.Presenter presenter;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<HighSchoolRankInfo> highSchoolRankInfos;
    private CommonAdapter<HighSchoolRankInfo> mAdapter;
    private EmptyWrapper<HighSchoolRankInfo> emptyWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_school_rank);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (rclvRanks != null) {
            rclvRanks.removeAllViews();
            rclvRanks = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (highSchoolRankInfos != null) {
            highSchoolRankInfos.clear();
            highSchoolRankInfos = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager = null;
        }
    }

    @Override
    protected void initViewAndData() {
        rclvRanks = findViewById(R.id.rclv_ranks);
        rclvRanks.setHasFixedSize(true);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvRanks.setLayoutManager(mLayoutManager);
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        titleView.setText("美国高中排行榜");
        new HighSchoolRankPresenter(this);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvRanks, false);
        presenter.showLoading(this, emptyView);
        presenter.getHighRank();
    }


    private void initAdapter() {
        highSchoolRankInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<HighSchoolRankInfo>(this, R.layout.item_high_schoo_usl_rank,
            highSchoolRankInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, HighSchoolRankInfo highSchoolRankInfo, int position) {
                if (highSchoolRankInfo != null) {
                    holder.setText(R.id.tv_rank, highSchoolRankInfo.getYear()
                        + highSchoolRankInfo.getOrg()
                        + highSchoolRankInfo.getTitle());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rclvRanks.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.putExtra(TITLE, highSchoolRankInfos.get(position).getTitle());
                intent.putExtra(CATEGORY_ID, highSchoolRankInfos.get(position).getId());
                intent.setClass(HighSchoolRankActivity.this, HighSchoolRankDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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

    @Override
    public void setPresenter(HighSchoolRankContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            emptyWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getHighRankSuccess(List<HighSchoolRankInfo> highSchoolRankInfos) {
        if (presenter != null && highSchoolRankInfos != null) {
            this.highSchoolRankInfos.clear();
            this.highSchoolRankInfos.addAll(highSchoolRankInfos);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getHighRank();
    }
}
