package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.entity.RstSchoolInfo;
import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.customview.decoration.SuspensionDecoration;
import com.smartstudy.commonlib.ui.customview.guidview.HoleBean;
import com.smartstudy.commonlib.ui.customview.guidview.NewbieGuideManager;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SrtSchoolResultContract;
import com.smartstudy.xxd.mvp.presenter.SrtSchoolResultPresenter;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class SrtSchoolResultActivity extends UIActivity implements SrtSchoolResultContract.View {

    private RecyclerView rclvschoolrlt;
    private CommonAdapter<SmartSchoolRstInfo> mAdapter;
    private EmptyWrapper<SmartSchoolRstInfo> emptyWrapper;

    private ArrayList<SmartSchoolRstInfo> schoolInfo;
    private SrtSchoolResultContract.Presenter srtP;
    private int clickPos = -1;
    private Intent intent_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_school_result);
        addActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (rclvschoolrlt != null) {
            rclvschoolrlt.removeAllViews();
            rclvschoolrlt = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schoolInfo != null) {
            schoolInfo.clear();
            schoolInfo = null;
        }
        if (srtP != null) {
            srtP = null;
        }
        super.onDestroy();
        removeActivity(SrtSchoolResultActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("智能选校结果");
        this.rclvschoolrlt = (RecyclerView) findViewById(R.id.rclv_school_rlt);
        LinearLayout llyt_menu = (LinearLayout) findViewById(R.id.llyt_menu);
        rclvschoolrlt.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvschoolrlt.setLayoutManager(mLayoutManager);
        intent_data = getIntent();
        schoolInfo = intent_data.getParcelableArrayListExtra("choose_result");
        rclvschoolrlt.addItemDecoration(new SuspensionDecoration(this, schoolInfo).setPic(true).setmTitleHeight(DensityUtils.dip2px(48)));
        rclvschoolrlt.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        mAdapter = new CommonAdapter<SmartSchoolRstInfo>(this, R.layout.item_school_list, schoolInfo) {

            @Override
            protected void convert(ViewHolder holder, final SmartSchoolRstInfo smartSchoolRstInfo, final int position) {
                if (smartSchoolRstInfo != null) {
                    RstSchoolInfo schoolInfo = smartSchoolRstInfo.getSchool();
                    if (schoolInfo != null) {
                        final ImageView iv = holder.getView(R.id.iv_add_school);
                        if (position == 0) {
                            if (NewbieGuideManager.isNeverShowed(SrtSchoolResultActivity.this, NewbieGuideManager.TYPE_SAMRT_CHOOSE)) {
                                new NewbieGuideManager(SrtSchoolResultActivity.this, NewbieGuideManager.TYPE_SAMRT_CHOOSE).addView(iv,
                                        HoleBean.TYPE_CIRCLE).show();
                            }
                        }
                        if (holder.getView(R.id.llyt_smart_rate).getVisibility() == View.GONE) {
                            holder.getView(R.id.llyt_smart_rate).setVisibility(View.VISIBLE);
                            iv.setVisibility(View.VISIBLE);
                        }
                        holder.setCircleImageUrl(R.id.iv_logo, schoolInfo.getLogo(), true);
                        holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                        holder.setText(R.id.tv_English_name, schoolInfo.getEnglishName());
                        int rate = (int) (smartSchoolRstInfo.getProb() * 100);
                        holder.setText(R.id.tv_smart_rate, String.format(getString(R.string.my_rate), rate + "%"));
                        holder.setText(R.id.tv_local_rank, String.format(getString(R.string.local_rank), TextUtils.isEmpty(schoolInfo.getLocalRank()) ? "暂无" : schoolInfo.getLocalRank()));
                        if (smartSchoolRstInfo.isSelected()) {
                            iv.setImageResource(R.drawable.ic_choose_green);
                        } else {
                            iv.setImageResource(R.drawable.ic_add_circle_blue);
                        }
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //添加至我的选校，添加按钮进行改变
                                if (smartSchoolRstInfo.isSelected()) {
                                    srtP.deleteMyChoose(smartSchoolRstInfo.getSchoolId(), position);
                                } else {
                                    srtP.add2MySchool(smartSchoolRstInfo.getSchoolId(), smartSchoolRstInfo.getMatch_type(), position);
                                }
                            }
                        });
                    }
                }
            }
        };
        HeaderAndFooterWrapper mFooter = new HeaderAndFooterWrapper(mAdapter);
        mFooter.addFootView(mInflater.inflate(R.layout.layout_smart_footer, null));
        emptyWrapper = new EmptyWrapper<>(mFooter);
        rclvschoolrlt.setAdapter(emptyWrapper);
        new SrtSchoolResultPresenter(this);
        if (schoolInfo == null) {
            schoolInfo = new ArrayList<>();
            llyt_menu.setVisibility(View.GONE);
            ToastUtils.showToast(this, "抱歉，没有与你相匹配的院校哦~");
            srtP.setEmptyView(mInflater, this, rclvschoolrlt);
            srtP.doFinishDelay();
        } else {
            if (schoolInfo.size() == 0) {
                llyt_menu.setVisibility(View.GONE);
                ToastUtils.showToast(this, "抱歉，没有与你相匹配的院校哦~");
                srtP.setEmptyView(mInflater, this, rclvschoolrlt);
                srtP.doFinishDelay();
            }
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                clickPos = position;
                removeActivity(SrtSchoolResultActivity.class.getSimpleName());
                String school_id = schoolInfo.get(position).getSchoolId();
                Bundle params = new Bundle();
                params.putString("id", school_id);
                Intent toMoreDetails = new Intent(SrtSchoolResultActivity.this, SchoolDetailActivity.class);
                toMoreDetails.putExtras(params);
                startActivityForResult(toMoreDetails, ParameterUtils.MSG_WHAT_REFRESH);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_ask).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_share:
                srtP.doShare(intent_data);
                break;
            case R.id.tv_add:
                srtP.addAll2Myschool(schoolInfo);
                break;
            case R.id.tv_ask:
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put("name", (String) SPCacheUtils.get("user_name", ""));
                clientInfo.put("tel", (String) SPCacheUtils.get("user_account", ""));
                Intent intent = new MQIntentBuilder(this)
                        .setClientInfo(clientInfo)
                        .build();
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SrtSchoolResultContract.Presenter presenter) {
        if (presenter != null) {
            srtP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void noitfyItem(int position) {
        if (schoolInfo != null) {
            SmartSchoolRstInfo info = schoolInfo.get(position);
            if (info.isSelected()) {
                info.setSelected(false);
            } else {
                info.setSelected(true);
            }
            emptyWrapper.notifyItemChanged(position);
        }
    }

    @Override
    public void addAll2MyschoolSucc() {
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void goShare(String url, String title, String des) {
        ShareUtils.showShare(this, url,
                title, des, null, null);
    }

    @Override
    public void doFinish() {
        finish();
    }

    @Override
    public void toMsgCenter() {
        startActivity(new Intent(this, MsgCenterActivity.class));
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.MSG_WHAT_REFRESH:
                if (clickPos != -1) {
                    noitfyItem(clickPos);
                }
                break;
            default:
                break;
        }
    }
}
