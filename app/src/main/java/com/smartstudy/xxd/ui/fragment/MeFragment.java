package com.smartstudy.xxd.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.customView.transferImage.loader.GlideImageLoader;
import com.smartstudy.commonlib.ui.customView.transferImage.style.progress.ProgressPieIndicator;
import com.smartstudy.commonlib.ui.customView.transferImage.transfer.TransferConfig;
import com.smartstudy.commonlib.ui.customView.transferImage.transfer.Transferee;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.mvp.contract.MeFragmentContract;
import com.smartstudy.xxd.mvp.presenter.MeFragmentPresenter;
import com.smartstudy.xxd.ui.activity.MsgCenterActivity;
import com.smartstudy.xxd.ui.activity.MyChooseSchoolActivity;
import com.smartstudy.xxd.ui.activity.MyCollectionActivity;
import com.smartstudy.xxd.ui.activity.MyInfoActivity;
import com.smartstudy.xxd.ui.activity.QaListActivity;
import com.smartstudy.xxd.ui.activity.SettingActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MeFragment extends UIFragment implements MeFragmentContract.View {
    private ImageView iv_user_pic;
    private TextView tv_user_name;
    private TextView meiqia_red;
    private TextView msg_red;
    private TextView tv_tf;
    private TextView tv_ys;
    private TextView tv_msk;

    private MeFragmentContract.Presenter meP;
    private List<String> packageNames;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_me, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        new MeFragmentPresenter(this);
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
            rootView.findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
            //获取个人信息，优先从缓存中获取，获取不到再从网络上获取
            String user_name = (String) SPCacheUtils.get("user_name", ParameterUtils.CACHE_NULL);
            String user_pic = (String) SPCacheUtils.get("user_pic", ParameterUtils.CACHE_NULL);
            if (!ParameterUtils.CACHE_NULL.equals(user_name) && !ParameterUtils.CACHE_NULL.equals(user_pic)
                    && !TextUtils.isEmpty(user_pic)) {
                DisplayImageUtils.formatPersonImgUrl(mActivity, user_pic, iv_user_pic);
                tv_user_name.setText(user_name);
            } else {
                //获取个人信息
                meP.getMyInfo();
            }
        } else {
            DisplayImageUtils.displayImageRes(mActivity, R.drawable.ic_person_default, iv_user_pic);
            rootView.findViewById(R.id.tv_tips).setVisibility(View.GONE);
            tv_user_name.setText("请先登录");
        }
        //处理消息红点
        handleRed();
        //处理工具图标
        packageNames = Utils.getPackageNames(mActivity);
        handToolIcon("com.smartstudy.zhantoefl", tv_tf, R.drawable.logo_toefl);
        handToolIcon("com.smartstudy.zhikeielts", tv_ys, R.drawable.logo_ietls);
        handToolIcon("com.smartstudy.zkmsk", tv_msk, R.drawable.logo_msk);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
            rootView.findViewById(R.id.tv_tips).setVisibility(View.VISIBLE);
            //获取个人信息，优先从缓存中获取，获取不到再从网络上获取
            String user_name = (String) SPCacheUtils.get("user_name", ParameterUtils.CACHE_NULL);
            String user_pic = (String) SPCacheUtils.get("user_pic", ParameterUtils.CACHE_NULL);
            if (!ParameterUtils.CACHE_NULL.equals(user_name) && !ParameterUtils.CACHE_NULL.equals(user_pic)
                    && !TextUtils.isEmpty(user_pic)) {
                DisplayImageUtils.formatPersonImgUrl(mActivity, user_pic, iv_user_pic);
                tv_user_name.setText(user_name);
            } else {
                //获取个人信息
                if (meP != null) {
                    meP.getMyInfo();
                }
            }
        } else {
            DisplayImageUtils.displayImageRes(mActivity, R.drawable.ic_person_default, iv_user_pic);
            rootView.findViewById(R.id.tv_tips).setVisibility(View.GONE);
            tv_user_name.setText("请先登录");
        }
        //处理消息红点
        handleRed();
        //处理工具图标
        if (packageNames != null) {
            handToolIcon("com.smartstudy.zhantoefl", tv_tf, R.drawable.logo_toefl);
            handToolIcon("com.smartstudy.zhikeielts", tv_ys, R.drawable.logo_ietls);
            handToolIcon("com.smartstudy.zkmsk", tv_msk, R.drawable.logo_msk);
        }
    }

    //美洽未读消息处理
    public void handleMeiQiaRed(int unread) {
        if (unread > 0) {
            meiqia_red.setVisibility(View.VISIBLE);
        } else {
            meiqia_red.setVisibility(View.GONE);
        }
    }

    //极光未读消息处理
    public void handleMsgRed(int unread) {
        if (unread > 0) {
            msg_red.setVisibility(View.VISIBLE);
        } else {
            msg_red.setVisibility(View.GONE);
        }
    }

    private void handleRed() {
        int meiqia_unread = (int) SPCacheUtils.get("meiqia_unread", 0);
        handleMeiQiaRed(meiqia_unread);
        int xxd_unread = (int) SPCacheUtils.get("xxd_unread", 0);
        handleMsgRed(xxd_unread);
    }

    @Override
    protected void initView(View rootView) {
        meiqia_red = (TextView) rootView.findViewById(R.id.meiqia_red);
        msg_red = (TextView) rootView.findViewById(R.id.msg_red);
        LinearLayout llyt_my_info = (LinearLayout) rootView.findViewById(R.id.llyt_my_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            llyt_my_info.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);

        }
        iv_user_pic = (ImageView) rootView.findViewById(R.id.iv_user_pic);
        tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
        tv_tf = (TextView) rootView.findViewById(R.id.tv_tf);
        tv_ys = (TextView) rootView.findViewById(R.id.tv_ys);
        tv_msk = (TextView) rootView.findViewById(R.id.tv_msk);
        llyt_my_info.setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_school).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_colllect).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_qa).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_my_msg).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_community).setOnClickListener(this);
        rootView.findViewById(R.id.rlyt_service).setOnClickListener(this);
        rootView.findViewById(R.id.llyt_setting).setOnClickListener(this);
        tv_tf.setOnClickListener(this);
        tv_ys.setOnClickListener(this);
        tv_msk.setOnClickListener(this);
        iv_user_pic.setOnClickListener(this);
        rootView = null;
    }

    @Override
    public void onClick(View v) {
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        switch (v.getId()) {
            case R.id.iv_user_pic:
                String url = (String) SPCacheUtils.get("user_pic", ParameterUtils.CACHE_NULL);
                if (!ParameterUtils.CACHE_NULL.equals(ticket) && !ParameterUtils.CACHE_NULL.equals(url)) {
                    TransferConfig config = TransferConfig.build()
                            .setSourceImageList(Arrays.asList(url))
                            .setProgressIndicator(new ProgressPieIndicator())
                            .setImageLoader(GlideImageLoader.with(mActivity.getApplicationContext()))
                            .create();
                    config.setNowThumbnailIndex(0);
                    config.setOriginImageList(Arrays.asList(iv_user_pic));
                    Transferee.getDefault(mActivity).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                        @Override
                        public void onShow() {
                            DisplayImageUtils.pauseRequest(mActivity);
                        }

                        @Override
                        public void onDismiss() {
                            DisplayImageUtils.resumeRequest(mActivity);
                        }
                    });
                }
                break;
            case R.id.llyt_my_info:
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    Intent toSetMyInfo = new Intent(getActivity(), MyInfoActivity.class);
                    startActivity(toSetMyInfo);
                } else {
                    Intent toLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(toLogin);
                }
                break;
            case R.id.llyt_my_school:
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    startActivity(new Intent(mActivity, MyChooseSchoolActivity.class));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.llyt_my_colllect:
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    startActivity(new Intent(mActivity, MyCollectionActivity.class));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.llyt_my_qa:
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    Bundle qa_data = new Bundle();
                    qa_data.putString("data_flag", "my");
                    startActivity(new Intent(mActivity, QaListActivity.class)
                            .putExtras(qa_data));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.llyt_my_msg:
                SPCacheUtils.put("xxd_unread", 0);
                startActivity(new Intent(mActivity, MsgCenterActivity.class));
                break;
            case R.id.llyt_community:
                joinQQGroup("GQSR-ZYkOBwSFArtmyKPw0j5bvWBHL43");
                break;
            case R.id.rlyt_service:
                SPCacheUtils.put("meiqia_unread", 0);
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put("name", (String) SPCacheUtils.get("user_name", ""));
                clientInfo.put("tel", (String) SPCacheUtils.get("user_account", ""));
                Intent intent = new MQIntentBuilder(mActivity)
                        .setClientInfo(clientInfo)
                        .build();
                clientInfo.clear();
                clientInfo = null;
                mActivity.startActivity(intent);
                break;
            case R.id.llyt_setting:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.tv_tf:
                goTools("com.smartstudy.zhantoefl", "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zhantoefl", "toefl_app_down");
                break;
            case R.id.tv_ys:
                goTools("com.smartstudy.zhikeielts", "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zhikeielts", "ielts_app_down");
                break;
            case R.id.tv_msk:
                goTools("com.smartstudy.zkmsk", "http://a.app.qq.com/o/simple.jsp?pkgname=com.smartstudy.zkmsk", "zkmsk_app_down");
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
     * @param key 由官网生成的key

     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，
        // 返回手Q主界面，不设置，按返回会返回到呼起产品界面
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            ToastUtils.showToast(mActivity, getString(R.string.no_qq));
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    @Override
    public void setPresenter(MeFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.meP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(mActivity, message);
    }

    @Override
    public void getMyInfoSuccess(String jsonObject) {
        if (meP != null) {
            PersonalInfo personalInfo = JSON.parseObject(jsonObject, PersonalInfo.class);
            if (personalInfo != null) {
                if (personalInfo.getAvatar() != null) {
                    SPCacheUtils.put("user_pic", personalInfo.getAvatar());
                    //图片裁剪
                    String params = "?x-oss-process=image/resize,m_pad,w_" + iv_user_pic.getWidth() + ",h_" + iv_user_pic.getHeight();
                    DisplayImageUtils.displayPersonImage(mActivity.getApplicationContext(), personalInfo.getAvatar() + params, iv_user_pic);
                }
                tv_user_name.setText(personalInfo.getName());
                SPCacheUtils.put("user_name", personalInfo.getName());
                if (personalInfo.getTargetSection() != null) {
                    PersonalInfo.TargetSectionEntity targetSection = personalInfo.getTargetSection();
                    if (targetSection != null) {
                        if (targetSection.getTargetDegree() != null) {
                            SPCacheUtils.put("project_name", targetSection.getTargetDegree().getName());
                        }
                        if (targetSection.getTargetCountry() != null) {
                            SPCacheUtils.put("target_countryInfo", JSON.toJSONString(targetSection.getTargetCountry()));
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
                MobclickAgent.onEvent(mActivity, eventId);
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
