package com.smartstudy.xxd.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.ui.activity.BrowserPictureActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.photoview.PhotoInfo;
import com.smartstudy.commonlib.ui.customview.photoview.PhotoView;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.mvp.contract.MeFragmentContract;
import com.smartstudy.xxd.mvp.presenter.MeFragmentPresenter;
import com.smartstudy.xxd.ui.activity.AbroadPlanActivity;
import com.smartstudy.xxd.ui.activity.CompareCountryActivity;
import com.smartstudy.xxd.ui.activity.ExamDateActivity;
import com.smartstudy.xxd.ui.activity.GpaCalculationActivity;
import com.smartstudy.xxd.ui.activity.MyChooseSchoolActivity;
import com.smartstudy.xxd.ui.activity.MyCollectionActivity;
import com.smartstudy.xxd.ui.activity.MyInfoActivity;
import com.smartstudy.xxd.ui.activity.SettingActivity;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;
import com.smartstudy.xxd.ui.activity.SrtChooseSchoolActivity;
import com.smartstudy.xxd.ui.activity.SrtChooseSpecialActivity;
import com.smartstudy.xxd.ui.activity.StudyGetPlanningActivity;
import com.smartstudy.xxd.utils.AppContants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smartstudy.commonlib.utils.ParameterUtils.MEIQIA_UNREAD;
import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

public class MeFragment extends BaseFragment implements MeFragmentContract.View {

