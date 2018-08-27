package com.smartstudy.xxd.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.base.listener.ScrollViewListener;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.entity.RadarData;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.entity.SerializableMap;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.FullyLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.MyScrollview;
import com.smartstudy.commonlib.ui.customview.RadarView;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.customview.guidview.HoleBean;
import com.smartstudy.commonlib.ui.customview.guidview.NewbieGuideManager;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SrtSpecialResultContract;
import com.smartstudy.xxd.mvp.presenter.SrtSpecialResultPresenter;
import com.smartstudy.xxd.utils.AppContants;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

public class SrtSpecialResultActivity extends BaseActivity implements SrtSpecialResultContract.View {

    private RadarView mRadarView;
    private LinearLayout llyt_spe_rst;
    private RecyclerView rclv_spe_rlt;
    private CommonAdapter<SchoolInfo> mAdapter;
    private Dialog mDialog;
    private MyScrollview ms_content;
    private View view_command;
    private TextView tv_command;
    private View showTipView;


    private List<SchoolInfo> schoolInfos;
    private SrtSpecialResultContract.Presenter specP;
    private int clickPos = -1;
    private boolean showTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_special_result);
    }

    @Override
    protected void onDestroy() {
        if (specP != null) {
            specP = null;
        }
        if (rclv_spe_rlt != null) {
            rclv_spe_rlt.removeAllViews();
            rclv_spe_rlt = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schoolInfos != null) {
            schoolInfos.clear();
            schoolInfos = null;
        }
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("智能选专业结果");
        mRadarView = (RadarView) findViewById(R.id.rc_result);
        llyt_spe_rst = (LinearLayout) findViewById(R.id.llyt_spe_rst);
        ms_content = (MyScrollview) findViewById(R.id.ms_content);
        view_command = findViewById(R.id.view_command);
        tv_command = (TextView) findViewById(R.id.tv_command);
        this.rclv_spe_rlt = (RecyclerView) findViewById(R.id.rclv_spe_rlt);
        rclv_spe_rlt.setHasFixedSize(true);
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_spe_rlt.setNestedScrollingEnabled(false);
        rclv_spe_rlt.setLayoutManager(mLayoutManager);
        rclv_spe_rlt.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        schoolInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<SchoolInfo>(this, R.layout.item_school_list, schoolInfos) {

            @Override
            protected void convert(ViewHolder holder, final SchoolInfo schoolInfo, final int position) {
                if (schoolInfo != null) {
                    if (schoolInfo != null) {
                        final ImageView iv = holder.getView(R.id.iv_add_school);
                        if (position == 0) {
                            if (showTipView == null) {
                                showTipView = iv;
                            }
                        }
                        if (holder.getView(R.id.llyt_smart_rate).getVisibility() == View.GONE) {
                            holder.getView(R.id.llyt_smart_rate).setVisibility(View.VISIBLE);
                            holder.getView(R.id.tv_smart_rate).setVisibility(View.GONE);
                            iv.setVisibility(View.VISIBLE);
                        }
                        holder.setCircleImageUrl(R.id.iv_logo, schoolInfo.getLogo(), true);
                        holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                        holder.setText(R.id.tv_English_name, schoolInfo.getEnglishName());
                        String rank = schoolInfo.getLocalRank();
                        holder.setText(R.id.tv_local_rank, String.format(getString(R.string.local_rank),
                            TextUtils.isEmpty(rank) ? "暂无" : rank));
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
                                    specP.deleteMyChoose(schoolInfo.getId() + "", position);
                                } else {
                                    showDialog(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {
                                                case R.id.llyt_top:
                                                    specP.add2MySchool(ParameterUtils.MATCH_TOP, schoolInfo.getId(), position);
                                                    mDialog.dismiss();
                                                    break;
                                                case R.id.llyt_mid:
                                                    specP.add2MySchool(ParameterUtils.MATCH_MID, schoolInfo.getId(), position);
                                                    mDialog.dismiss();
                                                    break;
                                                case R.id.llyt_bottom:
                                                    specP.add2MySchool(ParameterUtils.MATCH_BOT, schoolInfo.getId(), position);
                                                    mDialog.dismiss();
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        };
        rclv_spe_rlt.setAdapter(mAdapter);
        mRadarView.setEmptyHint("无数据");
        Intent data = getIntent();
        //得到数据集
        Bundle bundle = data.getExtras();
        //获得自定义类
        SerializableMap serializableMap = (SerializableMap) bundle
            .get("data");
        //填充雷达图
        setData(serializableMap.getMap());
        //展示适合类型
        showFitMsg(JSON.parseArray(data.getStringExtra("fit")));
        new SrtSpecialResultPresenter(this);
        //获取推荐学校
        specP.getRecSchools(data.getStringExtra("rec"), schoolInfos);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                clickPos = position;
                removeActivity(SrtSchoolResultActivity.class.getSimpleName());
                int school_id = schoolInfos.get(position).getId();
                Bundle params = new Bundle();
                params.putString("id", school_id + "");
                Intent toMoreDetails = new Intent(SrtSpecialResultActivity.this, CollegeDetailActivity.class);
                toMoreDetails.putExtras(params);
                startActivityForResult(toMoreDetails, ParameterUtils.REQUEST_CODE_WEBVIEW);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        ms_content.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (view_command.getBottom() - y < ms_content.getHeight() && !showTip) {
                    //滚动到推荐学校并展示提示遮罩层
                    showTip = true;
                    ms_content.smoothScrollTo(0, tv_command.getTop());
                    if (showTip) {
                        if (NewbieGuideManager.isNeverShowed(SrtSpecialResultActivity.this, NewbieGuideManager.TYPE_SPE_RESULT)) {
                            showTip = false;
                            if (showTipView != null) {
                                new NewbieGuideManager(SrtSpecialResultActivity.this, NewbieGuideManager.TYPE_SPE_RESULT).addView(showTipView,
                                    HoleBean.TYPE_CIRCLE).show();
                            }
                        }
                    }
                }
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
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tv_share:
                String url = HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_CHOOSE_SPEC);
                UMShareUtils.showShare(this, url, "智能选专业",
                    "点击查看详细内容", null, new ShareListener(url, "result_smart_major"));
                break;
            case R.id.tv_add:
                showDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.llyt_top:
                                specP.addAll2Myschool(ParameterUtils.MATCH_TOP, schoolInfos);
                                mDialog.dismiss();
                                break;
                            case R.id.llyt_mid:
                                specP.addAll2Myschool(ParameterUtils.MATCH_MID, schoolInfos);
                                mDialog.dismiss();
                                break;
                            case R.id.llyt_bottom:
                                specP.addAll2Myschool(ParameterUtils.MATCH_BOT, schoolInfos);
                                mDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case R.id.tv_ask:
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put(AppContants.NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                clientInfo.put(AppContants.TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
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
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void setData(Map<String, Object> map) {
        List<Float> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            values.add((float) entry.getValue());
        }
        List<String> vertexText = new ArrayList<>();
        Collections.addAll(vertexText, "艺术型", "研究型", "社会型", "传统型", "实际型", "企业型");
        mRadarView.setVertexText(vertexText);
        RadarData data = new RadarData(values);
        data.setColor(getResources().getColor(R.color.app_main_color));
        mRadarView.addData(data);
    }

    private void showFitMsg(JSONArray arr) {
        if (arr != null) {
            for (int i = 0, len = arr.size(); i < len; i++) {
                JSONObject data = arr.getJSONObject(i);
                View view = getLayoutInflater().inflate(R.layout.item_qa_result_list, null);
                TextView tv_fit_type = (TextView) view.findViewById(R.id.tv_fit_type);
                TextView tv_fit_intro = (TextView) view.findViewById(R.id.tv_fit_intro);
                TextView tv_fit_msg = (TextView) view.findViewById(R.id.tv_fit_msg);
                View rst_line = view.findViewById(R.id.rst_line);
                if (i == len - 1) {
                    rst_line.setVisibility(View.GONE);
                }
                tv_fit_type.setText(String.format(getString(R.string.fit_type), data.getString("type")));
                tv_fit_intro.setText(data.getString("introduction"));
                tv_fit_msg.setText(Html.fromHtml("<font style=\"font-weight:bold;\">可参考填报</font>" + "\n\n" + data.getString("majorFavor")));
                llyt_spe_rst.addView(view);
            }
        }
    }

    private void showDialog(View.OnClickListener listener) {
        mDialog = DialogCreator.createAdd2SchoolDialog(this, "添加为",
            listener);
        mDialog.getWindow().setLayout((int) (0.9 * ScreenUtils.getScreenWidth()),
            WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    @Override
    public void setPresenter(SrtSpecialResultContract.Presenter presenter) {
        if (presenter != null) {
            specP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }


    @Override
    public void dataRefres() {
        if (specP != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noitfyItem(int position) {
        if (specP != null) {
            SchoolInfo info = schoolInfos.get(position);
            if (info.isSelected()) {
                info.setSelected(false);
            } else {
                info.setSelected(true);
            }
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void addAll2MyschoolSucc() {
        if (specP != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //涉及到分享时必须调用到方法
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_WEBVIEW:
                if (clickPos != -1) {
                    if (data.getBooleanExtra("isTrue", false)) {
                        noitfyItem(clickPos);
                    }
                }
                break;
            default:
                break;
        }
    }

}
