package com.smartstudy.xxd.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customView.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customView.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customView.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.CommandSchoolListContract;
import com.smartstudy.xxd.mvp.presenter.CommandSchoolListPresenter;

import java.util.ArrayList;
import java.util.List;

@Route("CommandSchoolListActivity")
public class CommandSchoolListActivity extends UIActivity implements CommandSchoolListContract.View {

    private LoadMoreRecyclerView rclv_command;
    private CommonAdapter<SchoolInfo> mAdapter;
    private LoadMoreWrapper<SchoolInfo> loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private Dialog mDialog;

    private String spId;
    private int mPage = 1;
    private ArrayList<SchoolInfo> schoolsInfoList;
    private CommandSchoolListContract.Presenter schoolP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_school_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (schoolsInfoList != null) {
            schoolsInfoList.clear();
            schoolsInfoList = null;
        }
        if (schoolP != null) {
            schoolP = null;
        }
        if (rclv_command != null) {
            rclv_command.removeAllViews();
            rclv_command = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        titleView.setText(data.getStringExtra("title"));
        spId = data.getStringExtra("id");
        rclv_command = (LoadMoreRecyclerView) findViewById(R.id.rclv_command);
        rclv_command.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_command.setLayoutManager(mLayoutManager);
        rclv_command.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new CommandSchoolListPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_command, false);
        schoolP.showLoading(this, emptyView);
        schoolP.getSchools(ParameterUtils.NETWORK_ELSE_CACHED, spId, mPage, ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        schoolsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<SchoolInfo>(this, R.layout.item_command_school_list, schoolsInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, final SchoolInfo schoolInfo, final int position) {
                if (schoolInfo != null) {
                    holder.setCircleImageUrl(R.id.iv_logo, schoolInfo.getLogo(), true);
                    holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                    holder.setText(R.id.tv_English_name, schoolInfo.getEnglishName());
                    holder.setText(R.id.tv_local_rate, String.format(getString(R.string.local_rank), schoolInfo.getLocalRank()));
                    holder.setText(R.id.tv_spe_name, String.format(getString(R.string.speName), schoolInfo.getMajorChineseName()));
                    holder.setText(R.id.tv_spe_english_name, String.format(getString(R.string.speEgName), schoolInfo.getMajorEnglishName()));
                    final ImageView iv = holder.getView(R.id.iv_add_school);
                    if (schoolInfo.isSelected()) {
                        iv.setImageResource(R.drawable.ic_choose_green);
                    } else {
                        iv.setImageResource(R.drawable.ic_add_circle_blue);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //添加至我的选校，添加按钮进行改变
                            if (schoolInfo.isSelected()) {
                                schoolP.deleteMyChoose(schoolInfo.getId() + "", position);
                            } else {
                                showDialog(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (v.getId()) {
                                            case R.id.llyt_top:
                                                schoolP.add2MySchool(ParameterUtils.MATCH_TOP, schoolInfo.getId(), position);
                                                mDialog.dismiss();
                                                break;
                                            case R.id.llyt_mid:
                                                schoolP.add2MySchool(ParameterUtils.MATCH_MID, schoolInfo.getId(), position);
                                                mDialog.dismiss();
                                                break;
                                            case R.id.llyt_bottom:
                                                schoolP.add2MySchool(ParameterUtils.MATCH_BOT, schoolInfo.getId(), position);
                                                mDialog.dismiss();
                                                break;
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclv_command.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        rclv_command.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                schoolP.getSchools(ParameterUtils.CACHED_ELSE_NETWORK, spId, mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SchoolInfo info = schoolsInfoList.get(position);
                Bundle params = new Bundle();
                params.putString("id", info.getId() + "");
                Intent toMoreDetails = new Intent(CommandSchoolListActivity.this, SchoolDetailActivity.class);
                toMoreDetails.putExtras(params);
                startActivity(toMoreDetails);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
        }
    }

    private void showDialog(View.OnClickListener listener) {
        mDialog = DialogCreator.createAdd2SchoolDialog(this, "添加为",
                listener);
        mDialog.getWindow().setLayout((int) (0.8 * ScreenUtils.getScreenWidth()),
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    @Override
    public void setPresenter(CommandSchoolListContract.Presenter presenter) {
        if (presenter != null) {
            this.schoolP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (schoolP != null) {
            rclv_command.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void getSchoolsSuccess(List<SchoolInfo> data, int request_state) {
        if (schoolP != null) {
            schoolP.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_command.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsInfoList.clear();
                schoolsInfoList.addAll(data);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclv_command.loadComplete(false);
                } else {
                    rclv_command.loadComplete(true);
                    schoolsInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void noitfyItem(int position) {
        SchoolInfo info = schoolsInfoList.get(position);
        if (info.isSelected()) {
            info.setSelected(false);
        } else {
            info.setSelected(true);
        }
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_command.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void reload() {
        schoolP.showLoading(this, emptyView);
        schoolP.getSchools(ParameterUtils.NETWORK_ELSE_CACHED, spId, mPage, ParameterUtils.PULL_DOWN);
    }
}