    private ImageView ivUserPic;
    private TextView tvUserName;
    private TextView tvMeiqiaMsgRed;
    private TextView tvToefl;
    private TextView tvIetls;
    private TextView tvMsk;
    private MeFragmentContract.Presenter presenter;
    private List<String> packageNames;
    private LinearLayout llStudyTimePlanning;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
        UApp.actionEvent(mActivity, "22_B_my");
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        new MeFragmentPresenter(this);
        //获取个人信息，优先从缓存中获取，获取不到再从网络上获取
        String userName = (String) SPCacheUtils.get(USER_NAME, "");
        String userPic = (String) SPCacheUtils.get("user_pic", "");
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPic)) {
            if (ivUserPic != null) {
                DisplayImageUtils.formatPersonImgUrl(mActivity, userPic, ivUserPic);
                tvUserName.setText(userName);
            }
        } else {
            //获取个人信息
            if (presenter != null) {
                presenter.getMyInfo();
            }
        }
        //获取安装包集合
        packageNames = Utils.getPackageNames(mActivity);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        //获取个人信息，优先从缓存中获取，获取不到再从网络上获取
        String userName = (String) SPCacheUtils.get(USER_NAME, "");
        String userPic = (String) SPCacheUtils.get("user_pic", "");
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPic)) {
            if (ivUserPic != null) {
                DisplayImageUtils.formatPersonImgUrl(mActivity, userPic, ivUserPic);
                tvUserName.setText(userName);
            }
        } else {
            //获取个人信息
            if (presenter != null) {
                presenter.getMyInfo();
            }
        }
        //处理美高
        handleUsHighSchool();
        //处理消息红点
        handleRed();
        //处理工具图标
        handToolIcon("com.smartstudy.zhantoefl", tvToefl, R.drawable.logo_toefl);
        handToolIcon("com.smartstudy.zhikeielts", tvIetls, R.drawable.logo_ietls);
        handToolIcon("com.smartstudy.zkmsk", tvMsk, R.drawable.logo_msk);
    }

    //美洽未读消息处理
    public void handleMeiQiaRed(int unread) {
        if (unread > 0) {
            tvMeiqiaMsgRed.setVisibility(View.VISIBLE);
        } else {
            tvMeiqiaMsgRed.setVisibility(View.GONE);
        }
    }

    //处理消息红点
    private void handleRed() {
        int meiqiaUnread = (int) SPCacheUtils.get(MEIQIA_UNREAD, 0);
        handleMeiQiaRed(meiqiaUnread);
    }

    private void handleUsHighSchool() {
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        String targetCountryName = "意向国家";
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            if (targetCountryEntity != null) {
                targetCountryName = targetCountryEntity.getName();
            }
        }
        String projName = (String) SPCacheUtils.get("project_name", "研究生");
        if (("本科".equals(projName) || "研究生".equals(projName)) && "美国".equals(targetCountryName)) {
            //留学规划不显示   高中和其他
            llStudyTimePlanning.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.v_study_time_planning).setVisibility(View.VISIBLE);
        } else {
            llStudyTimePlanning.setVisibility(View.GONE);
            rootView.findViewById(R.id.v_study_time_planning).setVisibility(View.GONE);
        }

        if ("高中".equals(projName) && "美国".equals(targetCountryName)) {
            rootView.findViewById(R.id.llyt_my_school).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.llyt_my_school).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {
        tvMeiqiaMsgRed = rootView.findViewById(R.id.meiqia_red);
        LinearLayout llMyInfo = rootView.findViewById(R.id.llyt_my_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            llMyInfo.setPadding(0, ScreenUtils.getStatusHeight(), 0, 0);
        }
        ivUserPic = (ImageView) rootView.findViewById(R.id.iv_user_pic);
        tvUserName = (TextView) rootView.findViewById(R.id.tv_user_name);
        tvToefl = (TextView) rootView.findViewById(R.id.tv_tf);
        tvIetls = (TextView) rootView.findViewById(R.id.tv_ys);
        tvMsk = (TextView) rootView.findViewById(R.id.tv_msk);
        TextView tvSmartProfession = rootView.findViewById(R.id.tv_smart_profession);
        TextView tvSmartSchool = rootView.findViewById(R.id.tv_smart_school);
        TextView tvGpa = rootView.findViewById(R.id.tv_gpa);
        TextView tvStudyPlan = rootView.findViewById(R.id.tv_study_plan);
        TextView tvTargeWhere = rootView.findViewById(R.id.tv_targe_where);
        TextView tvRemarkDate = rootView.findViewById(R.id.tv_remark_date);
        llMyInfo.setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_school).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_colllect).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_community).setOnClickListener(this);
        rootView.findViewById(R.id.rlyt_service).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_setting).setOnClickListener(this);
        rootView.findViewById(R.id.rl_abroad_service).setOnClickListener(this);
        llStudyTimePlanning = rootView.findViewById(R.id.llyt_study_time_planning);
        handleUsHighSchool();
        tvSmartSchool.setOnClickListener(this);
        tvGpa.setOnClickListener(this);
        llStudyTimePlanning.setOnClickListener(this);
        tvStudyPlan.setOnClickListener(this);
        tvToefl.setOnClickListener(this);
        tvIetls.setOnClickListener(this);
        tvTargeWhere.setOnClickListener(this);
        tvMsk.setOnClickListener(this);
        ivUserPic.setOnClickListener(this);
        tvSmartProfession.setOnClickListener(this);
        tvRemarkDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llyt_study_time_planning:
                UApp.actionEvent(mActivity, "22_A_abroad_plan_cell");
                startActivity(new Intent(mActivity, AbroadPlanActivity.class));
                break;
            case R.id.iv_user_pic:
                String url = (String) SPCacheUtils.get("user_pic", "");
                if (!TextUtils.isEmpty(url)) {
                    PhotoInfo info = PhotoView.getImageViewInfo(ivUserPic);
                    ArrayList<PhotoInfo> infoList = new ArrayList();
                    infoList.add(info);
                    ArrayList<String> list = new ArrayList();
                    list.add(url);
                    Intent toPreview = new Intent(mActivity, BrowserPictureActivity.class);
                    toPreview.putStringArrayListExtra("pathList", list);
                    toPreview.putExtra("index", 0);
                    toPreview.putParcelableArrayListExtra("info", infoList);
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                        R.anim.fade_in, 0);
                    ActivityCompat.startActivity(mActivity, toPreview, compat.toBundle());
                }
                break;
            case R.id.llyt_my_info:
                UApp.actionEvent(mActivity, "22_A_userinfo_cell");
                Intent toSetMyInfo = new Intent(getActivity(), MyInfoActivity.class);
                toSetMyInfo.putExtra("from", "MeFragment");
                startActivity(toSetMyInfo);
                break;
            case R.id.tv_remark_date:
                startActivity(new Intent(mActivity, ExamDateActivity.class).putExtra("plain", true));
                break;
            case R.id.tv_study_plan:
                startActivity(new Intent(mActivity, StudyGetPlanningActivity.class));
                break;
            case R.id.tv_gpa:
                UApp.actionEvent(mActivity, "8_A_gpa_btn");
                startActivity(new Intent(mActivity, GpaCalculationActivity.class));
                break;
            case R.id.tv_smart_school:
                UApp.actionEvent(mActivity, "8_A_choose_school_btn");
                startActivity(new Intent(mActivity, SrtChooseSchoolActivity.class));
                break;
            case R.id.tv_smart_profession:
                UApp.actionEvent(mActivity, "8_A_choose_professional_btn");
                startActivity(new Intent(mActivity, SrtChooseSpecialActivity.class));
                break;
            case R.id.llyt_my_school:
                UApp.actionEvent(mActivity, "22_A_my_schools_cell");
                startActivity(new Intent(mActivity, MyChooseSchoolActivity.class));
                break;
            case R.id.llyt_my_colllect:
                UApp.actionEvent(mActivity, "22_A_my_favorit_cell");
                startActivity(new Intent(mActivity, MyCollectionActivity.class));
                break;
            case R.id.llyt_community:
                joinQQGroup();
                break;
            case R.id.rlyt_service:
                MQConfig.init(mActivity, ParameterUtils.MEIQIA_KEY, new OnInitCallback() {
                    @Override
                    public void onSuccess(String s) {
                        UApp.actionEvent(mActivity, "22_A_consult_cell");
                        SPCacheUtils.put(MEIQIA_UNREAD, 0);
                        HashMap<String, String> clientInfo = new HashMap<>();
                        clientInfo.put(AppContants.NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                        clientInfo.put(AppContants.TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                        Intent intent = new MQIntentBuilder(mActivity)
                            .setClientInfo(clientInfo)
                            .build();
                        clientInfo.clear();
                        clientInfo = null;
                        mActivity.startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.showToast("客服系统启动失败，请稍后重试！");
                    }
                });

                break;
            case R.id.llyt_setting:
                UApp.actionEvent(mActivity, "22_A_setting_cell");
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_targe_where:
                startActivity(new Intent(mActivity, CompareCountryActivity.class));
                break;
            case R.id.tv_tf:
                goTools("com.smartstudy.zhantoefl",
                    "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zhantoefl",
                    "22_A_down_toefl_cell");
                break;
            case R.id.tv_ys:
                goTools("com.smartstudy.zhikeielts",
                    "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zhikeielts",
                    "22_A_down_ielts_cell");
                break;
            case R.id.tv_msk:
                goTools("com.smartstudy.zkmsk",
                    "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zkmsk",
                    "22_A_down_class_cell");
                break;
            case R.id.rl_abroad_service:
                Intent toMoreDetails = new Intent(mActivity, ShowWebViewActivity.class);
                toMoreDetails.putExtra(WEBVIEW_URL, HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_ZHIKE_SERVICE));
                toMoreDetails.putExtra("title", "智课留学服务");
                toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
                startActivity(toMoreDetails);
                break;
            default:
                break;
        }
    }

    /****************
     *
     * 发起添加群流程。群号：选校帝留学群(626350084) 的 key 为：
     * GQSR-ZYkOBwSFArtmyKPw0j5bvWBHL43
     * 调用 joinQQGroup(GQSR-ZYkOBwSFArtmyKPw0j5bvWBHL43)
     * 即可发起手Q客户端申请加群 选校帝留学群(626350084)
     *
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup() {
        String key = "GQSR-ZYkOBwSFArtmyKPw0j5bvWBHL43";
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        String targetCountryName = null;
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            if (targetCountryEntity != null) {
                targetCountryName = targetCountryEntity.getName();
            }
        }
        if ("美国".equals(targetCountryName)) {
            // QQ群517936658
            key = "xc9ybzKGin9aEXU6A2WJwwV8Dq_Jdwuz";
        } else if ("英国".equals(targetCountryName)) {
            // QQ群693023533
            key = "PwL5jjnI5Ydrp043LLbJru9R76HkJtiO";
        } else if ("加拿大".equals(targetCountryName)) {
            // QQ群679888153
            key = "APs-fDaHshRonCsEVnO9hb2vs7s1PWav";
        } else if ("澳大利亚".equals(targetCountryName)) {
            // QQ群794662943
            key = "_90EURKh0Twg01gKt19_mBV9TjnRkpAI";
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?"
            + "url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，
        // 返回手Q主界面，不设置，按返回会返回到呼起产品界面
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            ToastUtils.showToast(getString(R.string.no_qq));
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    @Override
    public void setPresenter(MeFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void getMyInfoSuccess(String jsonObject) {
        if (presenter != null) {
            PersonalInfo personalInfo = JSON.parseObject(jsonObject, PersonalInfo.class);
            if (personalInfo != null) {
                if (personalInfo.getAvatar() != null) {
                    SPCacheUtils.put("user_pic", personalInfo.getAvatar());
                    //图片裁剪
                    if (mActivity != null && !mActivity.isFinishing()) {
                        DisplayImageUtils.formatPersonImgUrl(mActivity, personalInfo.getAvatar(), ivUserPic);
                    }
                }
                tvUserName.setText(personalInfo.getName());
                SPCacheUtils.put(USER_NAME, personalInfo.getName());
                if (personalInfo.getTargetSection() != null) {
                    PersonalInfo.TargetSectionEntity targetSection = personalInfo.getTargetSection();
                    if (targetSection != null) {
                        if (targetSection.getTargetDegree() != null) {
                            SPCacheUtils.put("project_name", targetSection.getTargetDegree().getName());
                        }
                        if (targetSection.getTargetCountry() != null) {
                            SPCacheUtils.put("target_countryInfo", JSON.toJSONString(targetSection.getTargetCountry()));
                        }
                        if (targetSection.getTargetMajorDirection() != null) {
                            SPCacheUtils.put("target_major", JSON.toJSONString(targetSection.getTargetMajorDirection()));
                        }
                        targetSection = null;
                    }
                }
                personalInfo = null;
            }
        }
    }

    /**
     * 用默认的浏览器打开网页
     *
     * @param pgkName 应用包名
     * @param url     应用在应用宝市场的下载地址
     * @param eventId 友盟自定义计数事件ID
     */
    private void goTools(String pgkName, String url, String eventId) {
        if (packageNames != null) {
            if (packageNames.contains(pgkName)) {
                Utils.openApp(mActivity, pgkName);
            } else {
                Utils.openWithWeb(mActivity, url);
                UApp.actionEvent(mActivity, eventId);
            }
        }
    }

    private void handToolIcon(String pgkName, TextView view, int logo) {
        if (packageNames != null) {
            if (packageNames.contains(pgkName)) {
                view.setCompoundDrawablesWithIntrinsicBounds(logo, 0, R.drawable.ic_tool_open, 0);
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(logo, 0, R.drawable.ic_tool_download, 0);
            }
            view = null;
        }
    }
}
