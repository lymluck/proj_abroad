package com.smartstudy.xxd.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.MySchoolInfo;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.mvp.contract.MySchoolContract;
import com.smartstudy.xxd.mvp.presenter.MySchoolPresenter;
import com.smartstudy.xxd.ui.animators.SlideInRightAnimator;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

public class MyChooseSchoolActivity extends BaseActivity implements MySchoolContract.View {

    private LinearLayout llyt_sample;
    private LinearLayout llyt_sample_menu;
    private PopupWindow mMenuPopWindow;
    private View mMenuView;
    private RecyclerView rcv_mySchool;
    private CommonAdapter<MySchoolInfo> mAdapter;
    private View click_view;
    private TextView tv_myschool_tip;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView topdefault_righttext;

    private List<MySchoolInfo> schoolInfo;
    private boolean is_sample;
    private MySchoolContract.Presenter myInfoP;
    private int click_position;
    private int screenHeight;
    private int popHeight;
    // 保存当前item坐标的数组
    private int[] Pos = {-1, -1};
    private boolean isNotTarget;
    private WeakHandler mHandler;
    private String targetCountryId;
    private String targetCountryName;
    private ArrayList<IdNameInfo> countryList;
    // 判断是否有过隐私设置的字段，进入界面初始化
    private boolean selectVivible = true;
    private TextView tvLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_choose_school);
    }

    @Override
    protected void onDestroy() {
        if (myInfoP != null) {
            myInfoP = null;
        }
        if (rcv_mySchool != null) {
            rcv_mySchool.removeAllViews();
            rcv_mySchool = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schoolInfo != null) {
            schoolInfo.clear();
            schoolInfo = null;
        }
        if (countryList != null) {
            countryList.clear();
            countryList = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            if (targetCountryEntity != null) {
                targetCountryId = targetCountryEntity.getId();
                targetCountryName = targetCountryEntity.getName();
                countryList = targetCountryEntity.getOptions();
            }
            if (TextUtils.isEmpty(targetCountryName) || "null".equals(targetCountryName)) {
                targetCountryName = "意向国家";
            }
            topdefault_righttext.setText(targetCountryName);
            topdefault_righttext.setTextColor(getResources().getColor(R.color.app_main_color));
            topdefault_righttext.setVisibility(View.VISIBLE);
            tv_myschool_tip.setText(String.format(getString(R.string.myschool_tip), targetCountryName));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initViewAndData() {
        topdefault_righttext = (TextView) findViewById(R.id.topdefault_righttext);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srfl_my_school);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myInfoP.getMySchool(ParameterUtils.NETWORK_ELSE_CACHED, schoolInfo);
            }
        });
        llyt_sample = (LinearLayout) findViewById(R.id.llyt_sample);
        rcv_mySchool = (RecyclerView) findViewById(R.id.rcv_mySchool);
        tv_myschool_tip = (TextView) findViewById(R.id.tv_myschool_tip);
        tvLock = findViewById(R.id.tv_lock);
        rcv_mySchool.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_mySchool.setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) rcv_mySchool.getItemAnimator()).setSupportsChangeAnimations(false);
        rcv_mySchool.setItemAnimator(new SlideInRightAnimator());
        rcv_mySchool.getItemAnimator().setRemoveDuration(500);
        rcv_mySchool.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(7)).colorResId(R.color.main_bg).build());
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rcv_mySchool.requestLayout();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        initAdapter();
        rcv_mySchool.setAdapter(mAdapter);
        llyt_sample_menu = (LinearLayout) findViewById(R.id.llyt_sample_menu);
        TextView topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_centertitle.setText("我的选校");
        mMenuView = LayoutInflater.from(this).inflate(R.layout.school_drop_down_menu, null);
        mMenuPopWindow = new PopupWindow(mMenuView, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true);
        mMenuPopWindow.getContentView().measure(0, 0);
        popHeight = mMenuPopWindow.getContentView().getMeasuredHeight();
        screenHeight = ScreenUtils.getScreenHeight();
        new MySchoolPresenter(this);
        myInfoP.getMySchool(ParameterUtils.NETWORK_ELSE_CACHED, schoolInfo);
    }

    private void initAdapter() {
        schoolInfo = new ArrayList<>();
        mAdapter = new CommonAdapter<MySchoolInfo>(this, R.layout.item_myschool_list, schoolInfo) {

            @Override
            protected void convert(ViewHolder holder, MySchoolInfo mySchoolInfo, final int position) {
                if (mySchoolInfo != null) {
                    if (position == 0) {
                        holder.getView(R.id.view_my_school).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.view_my_school).setVisibility(View.GONE);
                    }
                    JSONObject school = JSON.parseObject(mySchoolInfo.getSchool());
                    if (school != null) {
                        if (school.getString("worldRank") != null) {
                            holder.setText(R.id.tv_school_rank, school.getString("worldRank"));
                        } else {
                            holder.setText(R.id.tv_school_rank, "N/A");
                        }
                        TextView schoolName = holder.getView(R.id.tv_school_name);
                        if (school.getString("chineseName") != null) {
                            schoolName.setText(school.getString("chineseName"));
                        } else {
                            if (school.getString("englishName") != null) {
                                schoolName.setText(school.getString("englishName"));
                            } else {
                                schoolName.setText("暂无");
                            }
                        }
                        if (school.getString("chineseName") != null) {
                            holder.setText(R.id.tv_select_count, String.format(getString(R.string.select_count), school.getString("selectedCount")));
                        } else {
                            holder.setText(R.id.tv_select_count, String.format(getString(R.string.select_count), 0 + ""));
                        }

                        String country_id = school.getString("countryId");
                        if ("COUNTRY_226".equals(targetCountryId)
                            || "COUNTRY_225".equals(targetCountryId) || "COUNTRY_40".equals(targetCountryId)
                            || "COUNTRY_16".equals(targetCountryId)) {
                            if (!targetCountryId.equals(country_id)) {
                                isNotTarget = true;
                                holder.getView(R.id.iv_question).setVisibility(View.VISIBLE);
                            } else {
                                holder.getView(R.id.iv_question).setVisibility(View.GONE);
                            }
                        } else {
                            if ("COUNTRY_226".equals(country_id)
                                || "COUNTRY_225".equals(country_id) || "COUNTRY_40".equals(country_id)
                                || "COUNTRY_16".equals(country_id)) {
                                isNotTarget = true;
                                holder.getView(R.id.iv_question).setVisibility(View.VISIBLE);
                            } else {
                                holder.getView(R.id.iv_question).setVisibility(View.GONE);
                            }
                        }
                        if (isNotTarget) {
                            if (tv_myschool_tip.getVisibility() == View.GONE) {
                                tv_myschool_tip.setText(String.format(getString(R.string.myschool_tip), targetCountryName));
                                tv_myschool_tip.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (tv_myschool_tip.getVisibility() == View.VISIBLE) {
                                tv_myschool_tip.setVisibility(View.GONE);
                                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 500);
                            }
                        }
                    }
                    TextView tv_status = holder.getView(R.id.tv_status);
                    changeStatus(mySchoolInfo.getMatchTypeId(), tv_status);
                    final LinearLayout llyt_menu = holder.getView(R.id.llyt_menu);
                    llyt_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click_view = llyt_menu;
                            click_position = position;
                            showMenuPopWindow(llyt_menu);
                        }
                    });
                }
            }
        };
    }

    @Override
    public void initEvent() {
        topdefault_righttext.setOnClickListener(this);
        llyt_sample.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < schoolInfo.size()) {
                    MySchoolInfo info = schoolInfo.get(position);
                    JSONObject school = JSON.parseObject(info.getSchool());
                    if (school != null) {
                        String name = school.getString("chineseName");
                        if (TextUtils.isEmpty(name)) {
                            name = school.getString("englishName");
                            if (TextUtils.isEmpty(name)) {
                                name = "暂无";
                            }
                        }
                        startActivity(new Intent(MyChooseSchoolActivity.this, SchoolChooseInfoActivity.class)
                            .putExtra(TITLE, name)
                            .putExtra("id", school.getString("id")));
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        // 冲刺
        LinearLayout llyt_top = (LinearLayout) mMenuView.findViewById(R.id.llyt_top);
        llyt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if (is_sample) {
                    TextView tv_status = (TextView) click_view.findViewById(R.id.tv_status);
                    tv_status.setText("冲");
                    tv_status.setBackgroundResource(R.drawable.bg_oval_red_size);
                } else {
                    if (click_position < schoolInfo.size()) {
                        JSONObject school = JSON.parseObject(schoolInfo.get(click_position).getSchool());
                        if (school != null) {
                            myInfoP.editMySchool(school.getString("id"), ParameterUtils.MATCH_TOP);
                        }
                    }
                }
            }
        });
        // 核心
        LinearLayout llyt_mid = (LinearLayout) mMenuView.findViewById(R.id.llyt_mid);
        llyt_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if (is_sample) {
                    TextView tv_status = (TextView) click_view.findViewById(R.id.tv_status);
                    tv_status.setText("核");
                    tv_status.setBackgroundResource(R.drawable.bg_oval_blue_size);
                } else {
                    //下发修改状态请求
                    if (click_position < schoolInfo.size()) {
                        JSONObject school = JSON.parseObject(schoolInfo.get(click_position).getSchool());
                        if (school != null) {
                            myInfoP.editMySchool(school.getString("id"), ParameterUtils.MATCH_MID);
                        }
                    }
                }
            }
        });
        // 保底
        LinearLayout llyt_bottom = (LinearLayout) mMenuView.findViewById(R.id.llyt_bottom);
        llyt_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if (is_sample) {
                    TextView tv_status = (TextView) click_view.findViewById(R.id.tv_status);
                    tv_status.setText("保");
                    tv_status.setBackgroundResource(R.drawable.bg_oval_green_size);
                } else {
                    //下发修改状态请求
                    if (click_position < schoolInfo.size()) {
                        JSONObject school = JSON.parseObject(schoolInfo.get(click_position).getSchool());
                        if (school != null) {
                            myInfoP.editMySchool(school.getString("id"), ParameterUtils.MATCH_BOT);
                        }
                    }
                }
            }
        });
        // 删除
        LinearLayout llyt_del = (LinearLayout) mMenuView.findViewById(R.id.llyt_del);
        llyt_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
                if (is_sample) {
                    llyt_sample.setVisibility(View.GONE);
                } else {
                    //下发删除请求
                    if (click_position < schoolInfo.size()) {
                        JSONObject school = JSON.parseObject(schoolInfo.get(click_position).getSchool());
                        myInfoP.deleteMySchool(school.getString("id"), click_position);
                    }
                }
            }
        });
        mMenuPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色还原
                Utils.convertActivityToTranslucent(MyChooseSchoolActivity.this);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_add_school).setOnClickListener(this);
        findViewById(R.id.btn_privacy_setting).setOnClickListener(this);
        llyt_sample_menu.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.llyt_sample:
                startActivity(new Intent(MyChooseSchoolActivity.this, SchoolChooseInfoActivity.class)
                    .putExtra(TITLE, "哈佛大学")
                    .putExtra("id", "4"));
                break;
            case R.id.btn_add_school:
                Intent toSearch = new Intent(MyChooseSchoolActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.MYSCHOOL_FLAG);
                startActivityForResult(toSearch, ParameterUtils.REQUEST_CODE_ADD_SCHOOL);
                break;
            case R.id.llyt_sample_menu:
                click_view = llyt_sample_menu;
                showMenuPopWindow(llyt_sample_menu);
                break;
            case R.id.topdefault_righttext:
                Intent toChooseIntent = new Intent(this, ChooseListActivity.class);
                toChooseIntent.putExtra("value", targetCountryName);
                toChooseIntent.putParcelableArrayListExtra("list", countryList);
                toChooseIntent.putExtra("ischange", true);
                toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                toChooseIntent.putExtra(TITLE, "我的意向国家");
                startActivityForResult(toChooseIntent, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.btn_privacy_setting:
                DialogCreator.createPrivacyDialog(this, selectVivible, new DialogCreator.PrivacyOnClick() {
                    @Override
                    public void allCanseOnClick() {
                        myInfoP.setPrivacy(true);
                    }

                    @Override
                    public void noCanseOnClick() {
                        myInfoP.setPrivacy(false);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_ADD_SCHOOL:
                myInfoP.getMySchool(ParameterUtils.NETWORK_ELSE_CACHED, schoolInfo);
                break;
            default:
                break;
        }
    }

    /**
     * 显示下拉菜单
     *
     * @param view
     */
    public void showMenuPopWindow(View view) {
        Utils.convertActivityFromTranslucent(MyChooseSchoolActivity.this);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = .8f;
        getWindow().setAttributes(lp);
        mMenuPopWindow.setTouchable(true);
        mMenuPopWindow.setOutsideTouchable(true);
        mMenuPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
            (Bitmap) null));
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        } else {
            view.getLocationOnScreen(Pos);
            int offY = DensityUtils.dip2px(-60);
            //位置修正
            int fix = DensityUtils.dip2px(10);
            int viewH = view.getHeight();
            int offset = screenHeight - Pos[1] - offY - viewH - popHeight - fix;
            if (offset > 0) {
                mMenuPopWindow.showAsDropDown(view, DensityUtils.dip2px(-85),
                    offY);
            } else {
                mMenuPopWindow.showAsDropDown(view, DensityUtils.dip2px(-85),
                    -(popHeight - screenHeight + Pos[1] + viewH + fix));
            }

        }
    }

    public void dismissPopWindow() {
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        }
    }

    @Override
    public void setPresenter(MySchoolContract.Presenter presenter) {
        if (presenter != null) {
            this.myInfoP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (myInfoP != null) {
            if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
                if (!isFinishing()) {
                    DialogCreator.createLoginDialog(this);
                }
            }
            if (ParameterUtils.RESPONE_CODE_NETERR.equals(errCode)) {
                if (mAdapter.getDatas().size() <= 0) {
                    llyt_sample.setVisibility(View.VISIBLE);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getMySchoolSuccess(int school_len, boolean selectVivible) {
        this.selectVivible = selectVivible;
        if (myInfoP != null) {
            swipeRefreshLayout.setRefreshing(false);
            if (school_len > 0) {
                isNotTarget = false;
                llyt_sample.setVisibility(View.GONE);
                is_sample = false;
                mAdapter.notifyDataSetChanged();
            } else {
                is_sample = true;
                tv_myschool_tip.setVisibility(View.GONE);
                llyt_sample.setVisibility(View.VISIBLE);
            }
            //如果是设置过隐私的，图标需要进行更换
            if (selectVivible) {
                Drawable img = getResources().getDrawable(R.drawable.ic_unlock_blue);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                tvLock.setCompoundDrawables(img, null, null, null); //设置左图标
            } else {
                Drawable img = getResources().getDrawable(R.drawable.ic_lock_blue);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                tvLock.setCompoundDrawables(img, null, null, null); //设置左图标
            }
        }
    }

    @Override
    public void editMySchoolSuccess(String match_type_id) {
        if (myInfoP != null) {
            TextView tv_status = (TextView) click_view.findViewById(R.id.tv_status);
            changeStatus(match_type_id, tv_status);
            myInfoP.getMySchool(ParameterUtils.NETWORK_ELSE_CACHED, schoolInfo);
        }
    }

    @Override
    public void delMySchoolSuccess(int position) {
        if (myInfoP != null) {
            if (position < mAdapter.getItemCount()) {
                mAdapter.remove(position);
            }
        }
    }

    @Override
    public void setPrivacySuccess(boolean visible) {
        selectVivible = visible;
        //如果是设置过隐私的，图标需要进行更换
        if (visible) {
            Drawable img = getResources().getDrawable(R.drawable.ic_unlock_blue);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvLock.setCompoundDrawables(img, null, null, null); //设置左图标
        } else {
            Drawable img = getResources().getDrawable(R.drawable.ic_lock_blue);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvLock.setCompoundDrawables(img, null, null, null); //设置左图标
        }
    }

    private void changeStatus(String match_type_id, TextView tv_status) {
        if (ParameterUtils.MATCH_TOP.equals(match_type_id)) {
            tv_status.setText("冲");
            tv_status.setBackgroundResource(R.drawable.bg_oval_red_size);
        } else if (ParameterUtils.MATCH_MID.equals(match_type_id)) {
            tv_status.setText("核");
            tv_status.setBackgroundResource(R.drawable.bg_oval_blue_size);
        } else if (ParameterUtils.MATCH_BOT.equals(match_type_id)) {
            tv_status.setText("保");
            tv_status.setBackgroundResource(R.drawable.bg_oval_green_size);
        }
    }
}
